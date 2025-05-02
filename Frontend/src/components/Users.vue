<template>
  <div class="data-container">
    <h2 style="padding: 0 20px;">Usuarios</h2>
    <div class="data-content">
      <div class="top-bar">
        <div class="header-actions">
          <button @click="goToCreateUser()" title="Crear usuario">
            <i class="fas fa-plus"></i>
          </button>
          <button @click="goToUser()" :disabled="selectedUsers.length != 1" title="Editar usuario">
            <i class="fas fa-edit"></i>
          </button>
          <button @click="deleteUser()" :disabled="selectedUsers.length == 0" title="Eliminar usuario">
            <i class="fas fa-trash"></i>
          </button>
        </div>
        <div class="filter-bar">
          <i class="fas fa-filter"></i>
          <input v-model="searchWords" type="text" placeholder="Filtrar usuarios..." class="filter-input" />
        </div>
      </div>
      <div v-if="msgInfo">Cargando usuarios...</div>
      <table v-else>
        <thead>
          <tr>
            <th><input type="checkbox" @change="selectAllUsers()" :checked="allValuesSelected" /></th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Email</th>
            <th>Teléfono</th>
            <th>Administrador</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in usersFound" :key="user.idUser" class="clickable-row" @click="rowClicked(user.idUser)">
            <td><input type="checkbox" :value="user.idUser" v-model="selectedUsers" @click.stop /></td>
            <td>{{ user.name }}</td>
            <td>{{ user.surname }}</td>
            <td>{{ user.email }}</td>
            <td>{{ user.phone }}</td>
            <td>{{ user.isAdmin ? 'Sí' : 'No' }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getAllUsers, deleteUserById } from '@/services/api'
import { alertStore } from '@/stores/alerts'

const router = useRouter()
const alert = alertStore()
const msgInfo = ref(true)

const userList = ref([])
const selectedUsers = ref([])
const searchWords = ref('')

onMounted(async () => {
  try {
    userList.value = (await getAllUsers()).data
    msgInfo.value = false
  } catch (e) {
    console.error('Error al cargar usuarios:', e)
  }
})

const usersFound = computed(() => {
  if (!searchWords.value) {
    return userList.value
  }

  const searchPattern = searchWords.value.toLowerCase()

  return userList.value.filter(user =>
    Object.values(user).some(values =>
      String(values).toLowerCase().includes(searchPattern)
    )
  )
})

const allValuesSelected = computed(() => {
  if (userList.value.length > 0) {
    if (selectedUsers.value.length == userList.value.length) {
      return true;
    }
  }
  return false;
})

function selectAllUsers() {
  if (!allValuesSelected.value) {
    selectedUsers.value = [];
    for (let user of userList.value) {
      selectedUsers.value.push(user.idUser);
    }
  } else {
    selectedUsers.value = []
  }
}

function goToCreateUser() {
  router.push('/users/create')
}

function goToUser() {
  if (selectedUsers.value.length == 1) {
    router.push('/users/' + selectedUsers.value[0])
  } else {
    alert.addAlert('Seleccione un solo usuario para editar.', 'info')
  }
}

async function deleteUser() {
  if (selectedUsers.value.length == 0) {
    alert.addAlert('Selecciona al menos un usuario para eliminar.', 'info')
    return
  }

  for (let i = 0; i < selectedUsers.value.length; i++) {
    try {
      await deleteUserById(selectedUsers.value[i])
      alert.addAlert('Usuario eliminado correctamente.', 'success')
    } catch (e) {
      alert.addAlert('El usuario seleccionada no se puede eliminar.', 'error')
      console.error('Error al eliminar el usuario:', e)
    }
  }

  userList.value = (await getAllUsers()).data
  selectedUsers.value = []
}

function rowClicked(id) {
  const i = selectedUsers.value.indexOf(id)

  if (i > -1) {
    selectedUsers.value.splice(i, 1)
  } else {
    selectedUsers.value.push(id)
  }
}
</script>

<style scoped></style>