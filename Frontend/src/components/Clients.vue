<template>
  <div class="data-container">
    <h2 style="padding: 0 20px;">Clientes</h2>
    <div class="data-content">
      <div class="top-bar">
        <div class="header-actions">
          <button @click="goToCreateClient()" title="Crear cliente">
            <i class="fas fa-plus"></i>
          </button>
          <button @click="goToClient()" :disabled="selectedClients.length != 1" title="Editar cliente">
            <i class="fas fa-edit"></i>
          </button>
          <button @click="deleteClients()" :disabled="selectedClients.length == 0" title="Eliminar cliente">
            <i class="fas fa-trash"></i>
          </button>
        </div>
        <div class="filter-bar">
          <i class="fas fa-filter"></i>
          <input v-model="searchWords" type="text" placeholder="Filtrar clientes..." class="filter-input" />
        </div>
      </div>
      <div v-if="msgInfo">Cargando clientes...</div>
      <table v-else>
        <thead>
          <tr>
            <th>
              <input type="checkbox" @change="selectAllClients()" :checked="allValuesSelected" />
            </th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Email</th>
            <th>Compañía</th>
            <th>Teléfono</th>
            <th>Dirección</th>
            <th>City</th>
            <th>Codigo postal</th>
            <th>Pais</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="client in clientsFound" :key="client.idClient" class="clickable-row"
            @click="rowClicked(client.idClient)">
            <td>
              <input type="checkbox" :value="client.idClient" v-model="selectedClients" @click.stop />
            </td>
            <td>{{ client.name }}</td>
            <td>{{ client.surname }}</td>
            <td>{{ client.email }}</td>
            <td>{{ client.company }}</td>
            <td>{{ client.phone }}</td>
            <td>{{ client.address }}</td>
            <td>{{ client.city }}</td>
            <td>{{ client.zipCode }}</td>
            <td>{{ client.country }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getAllClients, deleteClientById } from '@/services/api'
import { useRouter } from 'vue-router'
import { alertStore } from '@/stores/alerts'

const router = useRouter()
const alert = alertStore()
const msgInfo = ref(true)

const clientList = ref([])
const selectedClients = ref([])
const searchWords = ref('')

onMounted(async () => {
  try {
    clientList.value = (await getAllClients()).data
    msgInfo.value = false
  } catch (e) {
    console.error('Error al cargar clientes:', e)
  }
})

const clientsFound = computed(() => {
  if (!searchWords.value) {
    return clientList.value
  }

  const searchPattern = searchWords.value.toLowerCase()

  return clientList.value.filter(client =>
    Object.values(client).some(values =>
      String(values).toLowerCase().includes(searchPattern)
    )
  )
})

const allValuesSelected = computed(() => {
  if (clientList.value.length > 0) {
    if (selectedClients.value.length == clientList.value.length) {
      return true;
    }
  }
  return false;
})

function selectAllClients() {
  if (!allValuesSelected.value) {
    selectedClients.value = [];
    for (let client of clientList.value) {
      selectedClients.value.push(client.idClient);
    }
  } else {
    selectedClients.value = []
  }
}

function goToCreateClient() {
  router.push('/clients/create')
}

function goToClient() {
  if (selectedClients.value.length == 1) {
    router.push('/clients/' + selectedClients.value[0])
  } else {
    alert.addAlert('Seleccione un solo cliente para editar.', 'info')
  }
}

async function deleteClients() {
  if (selectedClients.value.length == 0) {
    alert.addAlert('Seleccionar al menos un cliente para eliminar.', 'info')
    return
  }
  for (let i = 0; i < selectedClients.value.length; i++) {
    try {
      await deleteClientById(selectedClients.value[i])
      alert.addAlert('Cliente eliminado correctamente.', 'success')
    } catch (e) {
      alert.addAlert('El cliente seleccionado no se puede eliminar.', 'error')
      console.error('Error al eliminar el cliente:', e)
    }
  }
  clientList.value = (await getAllClients()).data
  selectedClients.value = []
}

function rowClicked(id) {
  const i = selectedClients.value.indexOf(id)

  if (i > -1) {
    selectedClients.value.splice(i, 1)
  } else {
    selectedClients.value.push(id)
  }
}
</script>

<style scoped></style>