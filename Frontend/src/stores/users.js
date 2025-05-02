import { defineStore } from 'pinia'
import { ref } from 'vue'

export const profileUser = defineStore('user', () => {
  const idUser = ref(null)
  const email = ref('')
  const name = ref('')
  const surname = ref('')
  const phone = ref('')
  const isAdmin = ref(false)
  const isLoggedIn = ref(false)

  const accessToken = ref(null)
  const expiresIn = ref(null)

  function setUser(data) {
    idUser.value = data.idUser
    email.value = data.email
    name.value = data.name
    surname.value = data.surname
    phone.value = data.phone
    isAdmin.value = data.isAdmin
    isLoggedIn.value = true

    accessToken.value = data.accessToken
    expiresIn.value = data.expiresIn

    localStorage.setItem('access_token', data.accessToken)
    localStorage.setItem('expires_in', data.expiresIn)
    localStorage.setItem('idUser', data.idUser)
    localStorage.setItem('email', data.email)
    localStorage.setItem('name', data.name)
    localStorage.setItem('surname', data.surname)
    localStorage.setItem('phone', data.phone)
    localStorage.setItem('isAdmin', data.isAdmin)
    localStorage.setItem('isLoggedIn', 'true')
  }

  function resetUser() {
    localStorage.removeItem('access_token');
    localStorage.removeItem('expires_in');
    localStorage.removeItem('isLoggedIn');
    localStorage.removeItem('idUser');
    localStorage.removeItem('email');
    localStorage.removeItem('name');
    localStorage.removeItem('surname');
    localStorage.removeItem('phone');
    localStorage.removeItem('isAdmin');

    idUser.value = null;
    email.value = '';
    name.value = '';
    surname.value = '';
    phone.value = '';
    isAdmin.value = false;
    isLoggedIn.value = false;
    accessToken.value = null;
    expiresIn.value = null;

    localStorage.clear()
  }

  function initUserFromStorage() {
    const token = localStorage.getItem('access_token')
    const expires = localStorage.getItem('expires_in')

    const isStillValid = expires && new Date().getTime() < parseInt(expires)

    if (token && isStillValid) {
      accessToken.value = token
      expiresIn.value = expires
      isLoggedIn.value = true

      idUser.value = localStorage.getItem('idUser')
      email.value = localStorage.getItem('email')
      name.value = localStorage.getItem('name')
      surname.value = localStorage.getItem('surname')
      phone.value = localStorage.getItem('phone')
      isAdmin.value = localStorage.getItem('isAdmin') === 'true'
    } else {
      resetUser()
    }
  }

  return {
    idUser,
    email,
    name,
    surname,
    phone,
    isAdmin,
    isLoggedIn,
    accessToken,
    expiresIn,
    initUserFromStorage,
    setUser,
    resetUser
  }
})