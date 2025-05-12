import { mount } from '@vue/test-utils';
import { vi, describe, it, expect } from 'vitest';
import CategoriesComponent from '@/components/Categories.vue';
import { createPinia } from 'pinia';

vi.mock('@/stores/alerts', () => ({
  alertStore: vi.fn(() => ({
    addAlert: vi.fn()
  }))
}))

vi.mock('vue-router', () => ({
  useRouter: () => ({
    push: vi.fn()
  }),
  useRoute: () => ({
    params: {}
  })
}))

vi.mock('@/services/api', () => ({
  getAllCategories: vi.fn(() => Promise.resolve({
    data: [
      {
        idCategory: 1,
        name: 'Electrónica',
        description: 'Dispositivos electrónicos como laptops, smartphones y tablets',
        parentId: null
      },
      {
        idCategory: 2,
        name: 'Equipamiento',
        description: 'Piezas',
        parentId: null
      },
      {
        idCategory: 3,
        name: 'Almacenamiento',
        description: 'Discos duros, usb...',
        parentId: 2
      },
      {
        idCategory: 4,
        name: 'Procesadores',
        description: 'Procesadores',
        parentId: 2
      }
    ]
  })),
  deleteCategoryById: vi.fn(() => Promise.resolve())
}))

describe('Categories.vue', () => {
  it('Las categorías se cargan bien', async () => {
    const component = mount(CategoriesComponent, {
      global: {
        plugins: [createPinia()]
      }
    })

    await new Promise(resolve => setTimeout(resolve, 0))
    await component.vm.$nextTick()

    const items = component.findAll('.category-list > div')
    expect(items).toHaveLength(4)

    expect(items[0].text()).toContain('Electrónica')
    expect(items[1].text()).toContain('Equipamiento')
    expect(items[2].text()).toContain('Almacenamiento')
    expect(items[3].text()).toContain('Procesadores')

    expect(items[0].classes()).toContain('category-item-0')
    expect(items[1].classes()).toContain('category-item-0')
    expect(items[2].classes()).toContain('category-item-1')
    expect(items[3].classes()).toContain('category-item-1')
  })


  it('Permite seleccionar y eliminar una categoría', async () => {
    const component = mount(CategoriesComponent, {
      global: {
        plugins: [createPinia()]
      }
    })

    await new Promise(resolve => setTimeout(resolve, 0))
    await component.vm.$nextTick()

    const checkboxes = component.findAll('input[type="checkbox"]')
    await checkboxes[1].setValue(true)
    await component.find('button[title="Eliminar categoría"]').trigger('click')
  })
})