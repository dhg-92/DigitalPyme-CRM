<template>
  <div class="data-detail">
    <h2>Crear Oferta</h2>
    <form @submit.prevent="saveOffer()">
      <div class="form-columns">
        <div class="form-group">
          <label>Nombre</label>
          <input v-model="offer.name" type="text" required />
        </div>
        <div class="form-group">
          <label>Fecha</label>
          <input v-model="offer.date" type="date" required />
        </div>
        <div class="form-group">
          <label>Cliente</label>
          <select v-model="offer.clientId" required>
            <option value="">Seleccionar cliente</option>
            <option v-for="client in clients" :key="client.idClient" :value="client.idClient">
              {{ client.name }} - {{ client.company }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label>Estado</label>
          <input type="text" v-model="offer.status" disabled />
        </div>
      </div>
      <div class="form-actions">
        <button type="submit">Crear Oferta</button>
        <button @click="goToOffers()" type="button">Volver</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { createOffer, getAllClients } from '@/services/api'
import { alertStore } from '@/stores/alerts'

const router = useRouter()
const alert = alertStore()

const offer = ref({
  name: '',
  date: getDateNow(),
  clientId: '',
  status: 'Borrador'
})

const clients = ref([])

onMounted(async () => {
  try {
    clients.value = (await getAllClients()).data
  } catch (e) {
    console.error('Error cargando clientes:', e)
  }
})

function getDateNow() {
  const now = new Date()
  const yyyy = now.getFullYear()
  const mm = String(now.getMonth() + 1).padStart(2, '0')
  const dd = String(now.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

function goToOffers() {
  router.push('/offers')
}

async function saveOffer() {
  try {
    const newOfferId = (await createOffer(offer.value)).data
    alert.addAlert('Oferta creada correctamente.', 'success')
    router.push('/offers/'+newOfferId+'/products')
  } catch (e) {
    alert.addAlert('Error al crear la oferta.', 'error')
    console.error('Error al crear la oferta:', e)
  }
}
</script>

<style scoped>

</style>