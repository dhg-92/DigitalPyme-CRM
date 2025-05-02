<template>
    <div class="data-detail">
        <h1>{{ edit_page ? 'Editar Categoría' : 'Crear Categoría' }}</h1>
        <form @submit.prevent="saveCategory()">
            <div class="form-columns">
                <div class="form-group">
                    <label>Nombre</label>
                    <input v-model="category.name" type="text" required />
                </div>
                <div class="form-group">
                    <label>Descripción</label>
                    <textarea v-model="category.description" ></textarea>
                </div>
                <div class="form-group full-width">
                    <label>(Opcional) Ser subcategoría de:</label>
                    <select v-model="category.parentId">
                        <option :value="null">No ser subcategoría.</option>
                        <option v-for="cat in parentCategories" :key="cat.idCategory" :value="cat.idCategory">
                            {{ cat.name }}
                        </option>
                    </select>
                </div>
            </div>
            <div class="form-actions">
                <button type="submit">
                    {{ edit_page ? 'Actualizar Categoría' : 'Crear Categoría' }}
                </button>
                <button @click="goToCategories()" type="button">Volver</button>
            </div>
        </form>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCategoryById, updateCategoryById, createCategory, getAllCategories } from '@/services/api'
import { alertStore } from '@/stores/alerts'

const router = useRouter()
const route = useRoute()
const alert = alertStore()

const edit_page = computed(() => !!route.params.id)

const category = ref({
    name: '',
    description: '',
    parentId: null
})

const parentCategories = ref([])

onMounted(async () => {
    try {
        parentCategories.value = (await getAllCategories()).data.filter(category =>
            category.idCategory != route.params.id && category.parentId == null
        )
    } catch (e) {
        console.error('Error cargando categorías:', e)
    }

    if (edit_page.value) {
        try {
            category.value = (await getCategoryById(route.params.id)).data
        } catch (e) {
            alert.addAlert('Error al cargar categoría.', 'error');
            console.error('Error al cargar categoría:', e)
        }
    }
})

function goToCategories() {
    router.push('/categories')
}

async function saveCategory() {
    try {
        if (edit_page.value) {
            await updateCategoryById(route.params.id, category.value)
            alert.addAlert('Categoría actualizada correctamente.', 'success')
        } else {
            await createCategory(category.value)
            alert.addAlert('Categoría creada correctamente.', 'success')
            router.push('/categories')
        }
    } catch (e) {
        alert.addAlert('Error guardando la categoría.', 'error')
        console.error('Error guardando la categoría:', e)
    }
}
</script>

<style scoped>
.full-width {
    grid-column: span 2;
}
</style>