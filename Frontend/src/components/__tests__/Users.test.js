import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { createPinia } from 'pinia'
import UsersComponent from '@/components/Users.vue'
import { deleteUserById } from '@/services/api'

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
  getAllUsers: vi.fn(() => Promise.resolve({
    data: [
      {
        idUser: 1,
        name: "Admin",
        surname: "Admin",
        email: "admin@admin.com",
        phone: "666666666",
        isAdmin: true
      },
      {
        idUser: 2,
        name: "User",
        surname: "User",
        email: "user@user.com",
        phone: "666666666",
        isAdmin: false
      }
    ]
  })),
  deleteUserById: vi.fn(() => Promise.resolve())
}))

describe('Users.vue', () => {
  let component

  beforeEach(async () => {
    component = mount(UsersComponent, {
      global: {
        plugins: [createPinia()]
      }
    })

    await new Promise(resolve => setTimeout(resolve, 0))
    await component.vm.$nextTick()
  })

  it('Los usuario se cargan bien', () => {
    const rows = component.findAll('tbody tr')
    expect(rows.length).toBe(2)
    expect(component.text()).toContain('Admin')
    expect(component.text()).toContain('User')
  })

  it('Permite seleccionar y eliminar un usuario', async () => {
    const checkboxes = component.findAll('tbody input[type="checkbox"]')
    await checkboxes[1].setValue(true)
    const deleteBtn = component.find('button[title="Eliminar usuario"]')
    await deleteBtn.trigger('click')

    expect(deleteUserById).toHaveBeenCalledWith(2)
  })

})