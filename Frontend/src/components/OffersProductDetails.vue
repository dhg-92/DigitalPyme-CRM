<template>
  <div class="offer-view">
    <div class="title-bar">
      <h2>Detalle de la oferta</h2>
    </div>
    <div class="details-grid">
      <div class="detail-item two-column">
        <div class="form-section">
          <div class="form-group">
            <label><strong>Nombre:</strong></label>
            <input type="text" v-model="offerList.name" />
            <label><strong>Estado:</strong></label>
            <select v-model="offerList.status">
              <option value="Borrador">Borrador</option>
              <option value="Pendiente de enviar">Pendiente de enviar</option>
              <option value="Enviada">Enviada</option>
              <option value="Aceptada">Aceptada</option>
              <option value="Rechazada">Rechazada</option>
            </select>
            <label><strong>Fecha oferta:</strong></label>
            <input type="date" v-model="offerList.date" />
          </div>
        </div>
        <div class="price-summary">
          <h3>Resumen</h3>
          <div class="price-line">
            <strong>Subtotal:</strong>
            <span>{{ offerList.subTotal }} €</span>
          </div>
          <div class="price-line">
            <strong>Impuestos:</strong>
            <span>{{ offerList.totalTax }} €</span>
          </div>
          <div class="price-line total">
            <strong>Total:</strong>
            <span>{{ offerList.totalPrice }} €</span>
          </div>
        </div>
      </div>
      <div class="detail-item">
        <div class="updateClient">
          <div class="client-icon" @click="showClients()" title="Editar cliente">
            <i class="fas fa-edit"></i><strong>Editar cliente</strong>
          </div>
          <div v-if="!showClientSelect" class="client-select">
            <p>Seleccionar un nuevo cliente</p>
            <select v-model="offerList.clientId" @change="updateClientDetails()">
              <option v-for="client in clientList" :key="client.idClient" :value="client.idClient">
                {{ client.name }} {{ client.surname }}
              </option>
            </select>
          </div>
        </div>
        <strong>Cliente:</strong><br>
        <p>{{ offerList.client?.name }} {{ offerList.client?.surname }}</p>
        <strong>Datos de contacto:</strong><br>
        <p>{{ offerList.client?.phone }} <br> {{ offerList.client?.email }}</p>
        <strong>Empresa:</strong><br>
        <p>{{ offerList.client?.company }}</p>
        <strong>Dirección:</strong><br>
        <p>{{ offerList.client?.address }}, {{ offerList.client?.zipCode }},
          {{ offerList.client?.city }}, {{ offerList.client?.country }}</p><br><br>
      </div>
    </div>
    <h3 class="productInfo">Productos</h3>
    <div class="top-bar">
      <div class="header-actions">
        <button @click="addProductToOffer()" title="Añadir producto">
          <i class="fas fa-plus"></i>
        </button>
        <button @click="deleteSelectedProducts()" :disabled="selectedProducts.length == 0" title="Eliminar producto">
          <i class="fas fa-trash"></i>
        </button>
      </div>
    </div>
    <table v-if="offerList.products?.length">
      <thead>
        <tr>
          <th><input type="checkbox" @change="selectAllProducts" :checked="allSelected" /></th>
          <th>Producto</th>
          <th>Descripción</th>
          <th>Cantidad</th>
          <th>Margen (%)</th>
          <th>Precio Unitario</th>
          <th>Impuestos (%)</th>
          <th>Total Producto</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(product, index) in offerList.products" :key="index">
          <td><input type="checkbox" :value="index" v-model="selectedProducts" /></td>
          <td>
            <select v-model="product.idProduct" @change="updateProductData(index)">
              <option v-for="p in productList" :key="p.idProduct" :value="p.idProduct">
                {{ p.name }} - {{ p.brand }} - {{ p.model }}
              </option>
            </select>
          </td>
          <td>{{ product.description }}</td>
          <td>
            <input type="number" v-model="product.quantity" @input="updatePrices(index)" min="1" />
          </td>
          <td>
            <input type="number" v-model="product.margin" @input="updatePrices(index)" min="0" /> %
          </td>
          <td>
            {{ product.unitPrice }} €
          </td>
          <td>
            <input type="number" v-model="product.defaultTax" @input="updatePrices(index)" min="0" /> %
          </td>
          <td>{{ product.totalPrice }} €</td>
        </tr>
      </tbody>
    </table>
    <div class="actions">
      <button @click="goToOffers()">Volver</button>
      <button @click="saveData()">Guardar productos</button>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getExtendOffer, getAllProducts, updateProductsByOfferId, updateOfferById, getAllClients } from '@/services/api'
import { alertStore } from '@/stores/alerts'

const route = useRoute()
const router = useRouter()
const alert = alertStore()

const offerList = ref({})
const selectedProducts = ref([])
const productList = ref([])
const clientList = ref([])
const showClientSelect = ref(true)

onMounted(async () => {
  clientList.value  = (await getAllClients()).data
  await getOffer()
  productList.value = (await getAllProducts()).data
})

function showClients() {
  showClientSelect.value = !showClientSelect.value
}

