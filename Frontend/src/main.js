import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import { profileUser } from './stores/users'

const app = createApp(App)

const pinia = createPinia()
app.use(pinia)

app.use(router)

app.mount('#app')

const userStore = profileUser()
userStore.initUserFromStorage()

