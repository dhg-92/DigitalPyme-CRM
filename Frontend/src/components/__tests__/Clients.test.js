import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { createPinia } from 'pinia'
import ClientsComponent from '@/components/Clients.vue'
import { deleteClientById } from '@/services/api'

const push = vi.fn()

vi.mock('vue-router', () => ({
    useRouter: () => ({ push }),
}))

vi.mock('@/stores/alerts', () => ({
    alertStore: () => ({
        addAlert: vi.fn()
    })
}))

vi.mock('@/services/api', () => ({
    getAllClients: vi.fn(() => Promise.resolve({
        data: [
            {
                idClient: 1,
                name: "Client 1",
                surname: "Client 1",
                email: "diegohg8@email.com",
                company: "Company 1",
                phone: "+34111111111",
                address: "Dirección 1",
                city: "Madrid",
                zipCode: 28080,
                country: "España"
            },
            {
                idClient: 2,
                name: "Client 2",
                surname: "Client 2",
                email: "Client.2@email.com",
                company: "Company 2",
                phone: "+34222222222",
                address: "Dirección 2",
                city: "Madrid",
                zipCode: 28080,
                country: "España"
            }
        ]
    })),
    deleteClientById: vi.fn(() => Promise.resolve())
}))

describe('Clients.vue', () => {
    let component

    beforeEach(async () => {
        component = mount(ClientsComponent, {
            global: {
                plugins: [createPinia()]
            }
        })
        await new Promise(resolve => setTimeout(resolve, 0))
        await component.vm.$nextTick()
    })

    it('Los clientes se cargan bien', () => {
        const rows = component.findAll('tbody tr')
        expect(rows.length).toBe(2)
        expect(component.text()).toContain('Client 1')
        expect(component.text()).toContain('Client 2')
    })

    it('Permite seleccionar y eliminar un cliente', async () => {
        const checkboxes = component.findAll('tbody input[type="checkbox"]')
        await checkboxes[1].setValue(true)
        await component.find('button[title="Eliminar cliente"]').trigger('click')

        expect(deleteClientById).toHaveBeenCalledWith(2)
    })
})