async function getOffer() {
  try {
    const dataExtendOffer = (await getExtendOffer(route.params.id)).data

    offerList.value = { ...dataExtendOffer, products: [] }

    offerList.value.products = dataExtendOffer.items.map(item => ({
      idProduct: item.idProduct,
      name: item.name,
      brand: item.brand,
      model: item.model,
      description: item.description,
      unitPrice: item.productPrice,
      basePrice: item.productPrice,
      margin: 0,
      quantity: item.quantity,
      defaultTax: item.tax,
      totalPrice: (item.quantity * (item.productPrice + item.productPrice * item.tax / 100)).toFixed(2)
    }))

    offerList.value.subTotal = parseFloat(dataExtendOffer.prices.totalBeforeTAX).toFixed(2)
    offerList.value.totalTax = parseFloat(dataExtendOffer.prices.totalWithTAX).toFixed(2)
    offerList.value.totalPrice = parseFloat(dataExtendOffer.prices.totalOffer).toFixed(2)
    offerList.value.date = new Date(dataExtendOffer.date).toISOString().split('T')[0]
    offerList.value.status = dataExtendOffer.status
    offerList.value.clientId = dataExtendOffer.clientId

    updateClientDetails()
  } catch (e) {
    alert.addAlert('Error al actualizar datos de la oferta.', 'error')
    console.error('Error al cargar datos:', e)
  }
}

function updateClientDetails() {
  const selectedClient = clientList.value.find(client => client.idClient == offerList.value.clientId)
  if (selectedClient) {
    offerList.value.client = selectedClient
  }
}

function goToOffers() {
  router.push('/offers')
}

const allSelected = computed(() => {
  return offerList.value.products?.length > 0 && selectedProducts.value.length == offerList.value.products.length
})

function selectAllProducts(event) {
  selectedProducts.value = event.target.checked ? offerList.value.products.map((_, i) => i) : []
}

function addProductToOffer() {
  if (offerList.value.products.length == 0) {
    offerList.value.products = []
  } else {
    offerList.value.products = offerList.value.products
  }
  offerList.value.products.push({
    idProduct: null,
    name: '',
    quantity: 1,
    unitPrice: 0,
    basePrice: 0,
    margin: 0,
    totalPrice: 0,
    defaultTax: 0,
    description: '',
    brand: '',
    model: ''
  })
}

function deleteSelectedProducts() {
  offerList.value.products = offerList.value.products.filter((_, i) => !selectedProducts.value.includes(i))
  selectedProducts.value = []
  updateOfferTotal()
}

function updateProductTotal(index) {
  const product = offerList.value.products[index]
  const unitPrice = parseFloat(product.unitPrice) || 0
  const quantity = parseInt(product.quantity) || 0
  const defaultTax = parseFloat(product.defaultTax) || 0
  const tax = (unitPrice * defaultTax) / 100
  product.totalPrice = (quantity * (unitPrice + tax)).toFixed(2)
}

function updateOfferTotal() {
  const subtotal = offerList.value.products.reduce((value, p) => value + p.unitPrice * p.quantity, 0)
  const totalTax = offerList.value.products.reduce((value, p) => value + p.unitPrice * p.quantity * p.defaultTax / 100, 0)
  const totalPrice = subtotal + totalTax

  offerList.value.subTotal = subtotal.toFixed(2)
  offerList.value.totalTax = totalTax.toFixed(2)
  offerList.value.totalPrice = totalPrice.toFixed(2)
}

function updatePrices(index) {
  const product = offerList.value.products[index]

  if (product.basePrice != null && product.margin != null) {
    product.unitPrice = (product.basePrice * (1 + product.margin / 100)).toFixed(2)
  }

  updateProductTotal(index)
  updateOfferTotal()
}

function updateProductData(index) {
  const product = offerList.value.products[index]
  const selected = productList.value.find(p => p.idProduct == product.idProduct)
  if (selected) {
    product.name = selected.name
    product.basePrice = selected.defaultPrice
    product.unitPrice = selected.defaultPrice
    product.margin = 0
    product.defaultTax = selected.defaultTax
    product.description = selected.description
    product.brand = selected.brand
    product.model = selected.model
    updatePrices(index)
  }
}

async function saveData() {
  try {
    const productosFormateados = offerList.value.products.map(p => ({
      productId: p.idProduct,
      productPrice: parseFloat(p.unitPrice),
      tax: parseFloat(p.defaultTax),
      quantity: parseInt(p.quantity)
    }))

    await updateProductsByOfferId(offerList.value.idOffer, productosFormateados)

    const offerDetails = {
      idOffer: offerList.value.idOffer,
      clientId: offerList.value.client.idClient,
      name: offerList.value.name,
      status: offerList.value.status,
      date: offerList.value.date,
      totalPrice: offerList.value.totalPrice
    }

    await updateOfferById(offerList.value.idOffer, offerDetails)
    await getOffer()
    alert.addAlert('Productos y detalles de la oferta actualizados correctamente.', 'success')
  } catch (e) {
    console.error('Error al guardar productos y detalles de la oferta:', e)
    alert.addAlert('Error al guardar productos y detalles de la oferta.', 'error')
  }
}
</script>

<style scoped>
@import "@/assets/offersDetails.css";
</style>