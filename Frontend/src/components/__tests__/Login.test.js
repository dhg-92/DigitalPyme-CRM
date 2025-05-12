import { mount } from '@vue/test-utils';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import LoginComponent from '@/components/Login.vue';
import { createPinia, setActivePinia } from 'pinia';
import { alertStore } from '@/stores/alerts';

vi.mock('@/services/api', () => ({
  login: vi.fn(() => Promise.resolve({ access_token: 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaWVnb2hnOEBnbWFpbC5jb20iLCJpZCI6MSwibmFtZSI6IkFkbWluIiwic3VybmFtZSI6IkFkbWluIiwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzQ2OTgzNjIyLCJleHAiOjE3NDY5ODcyMjJ9.AG_MS1LtTlhXqp4GFUJaY0SAIkyTD1pUi4qmYiCEVXLCoxxnJT7Kb6ExT9vp0BIp0P42uHlo5kf_VMUvoGLpmQ', token_type: 'Bearer', expires_in: 3600 })),
}));

vi.mock('vue-router', () => ({
  useRouter: vi.fn(() => ({
    push: vi.fn(),
  })),
  useRoute: vi.fn(() => ({
    params: { token: '' },
  })),
}));

vi.mock('@/stores/alerts', () => ({
  alertStore: vi.fn(() => ({
    setError: vi.fn(),
    clear: vi.fn()
  }))
}));

describe('Login.vue', () => {
  beforeEach(() => {
    vi.spyOn(console, 'error').mockImplementation(() => { });
    setActivePinia(createPinia())
  });

  it('El formulario se carga bien', () => {
    const wrapper = mount(LoginComponent, {
      global: {
        plugins: [createPinia()]
      }
    })

    expect(wrapper.find('input[name="mail"]').exists()).toBe(true);
    expect(wrapper.find('input[name="password"]').exists()).toBe(true);
    expect(wrapper.find('button[name="login"').exists()).toBe(true);
    expect(wrapper.find('button[name="resetPassButton"').exists()).toBe(false);
    expect(wrapper.find('button[name="resetPassConfirmButton"').exists()).toBe(false);
  });

  it('Se introducen datos correctos', () => {
    const mockedAlertStore = {
      setError: vi.fn(),
      clear: vi.fn()
    };
    alertStore.mockReturnValue(mockedAlertStore);

    const wrapper = mount(LoginComponent, {
      global: {
        plugins: [createPinia()]
      }
    })

    const emailValue = wrapper.find('input[name="mail"]');
    const passwordValue = wrapper.find('input[name="password"]');

    emailValue.setValue('admin@admin.com');
    passwordValue.setValue('123456');

    expect(emailValue.element.value).toBe('admin@admin.com');
    expect(passwordValue.element.value).toBe('123456');

    expect(wrapper.find('input[name="mail"]').element.value).toBe('admin@admin.com');
    expect(wrapper.find('input[name="password"]').element.value).toBe('123456');
    expect(mockedAlertStore.setError).not.toHaveBeenCalled();
  });

  it('Se introducen datos incorrectos', () => {
    const mockedAlertStore = {
      setError: vi.fn(),
      clear: vi.fn()
    };
    alertStore.mockReturnValue(mockedAlertStore);

    const wrapper = mount(LoginComponent, {
      global: {
        plugins: [createPinia()]
      }
    })

    const emailValue = wrapper.find('input[name="mail"]');
    const passwordValue = wrapper.find('input[name="password"]');

    emailValue.setValue('admin');
    passwordValue.setValue('123456');

    expect(emailValue.element.value).toBe('admin');
    expect(passwordValue.element.value).toBe('123456');

    expect(wrapper.find('input[name="mail"]').element.value).toBe('admin');
    expect(wrapper.find('input[name="password"]').element.value).toBe('123456');
    expect(mockedAlertStore.setError).not.toHaveBeenCalled();
  });

  it('Restablecer contraseña"', async () => {
    const mockedAlertStore = {
      addAlert: vi.fn(),
      clear: vi.fn()
    };
    alertStore.mockReturnValue(mockedAlertStore);

    const wrapper = mount(LoginComponent, {
      global: {
        plugins: [createPinia()]
      }
    });

    expect(wrapper.find('form').exists()).toBe(true);
    expect(wrapper.find('input[name="mail"]').exists()).toBe(true);
    expect(wrapper.find('input[name="password"]').exists()).toBe(true);

    await wrapper.find('#goToResetPass').trigger('click');

    expect(wrapper.find('input[name="resetPassInput"]').exists()).toBe(true);
    expect(wrapper.find('input[name="password"]').exists()).toBe(false);
  });
});
