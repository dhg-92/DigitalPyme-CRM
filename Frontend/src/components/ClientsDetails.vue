<template>
    <div class="data-detail">
        <h1>{{ edit_page ? 'Editar Cliente' : 'Crear Cliente' }}</h1>
        <form @submit.prevent="saveClient()">
            <div class="form-columns">
                <div class="form-group">
                    <label>Nombre</label>
                    <input v-model="client.name" type="text" required />
                </div>
                <div class="form-group">
                    <label>Apellido</label>
                    <input v-model="client.surname" type="text" required />
                </div>
                <div class="form-group">
                    <label>Email</label>
                    <input v-model="client.email" type="email" required />
                </div>
                <div class="form-group">
                    <label>Compañía</label>
                    <input v-model="client.company" type="text" />
                </div>
                <div class="form-group">
                    <label>Teléfono</label>
                    <input v-model="client.phone" type="text" />
                </div>
                <div class="form-group">
                    <label>Dirección</label>
                    <input v-model="client.address" type="text" />
                </div>
                <div class="form-group">
                    <label>Ciudad</label>
                    <input v-model="client.city" type="text" />
                </div>
                <div class="form-group">
                    <label>Código Postal</label>
                    <input v-model="client.zipCode" type="number" />
                </div>
                <div class="form-group">
                    <label>País</label>
                    <input v-model="client.country" type="text" />
                </div>
            </div>
            <div class="form-actions">
                <button type="submit">
                    {{ edit_page ? 'Actualizar datos' : 'Crear cliente' }}
                </button>
                <button @click="goToClient()">Volver</button>
            </div>
        </form>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getClientById, updateClientById, createClient } from '@/services/api'
import { alertStore } from '@/stores/alerts'

const route = useRoute()
const router = useRouter()
const alert = alertStore()

const edit_page = computed(() => !!route.params.id)

const client = ref({
    name: '',
    surname: '',
    email: '',
    company: '',
    phone: '',
    address: '',
    city: '',
    zipCode: '',
    country: ''
})

onMounted(async () => {
    if (edit_page.value) {
        try {
            const response = await getClientById(route.params.id)
            client.value = response.data
        } catch (error) {
            console.error('Error al cargar cliente:', error)
        }
    }
})

function goToClient() {
    router.push('/clients')
}

async function saveClient() {
    try {
        if (edit_page.value) {
            await updateClientById(route.params.id, client.value)
            alert.addAlert('Cliente actualizado correctamente.', 'success');
        } else {
            await createClient(client.value)
            alert.addAlert('Cliente creado correctamente.', 'success');
            router.push('/clients')
        }
    } catch (e) {
        alert.addAlert('Error guardando el cliente.', 'error');
        console.error('Error guardando el cliente:', e)
    }
}
</script>

<style scoped></style>