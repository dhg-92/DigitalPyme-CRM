<template>
    <div class="data-detail">
        <h1>{{ edit_page ? 'Editar Producto' : 'Crear Producto' }}</h1>
        <form @submit.prevent="saveProduct()">
            <div class="form-columns">
                <div class="form-group">
                    <label>Nombre</label>
                    <input v-model="product.name" type="text" required />
                </div>
                <div class="form-group">
                    <label>Descripción</label>
                    <input v-model="product.description" type="text" required />
                </div>
                <div class="form-group">
                    <label>Precio</label>
                    <input v-model="product.defaultPrice" type="number" required />
                </div>
                <div class="form-group">
                    <label>Impuesto</label>
                    <input v-model="product.defaultTax" type="number" required />
                </div>
                <div class="form-group">
                    <label>Marca</label>
                    <input v-model="product.brand" type="text" />
                </div>
                <div class="form-group">
                    <label>Modelo</label>
                    <input v-model="product.model" type="text" />
                </div>
                <div class="form-group">
                    <label>Categoría</label>
                    <select v-model="product.categoryId" required>
                        <option value="" disabled>Selecciona una categoría</option>
                        <option v-for="category in categoryList" :key="category.idCategory"
                            :value="category.idCategory">
                            {{ category.name }}
                        </option>
                    </select>
                </div>
            </div>
            <div class="form-actions">
                <button type="submit">
                    {{ edit_page ? 'Actualizar Producto' : 'Crear Producto' }}
                </button>
                <button @click="goToProducts()" type="button">Volver</button>
            </div>
        </form>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getProductById, updateProductById, createProduct, getAllCategories } from '@/services/api'
import { alertStore } from '@/stores/alerts'

const route = useRoute()
const router = useRouter()
const alert = alertStore()

const edit_page = computed(() => !!route.params.id)

const product = ref({
    name: '',
    description: '',
    defaultPrice: '',
    defaultTax: '',
    brand: '',
    model: '',
    categoryId: ''
})

const categoryList = ref([])

onMounted(async () => {
    try {
        categoryList.value = (await getAllCategories()).data

        if (edit_page.value) {
            const product_data = (await getProductById(route.params.id)).data

            product.value = {
                name: product_data.name,
                description: product_data.description,
                defaultPrice: product_data.defaultPrice,
                defaultTax: product_data.defaultTax,
                brand: product_data.brand,
                model: product_data.model,
                categoryId: product_data.category?.idCategory ?? null
            }
        }
    } catch (e) {
        alert.addAlert('Error al cargar el producto.', 'error')
        console.error('Error al cargar el producto:', e)
    }
})

function goToProducts() {
    router.push('/products')
}

async function saveProduct() {
    try {
        if (edit_page.value) {
            await updateProductById(route.params.id, product.value)
            alert.addAlert('Producto actualizado correctamente.', 'success')
        } else {
            await createProduct(product.value)
            alert.addAlert('Producto creado correctamente.', 'success')
            goToProducts()
        }
    } catch (e) {
        alert.addAlert('Error guardando el producto.', 'error')
        console.error('Error guardando el producto.', e)
    }
}
</script>

<style scoped></style>