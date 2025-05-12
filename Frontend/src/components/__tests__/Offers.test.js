import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { createPinia } from 'pinia'
import OffersComponent from '@/components/Offers.vue'
import { deleteOfferById } from '@/services/api'

const push = vi.fn()

vi.mock('vue-router', () => ({
  useRouter: () => ({ push })
}))

vi.mock('@/stores/alerts', () => ({
  alertStore: () => ({
    addAlert: vi.fn()
  })
}))

vi.mock('@/services/api', () => ({
  getAllOffers: vi.fn(() => Promise.resolve({
    data: [
      {
        idOffer: 1,
        name: 'Test',
        date: '2025-01-01T01:00:00.000+00:00',
        client: {
          idClient: 1,
          name: 'Client 1',
          surname: 'Client 1',
          email: 'diegohg8@email.com',
          company: 'Company 1',
          phone: '+34111111111',
          address: 'Dirección 1',
          city: 'Madrid',
          zipCode: 28080,
          country: 'España'
        },
        status: 'Borrador',
        totalPrice: 10.0
      },
      {
        idOffer: 2,
        name: 'Test 1',
        date: '2025-04-01T01:00:00.000+00:00',
        client: {
          idClient: 2,
          name: 'Client 2',
          surname: 'Client 2',
          email: 'Client.2@email.com',
          company: 'Company 2',
          phone: '+34222222222',
          address: 'Dirección 2',
          city: 'Madrid',
          zipCode: 28080,
          country: 'España'
        },
        status: 'Pendiente de enviar',
        totalPrice: 10.0
      }
    ]
  })),
  deleteOfferById: vi.fn(() => Promise.resolve())
}))

describe('Offers.vue', () => {
  let component

  beforeEach(async () => {
    component = mount(OffersComponent, {
      global: {
        plugins: [createPinia()]
      }
    })

    await new Promise(resolve => setTimeout(resolve, 0))
    await component.vm.$nextTick()
  })

  it('Las ofertas se cargan bien', () => {
    const rows = component.findAll('tbody tr')
    expect(rows.length).toBe(2)
    expect(component.text()).toContain('Test')
    expect(component.text()).toContain('Test 1')
  })

  it('Permite seleccionar y eliminar una oferta', async () => {
    const checkboxes = component.findAll('tbody input[type="checkbox"]')
    await checkboxes[1].setValue(true)
    const deleteBtn = component.find('button[title="Eliminar oferta"]')
    await deleteBtn.trigger('click')

    expect(deleteOfferById).toHaveBeenCalledWith(2)
  })

})