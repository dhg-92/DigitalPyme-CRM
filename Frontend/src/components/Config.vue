<template>
  <div class="data-container">
    <h2>Notificaciones</h2>
    <button @click="showConfig()" class="show-config-btn">
      Configuración SMTP
    </button>
    <div v-if="isConfigVisible" class="smtp-config-form">
      <h3>Configuración SMTP</h3>
      <div class="smtp-form">
        <div class="column">
          <div class="form-group">
            <label for="host">Host:</label>
            <input type="text" id="host" v-model="smtpConfig.host" />
          </div>
          <div class="form-group">
            <label for="protocol">Protocolo:</label>
            <input type="text" id="protocol" v-model="smtpConfig.protocol" />
          </div>
          <div class="form-group">
            <label for="port">Puerto:</label>
            <input type="number" id="port" v-model="smtpConfig.port" />
          </div>
        </div>
        <div class="column">
          <div class="form-group">
            <label for="username">Usuario:</label>
            <input type="text" id="username" v-model="smtpConfig.username" />
          </div>
          <div class="form-group">
            <label for="password">Contraseña:</label>
            <input type="password" id="password" v-model="smtpConfig.password" />
          </div>
          <div class="checkbox-group">
            <div class="checkbox-item">
              <label for="useSSL">Usar SSL:</label>
              <input type="checkbox" id="useSSL" v-model="smtpConfig.useSSL" />
            </div>
            <div class="checkbox-item">
              <label for="auth">Autenticación requerida:</label>
              <input type="checkbox" id="auth" v-model="smtpConfig.auth" />
            </div>
          </div>
        </div>
        <button type="submit" class="save-btn-config" @click="saveConfig()">Guardar Configuración</button>
        <button class="test-btn" @click="sendTestMail()">Test Mail</button>
      </div>
    </div>
    <div v-if="msgInfo">Cargando notificaciones...</div>
    <table v-else>
      <thead>
        <tr>
          <th>ID</th>
          <th>Oferta ID</th>
          <th>Correo destinatario</th>
          <th>Asunto</th>
          <th>Estado</th>
          <th>Fecha de envío</th>
          <th>Mensaje de error</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="notification in notificationList" :key="notification.id">
          <td>{{ notification.id }}</td>
          <td>{{ notification.offerId }}</td>
          <td>{{ notification.emailTo }}</td>
          <td>{{ notification.subject }}</td>
          <td>{{ notification.status }}</td>
          <td>{{ formatDate(notification.dateShipment) }}</td>
          <td>{{ notification.errorMessage }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getNotifications, getConfig, updateConfig, testMail } from '@/services/api'
import { alertStore } from '@/stores/alerts'

const alert = alertStore()
const msgInfo = ref(true)

const notificationList = ref([])
const isConfigVisible = ref(false)
const smtpConfig = ref({
  host: '',
  protocol: '',
  port: 0,
  username: '',
  password: '',
  useSSL: false,
  auth: false
})

onMounted(async () => {
  try {
    reloadInfo()
    smtpConfig.value = (await getConfig()).data
    msgInfo.value = false
  } catch (e) {
    console.error('Error al cargar las notificaciones o la configuración:', e)
    alert.addAlert('Error al cargar las notificaciones o la configuración.', 'error')
  }
})

async function reloadInfo(){
  notificationList.value = (await getNotifications()).data
  notificationList.value.sort((a, b) => {
  const A = new Date(a.dateShipment)
  const B = new Date(b.dateShipment)
  return B - A
})
}

function showConfig() {
  isConfigVisible.value = !isConfigVisible.value
}

async function saveConfig() {
  try {
    await updateConfig(smtpConfig.value)
    alert.addAlert('Configuración SMTP guardada correctamente.', 'success')
  } catch (e) {
    alert.addAlert('Error al guardar la configuración SMTP.', 'error')
    console.error('Error al guardar la configuración SMTP:', e)
  }
}

function formatDate(date) {
  const d = new Date(date)
  const day = d.getDate().toString().padStart(2, '0')
  const month = (d.getMonth() + 1).toString().padStart(2, '0')
  const year = d.getFullYear()
  const hours = d.getHours().toString().padStart(2, '0')
  const minutes = d.getMinutes().toString().padStart(2, '0')
  const seconds = d.getSeconds().toString().padStart(2, '0')

  return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`
}

function sendTestMail() {
  testMail()
  alert.addAlert('Se enviará un mensaje de prueba.', 'info')
  setTimeout(() => {
    reloadInfo()
  }, 5000)
}

</script>

<style scoped>

.test-btn {
  display: flex;
  align-items: flex-end;
  flex-direction: column;
}

.smtp-config-form {
  margin-bottom: 20px;
  padding: 10px;
  background-color: #f4f4f4;
  border-radius: 8px;
}

.smtp-config-form h3 {
  margin-bottom: 15px;
}

.column {
  flex: 1 1 45%;
  min-width: 300px;
}

.smtp-form {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.save-btn-config {
  width: 20%;
  margin-left: auto;
  background-color: #000;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.save-btn-config:hover {
  background-color: #696969;
}

.show-config-btn {
  background-color: #000;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.show-config-btn:hover {
  background-color: #696969;
}

</style>