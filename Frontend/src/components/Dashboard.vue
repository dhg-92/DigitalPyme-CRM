<template>
  <div class="data-container">
    <h2>Dashboard</h2>
    <div class="dashboard-content" v-if="offersByStatus.length != 0">
      <div class="dashboard-row">
        <div class="dashboard-table">
          <h3>Ofertas por Estado</h3>
          <table>
            <thead>
              <tr>
                <th>Estado</th>
                <th>Cantidad</th>
                <th>Porcentaje</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in offersByStatus" :key="index">
                <td>{{ item.status }}</td>
                <td>{{ item.count }}</td>
                <td>{{ item.percentage.toFixed(2) }}%</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="dashboard-table" v-if="topClients.length != 0">
          <h3>Top 5 Clientes con más Ofertas Aceptadas</h3>
          <table>
            <thead>
              <tr>
                <th>Cliente</th>
                <th>Ofertas Aceptadas</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(client, index) in topClients" :key="index">
                <td>{{ client.name }}</td>
                <td>{{ client.acceptedOffers }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="dashboard-row" v-if="offersLatestAccepted.length != 0">
        <div class="dashboard-table">
          <h3>Últimas 5 Ofertas Aceptadas</h3>
          <table>
            <thead>
              <tr>
                <th>Oferta</th>
                <th>Cliente</th>
                <th>Importe</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="offer in offersLatestAccepted" :key="offer.idOffer" @click="goToOffer(offer.idOffer)"
                style="cursor: pointer;">
                <td>{{ offer.name }}</td>
                <td>{{ offer.client.name + " " + offer.client.surname + " - " + offer.client.company }}</td>
                <td>{{ offer.totalPrice }} €</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="dashboard-table" v-if="offersLatestRejected.length != 0">
          <h3>Últimas 5 Ofertas Rechazadas</h3>
          <table>
            <thead>
              <tr>
                <th>Oferta</th>
                <th>Cliente</th>
                <th>Importe</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="offer in offersLatestRejected" :key="offer.idOffer" @click="goToOffer(offer.idOffer)"
                style="cursor: pointer;">
                <td>{{ offer.name }}</td>
                <td>{{ offer.client.name + " " + offer.client.surname + " - " + offer.client.company }}</td>
                <td>{{ offer.totalPrice }} €</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="dashboard-row" v-if="offersLastweekPending.length != 0">
        <div class="dashboard-table">
          <h3>Ofertas pendientes del último mes</h3>
          <table>
            <thead>
              <tr>
                <th>Oferta</th>
                <th>Cliente</th>
                <th>Importe</th>
                <th>Fecha de Creación</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="offer in offersLastweekPending" :key="offer.idOffer" @click="goToOffer(offer.idOffer)"
                style="cursor: pointer;">
                <td>{{ offer.name }}</td>
                <td>{{ offer.client.name + " " + offer.client.surname + " - " + offer.client.company }}</td>
                <td>{{ offer.totalPrice }} €</td>
                <td>{{ new Date(offer.date) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { getAllOffers } from '@/services/api'
import { useRouter } from 'vue-router'

const offers = ref([])
const offersByStatus = ref([])
const topClients = ref([])
const offersLatestAccepted = ref([])
const offersLatestRejected = ref([])
const offersLastweekPending = ref([])

const router = useRouter()

const fetchOffers = async () => {
  offers.value = (await getAllOffers()).data

  offers_by_status()
  top_clients()
  latest_offers()
  pending_LastWeek_offers()
}

const offers_by_status = () => {
  const offersCount = {}

  for (let i = 0; i < offers.value.length; i++) {
    const offer = offers.value[i];
    if (offer && offer.status) {
      if (offersCount[offer.status]) {
        offersCount[offer.status] += 1;
      } else {
        offersCount[offer.status] = 1;
      }
    }
  }

  const totalOffers = offers.value.length

  offersByStatus.value = Object.entries(offersCount).map(([status, count]) => ({
    status,
    count,
    percentage: (count / totalOffers) * 100
  }))
}

const top_clients = () => {
  const clientCount = {}

  for (let i = 0; i < offers.value.length; i++) {
    const offer = offers.value[i];
    if (offer.status == 'Aceptada') {
      const clientName = `${offer.client.name} ${offer.client.surname} - ${offer.client.company}`;

      if (clientCount[clientName]) {
        clientCount[clientName] += 1;
      } else {
        clientCount[clientName] = 1;
      }
    }
  }

  const sortedClients = Object.entries(clientCount)
    .map(([name, acceptedOffers]) => ({ name, acceptedOffers }))
    .sort((a, b) => b.acceptedOffers - a.acceptedOffers)
    .slice(0, 5)

  topClients.value = sortedClients
}

const latest_offers = () => {
  const accepted = offers.value
    .filter(offer => offer.status == 'Aceptada')
    .sort((a, b) => new Date(b.date) - new Date(a.date))
    .slice(0, 5)

  const rejected = offers.value
    .filter(offer => offer.status == 'Rechazada')
    .sort((a, b) => new Date(b.date) - new Date(a.date))
    .slice(0, 5)

  offersLatestAccepted.value = accepted
  offersLatestRejected.value = rejected
}

const pending_LastWeek_offers = () => {
  const lastWeek = new Date()
  lastWeek.setDate(lastWeek.getDate() - 7)

  const pendingOffers = offers.value
    .filter(offer => offer.status == 'Pendiente de enviar' && new Date(offer.date) >= lastWeek)

  offersLastweekPending.value = pendingOffers
}

const goToOffer = (id) => {
  router.push('/offers/' + id + '/products')
}
fetchOffers()
</script>


<style scoped>
.dashboard-container {
  margin: 10px;
  padding: 0px 20px;
}

.title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 20px;
}

.dashboard-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.dashboard-row {
  display: flex;
  gap: 20PX;
}

.dashboard-table {
  flex: 1;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

thead {
  background-color: #f2f2f2;
}

h3 {
  margin-bottom: 10px;
}
</style>