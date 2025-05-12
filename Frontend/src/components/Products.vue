<template>
  <div class="data-container">
    <h2 style="padding: 0 20px;">Productos</h2>
    <div class="data-content">
      <div class="top-bar">
        <div class="header-actions">
          <button @click="goToCreateProduct()" title="Crear producto">
            <i class="fas fa-plus"></i>
          </button>
          <button @click="goToProduct()" :disabled="selectedProducts.length != 1" title="Editar producto">
            <i class="fas fa-edit"></i>
          </button>
          <button @click="deleteProduct()" :disabled="selectedProducts.length == 0" title="Eliminar producto">
            <i class="fas fa-trash"></i>
          </button>
        </div>

        <div class="filter-bar">
          <i class="fas fa-filter"></i>
          <input v-model="searchWords" type="text" placeholder="Filtrar productos..." class="filter-input" />
        </div>
      </div>

      <div v-if="msgInfo">Cargando productos...</div>

      <table v-else>
        <thead>
          <tr>
            <th>
              <input type="checkbox" @change="selectAllProducts()" :checked="allValuesSelected" />
            </th>
            <th>Nombre</th>
            <th>Descripción</th>
            <th>Precio</th>
            <th>Impuesto</th>
            <th>Marca</th>
            <th>Modelo</th>
            <th>Categoría</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in filteredProducts" :key="product.idProduct" class="clickable-row"
            @click="rowClicked(product.idProduct)">
            <td>
              <input type="checkbox" :value="product.idProduct" v-model="selectedProducts" @click.stop />
            </td>
            <td>{{ product.name }}</td>
            <td>{{ product.description }}</td>
            <td>{{ product.defaultPrice }} €</td>
            <td>{{ product.defaultTax }} %</td>
            <td>{{ product.brand }}</td>
            <td>{{ product.model }}</td>
            <td>{{ product.category.name }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getAllProducts, deleteProductById } from '@/services/api'
import { useRouter } from 'vue-router'
import { alertStore } from '@/stores/alerts'

const router = useRouter()
const alert = alertStore()
const msgInfo = ref(true)

const productList = ref([])
const selectedProducts = ref([])
const searchWords = ref('')

onMounted(async () => {
  try {
    productList.value = (await getAllProducts()).data
    msgInfo.value = false
  } catch (e) {
    console.error('Error al cargar productos:', e)
  }
})

const filteredProducts = computed(() => {
  if (!searchWords.value) {
    return productList.value
  }

  const searchPattern = searchWords.value.toLowerCase()

  return productList.value.filter(product => {
    return (
      product.name?.toLowerCase().includes(searchPattern) ||
      product.description?.toLowerCase().includes(searchPattern) ||
      product.brand?.toLowerCase().includes(searchPattern) ||
      product.model?.toLowerCase().includes(searchPattern) ||
      product.category?.name?.toLowerCase().includes(searchPattern)
    )
  })
})

const allValuesSelected = computed(() => {
  if (productList.value && productList.value.length > 0) {
    if (selectedProducts.value.length == productList.value.length) {
      return true;
    }
  }
  return false;
})

function selectAllProducts() {
  if (!allValuesSelected.value) {
    selectedProducts.value = [];
    for (let product of productList.value) {
      selectedProducts.value.push(product.idProduct);
    }
  } else {
    selectedProducts.value = []
  }
}

function goToCreateProduct() {
  router.push('/products/create')
}

function goToProduct() {
  if (selectedProducts.value.length == 1) {
    router.push(`/products/${selectedProducts.value[0]}`)
  } else {
    alert.addAlert('Seleccione un solo producto para editar.', 'info')
  }
}

async function deleteProduct() {
  if (selectedProducts.value.length == 0) {
    alert.addAlert('Seleccionar al menos un producto para eliminar.', 'info')
    return
  }
  for (let i = 0; i < selectedProducts.value.length; i++) {
    try {
      await deleteProductById(selectedProducts.value[i])
      alert.addAlert('Producto eliminado correctamente.', 'success')
    } catch (e) {
      alert.addAlert('El producto seleccionado no se puede eliminar.', 'error')
      console.error('El producto seleccionado no se puede eliminar:', e)
    }
  }

  productList.value = (await getAllProducts()).data;
  selectedProducts.value = []
}

function rowClicked(id) {
  const i = selectedProducts.value.indexOf(id)
  if (i == -1) {
    selectedProducts.value.push(id)
  } else {
    selectedProducts.value.splice(i, 1)
  }
}
</script>

<style scoped></style>