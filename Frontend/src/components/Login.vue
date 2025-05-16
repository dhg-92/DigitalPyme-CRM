<template>
    <div id="login-container">
        <div id="content">
            <div id="background" class="fondo"></div>
        </div>
        <div class="auth">
            <div id="content">
                <div id="head">
                    <h1 class="title">Digital Pyme CRM </h1>
                </div>
                <div id="container">
                    <form v-if="loginForm && !showPasswordForm" @submit.prevent="loginUser()">
                        <div class="info">
                            Iniciar sesión
                        </div>
                        <input v-model="email" type="text" name="mail" placeholder="Introduce correo electrónico" required />
                        <input v-model="password" type="password" name="password" placeholder="Introduce contraseña" required
                            autocomplete="off" />
                        <span id="goToResetPass" @click="swapLoginForm()">
                            ¿Contraseña olvidada?
                        </span>
                        <br>
                        <button type="submit" class="button" name="login">Iniciar sesión</button>
                    </form>
                    <form v-if="!loginForm && !showPasswordForm" @submit.prevent="resetPasswordForm()">
                        <div class="info">
                            Inserte su correo a continuación para restablecer la contraseña:
                        </div>
                        <input v-model="email" type="email" name="resetPassInput" placeholder="Introduce correo electrónico" required />
                        <span id="goToLogin" @click="swapLoginForm()">
                            Volver
                        </span>
                        <br>
                        <button type="submit" name="resetPassButton">Solicitar restablecimiento</button>
                    </form>
                    <form v-if="showPasswordForm" @submit.prevent="setPasswordForm()">
                        <div class="info">Restablecer contraseña</div>
                        <input v-model="email" type="email" placeholder="Introduce tu correo" required />
                        <input v-model="newPassword" type="password" placeholder="Nueva contraseña" required />
                        <input v-model="confirmPassword" type="password" placeholder="Confirmar nueva contraseña" required />
                        <button type="submit" name="resetPassConfirmButton">Restablecer contraseña</button>
                    </form>
                </div>
                <div id="footer">
                    <span id="copyright">© 2025 Digital Pyme CRM</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { login, userInfo, setRestorePassword, setPassword } from '@/services/api'
import { profileUser } from '@/stores/users'
import { alertStore } from '@/stores/alerts'

const router = useRouter()
const route = useRoute()
const alert = alertStore()

const email = ref('')
const password = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const loginForm = ref(true)

const token = computed(() => route.params.token)
const showPasswordForm = computed(() => !!token.value)

const validEmail = (email) => {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    return re.test(email)
}

const swapLoginForm = () => {
    loginForm.value = !loginForm.value
}

const loginUser = () => {
    if (!email.value || !password.value || !validEmail(email.value)) {
        alert.addAlert('Por favor, proporciona datos de contacto válidos.', 'error')
        return
    }

    const userData = {
        email: email.value,
        password: password.value,
    }

    login(userData)
        .then(response => {
            localStorage.setItem('access_token', response.data.access_token)
            localStorage.setItem('expires_in', new Date().getTime() + response.data.expires_in * 1000)
            return userInfo()
        }).then(userInfo => {
            const userData = userInfo.data
            const saveUser = profileUser()

            saveUser.setUser({
                idUser: userData.idUser,
                email: userData.email,
                name: userData.name,
                surname: userData.surname,
                phone: userData.phone,
                isAdmin: userData.isAdmin,
                accessToken: localStorage.getItem('access_token'),
                expiresIn: localStorage.getItem('expires_in')
            })

            router.push('/')
        })
        .catch((e) => {
            alert.addAlert('Usuario o contraseña erróneos', 'error')
            console.error('Usuario o contraseña erróneos', e)
        })
}

const resetPasswordForm = () => {
    if (!email.value || !validEmail(email.value)) {
        alert.addAlert('Por favor, ingresa un correo electrónico válido.', 'error')
        return
    }

    setRestorePassword({ email: email.value })
        .then(() => {
            alert.addAlert('Se ha enviado un correo a la cuenta indicada. Para restablecer la contraseña, siga las instrucciones.', 'success')
            swapLoginForm()
        })
        .catch(() => {
            alert.addAlert('Error al solicitar el restablecimiento de contraseña.', 'error')
            console.error('Error al solicitar el restablecimiento de contraseña.', e)
        })
}

const setPasswordForm = () => {
    if (!email.value || !validEmail(email.value)) {
        alert.addAlert('Introduce un correo válido.', 'error')
        return
    }

    if (!newPassword.value || newPassword.value !== confirmPassword.value) {
        alert.addAlert('Las contraseñas no coinciden o están vacías.', 'error')
        return
    }

    const data = {
        email: email.value,
        password: newPassword.value,
        restoreToken: token.value
    }

    setPassword(data)
        .then(() => {
            alert.addAlert('Contraseña restablecida correctamente.', 'success')
            router.push('/login')
        })
        .catch(() => {
            alert.addAlert('Error al establecer la nueva contraseña.', 'error')
            console.error('Error al establecer la nueva contraseña.', e)
        })
}
</script>

<style scoped>
#login-container {
    display: flex;
    width: 100%;
    height: 100vh;
    margin: 0;
    padding: 0;
}

#content {
    display: flex;
    width: 100%;
    height: 100%;
    flex-direction: column;
    justify-content: center;
}

#background.fondo {
    width: 100%;
    height: 100%;
    background-image: url('../assets/img/fondo.jpg');
    background-size: cover;
    background-position: center;
    margin: 0;
}

.auth {
    width: 20%;
    background-color: rgba(255, 255, 255, 0.9);
    padding: 30px;
    box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    position: relative;
    margin: 0;
}

.logo {
    width: 100%;
    margin-bottom: 20px;
}

.info {
    padding-bottom: 10px;
}

input {
    padding: 10px;
    font-size: 16px;
    width: 90%;
    margin: 15px 0;
}

button {
    font-size: 16px;
    background-color: #000000;
    color: white;
    border: none;
    cursor: pointer;
    transition: background-color 0.3s ease;
    margin-top: 10px;
    margin-left: 0px;
}

button:hover {
    background-color: #696969;
}

#head {
    width: 100%;
    justify-content: center;
}

#footer {
    position: absolute;
    bottom: 10px;
    text-align: center;
    width: 80%;
}

#copyright {
    text-align: center;
}

.title {
    text-align: center;
    font-size: 3em;
}
</style>