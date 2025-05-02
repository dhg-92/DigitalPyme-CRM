<template>
  <div class="data-detail">
    <h1>{{ edit_page ? 'Editar Usuario' : 'Crear Usuario' }}</h1>
    <form @submit.prevent="saveUser()">
      <div class="form-columns">
        <div class="form-group">
          <label>Nombre</label>
          <input v-model="user.name" type="text" required />
        </div>
        <div class="form-group">
          <label>Apellido</label>
          <input v-model="user.surname" type="text" required />
        </div>
        <div class="form-group">
          <label>Email</label>
          <input v-model="user.email" type="email" required />
        </div>
        <div class="form-group">
          <label>Teléfono</label>
          <input v-model="user.phone" type="text" />
        </div>
        <div class="form-group">
          <label>¿Es administrador?</label>
          <input v-model="user.isAdmin" type="checkbox" />
        </div>
      </div>
      <div class="form-actions">
        <button type="submit">
          {{ edit_page ? 'Actualizar usuario' : 'Crear usuario' }}
        </button>
        <button @click="goToUsers()" type="button">Volver</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getUserById, updateUserById, createUser } from '@/services/api'
import { alertStore } from '@/stores/alerts'

const route = useRoute()
const router = useRouter()
const alert = alertStore()

const edit_page = computed(() => !!route.params.id)

const user = ref({
  name: '',
  surname: '',
  email: '',
  phone: '',
  isAdmin: false
})

onMounted(async () => {
  if (edit_page.value) {
    try {
      user.value = (await getUserById(route.params.id)).data
    } catch (e) {
      alert.addAlert('Error al cargar usuario.', 'error')
      console.error('Error al cargar usuario.', e)
    }
  }
})

function goToUsers() {
  router.push('/users')
}

async function saveUser() {
  try {
    if (edit_page.value) {
      await updateUserById(route.params.id, user.value)
      alert.addAlert('Usuario actualizado correctamente.', 'success')
    } else {
      await createUser(user.value)
      alert.addAlert('Usuario creado correctamente.', 'success')
      goToUsers()
    }
  } catch (e) {
    alert.addAlert('Error guardando el usuario.', 'error')
    console.error('Error guardando el usuario:', e)
  }
}
</script>

<style scoped></style>