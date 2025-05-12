import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { createPinia } from 'pinia'
import ProductsComponent from '@/components/Products.vue'
import { deleteProductById } from '@/services/api'

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
  getAllProducts: vi.fn(() => Promise.resolve({
      data: [
        {
          idProduct: 1,
          name: "Producto1",
          description: "ProductoDescripcion1",
          defaultPrice: 100.00,
          defaultTax: 7.00,
          brand: "HP",
          model: "G3",
          category: {
            idCategory: 1,
            name: "Electrónica",
            description: "Dispositivos electrónicos como laptops, smartphones y tablets",
            parentId: null
          }
        },
        {
          idProduct: 2,
          name: "Producto2",
          description: "ProductoDescripcion2",
          defaultPrice: 110.00,
          defaultTax: 7.50,
          brand: "HP",
          model: "G3",
          category: {
            idCategory: 1,
            name: "Electrónica",
            description: "Dispositivos electrónicos como laptops, smartphones y tablets",
            parentId: null
          }
        }
      ]
  })),
  deleteProductById: vi.fn(() => Promise.resolve())
}))

describe('Products.vue', () => {
  let component

  beforeEach(async () => {
    component = mount(ProductsComponent, {
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
    expect(component.text()).toContain('Producto1')
    expect(component.text()).toContain('Producto2')
  })

  it('Permite seleccionar y eliminar una oferta', async () => {
    const checkboxes = component.findAll('tbody input[type="checkbox"]')
    await checkboxes[1].setValue(true)
    const deleteBtn = component.find('button[title="Eliminar producto"]')
    await deleteBtn.trigger('click')

    expect(deleteProductById).toHaveBeenCalledWith(2)
  })

})