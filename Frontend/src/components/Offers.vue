<template>
  <div class="data-container">
    <h2 style="padding: 0 20px;">Ofertas</h2>
    <div class="data-content">
      <div class="top-bar">
        <div class="header-actions">
          <button @click="createOffer()" title="Crear oferta">
            <i class="fas fa-plus"></i>
          </button>
          <button @click="goToOffer()" :disabled="selectedOffers.length != 1" title="Editar oferta">
            <i class="fas fa-edit"></i>
          </button>
          <button @click="sendOffer()" :disabled="selectedOffers.length != 1" title="Enviar oferta">
            <i class="fas fa-envelope"></i>
          </button>
          <button @click="printOffer()" :disabled="selectedOffers.length != 1" title="Imprimir oferta">
            <i class="fas fa-print"></i>
          </button>
          <button @click="deleteOffers()" :disabled="selectedOffers.length == 0" title="Eliminar oferta">
            <i class="fas fa-trash"></i>
          </button>
        </div>
        <div class="filter-bar">
          <i class="fas fa-filter"></i>
          <select v-model="selectedStatus" class="filter-input">
            <option value="">Filtrar por estado...</option>
            <option value="Borrador">Borrador</option>
            <option value="Pendiente de enviar">Pendiente de enviar</option>
            <option value="Enviada">Enviada</option>
            <option value="Aceptada">Aceptada</option>
            <option value="Rechazada">Rechazada</option>
          </select>
        </div>
      </div>
      <div v-if="loading">Cargando ofertas...</div>
      <table v-else>
        <thead>
          <tr>
            <th>
              <input type="checkbox" @change="selectAllOffers()" :checked="allValuesSelected" />
            </th>
            <th>Nombre</th>
            <th>Fecha</th>
            <th colspan="2">Cliente</th>
            <th>Empresa</th>
            <th>Estado</th>
            <th>Total</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="offer in offersFound" :key="offer.idOffer" class="clickable-row"
            @click="rowClicked(offer.idOffer)">
            <td>
              <input type="checkbox" :value="offer.idOffer" v-model="selectedOffers" @click.stop />
            </td>
            <td>{{ offer.name }}</td>
            <td>{{ formatDate(offer.date) }}</td>
            <td>{{ offer.client.name + " " + offer.client.surname }}</td>
            <td>{{ offer.client.email }}</td>
            <td>{{ offer.client.company }}</td>
            <td>{{ offer.status }}</td>
            <td>{{ offer.totalPrice }} €</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getAllOffers, deleteOfferById, printOfferById, sendOfferApi } from '@/services/api'
import { useRouter } from 'vue-router'
import { alertStore } from '@/stores/alerts'

const router = useRouter()
const alert = alertStore()
const offerList = ref([])
const loading = ref(true)
const selectedOffers = ref([])
const selectedStatus = ref('')

onMounted(async () => {
  try {
    offerList.value = (await getAllOffers()).data
    loading.value = false
  } catch (e) {
    console.error('Error al cargar ofertas:', e)
  }
})

const offersFound = computed(() => {
  let filterSelect = offerList.value
  if (selectedStatus.value) {
    filterSelect = filterSelect.filter(offer => offer.status == selectedStatus.value)
  }
  return filterSelect
})

function createOffer() {
  router.push('/offers/create')
}

async function deleteOffers() {
  if (selectedOffers.value.length == 0) {
    alert.addAlert('Seleccionar al menos una oferta para eliminar.', 'info')
    return
  }

  for (let i = 0; i < selectedOffers.value.length; i++) {
    const offerId = selectedOffers.value[i]
    try {
      await deleteOfferById(offerId)
      alert.addAlert('Oferta eliminada correctamente.', 'success')
    } catch (e) {
      alert.addAlert('La oferta seleccionada no se puede eliminar.', 'error')
      console.error('La oferta seleccionada no se puede eliminar:', e)
    }
  }
  offerList.value = (await getAllOffers()).data
  selectedOffers.value = []
}

function goToOffer() {
  if (selectedOffers.value.length == 1) {
    router.push('/offers/' + selectedOffers.value[0] + '/products')
  } else {
    alert.addAlert('Seleccione una sola oferta para editar.', 'info')
  }
}

function formatDate(date) {
  const now = new Date(date);
  const dd = now.getDate().toString().padStart(2, '0');
  const mm = (now.getMonth() + 1).toString().padStart(2, '0');
  const yyyy = now.getFullYear();
  return `${dd}/${mm}/${yyyy}`;
}

const allValuesSelected = computed(() => {
  if (offerList.value.length > 0) {
    if (selectedOffers.value.length == offerList.value.length) {
      return true;
    }
  }
  return false;
})

function selectAllOffers() {
  if (!allValuesSelected.value) {
    selectedOffers.value = [];
    for (let offer of offerList.value) {
      selectedOffers.value.push(offer.idOffer);
    }
  } else {
    selectedOffers.value = []
  }
}

function printOffer() {
  if (selectedOffers.value.length === 1) {
    const offerId = selectedOffers.value[0];

    printOfferById(offerId).then((response) => {
      const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }))

      const link = document.createElement("a")
      link.href = url
      link.download = 'Oferta_' + offerId + '.pdf'

      link.click();

      window.URL.revokeObjectURL(url);
    }).catch(e => {
      alert.addAlert('Error al imprimir la oferta.', 'error');
      console.error('Error al imprimir la oferta:', e);
    });
  } else {
    alert.addAlert('Seleccione una sola oferta para imprimir.', 'info');
  }
}

function sendOffer() {
  if (selectedOffers.value.length == 1) {
    sendOfferApi(selectedOffers.value[0]).then(
      alert.addAlert('Oferta tramitada para el envio.', 'success')
    ).catch(e => {
      console.error('Error al generar el PDF:', e);
      alert.addAlert('Error al generar el PDF.', 'error');
    });
  } else {
    alert.addAlert('Seleccione una sola oferta para generar el PDF.', 'info');
  }
}

function rowClicked(id) {
  const i = selectedOffers.value.indexOf(id)

  if (i > -1) {
    selectedOffers.value.splice(i, 1)
  } else {
    selectedOffers.value.push(id)
  }
}

</script>

<style scoped></style>