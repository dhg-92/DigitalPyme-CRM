<template>
  <div class="data-container">
    <h2 style="padding: 0 20px;">Categorías</h2>
    <div class="data-content">
      <div class="top-bar">
        <div class="header-actions">
          <button @click="goToCreateCategory()" title="Crear categoría">
            <i class="fas fa-plus"></i>
          </button>
          <button @click="goToEditCategory()" :disabled="selectedCategories.length != 1" title="Editar categoría">
            <i class="fas fa-edit"></i>
          </button>
          <button @click="deleteCategories()" :disabled="selectedCategories.length == 0" title="Eliminar categoría">
            <i class="fas fa-trash"></i>
          </button>
        </div>
      </div>
      <div v-if="msgInfo">Cargando categorías...</div>
      <div v-else class="category-list">
        <div v-for="category in orderCategory" :key="category.idCategory" :class="[`category-item-${category.level}`]"
           @click="rowClicked(category.idCategory)">
          <input type="checkbox" :value="category.idCategory" v-model="selectedCategories" @click.stop />
          <span class="category-name">{{ category.name }}</span>
          <span class="category-description">{{ category.description }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { alertStore } from '@/stores/alerts'
import { getAllCategories, deleteCategoryById } from '@/services/api'

const router = useRouter()
const alert = alertStore()
const msgInfo = ref(true)

const categoryList = ref([])
const selectedCategories = ref([])

onMounted(async () => {
  try {
    categoryList.value = (await getAllCategories()).data
    msgInfo.value = false
  } catch (e) {
    console.error('Error al cargar categorías:', e)
  }
})

function createTree(categories) {
  const CategoryValuesMap = new Map();

  for (let i = 0; i < categories.length; i++) {
    const category = categories[i];
    CategoryValuesMap.set(category.idCategory, { ...category, hijo: [] });

    if (category.parentId) {
      const parent = CategoryValuesMap.get(category.parentId);
      if (parent) {
        parent.hijo.push(CategoryValuesMap.get(category.idCategory));
      }
    }
  }

  const result = [];
  function treeRecursive(category, level = 0) {
    result.push({ ...category, level });
    for (let i = 0; i < category.hijo.length; i++) {
      treeRecursive(category.hijo[i], level + 1);
    }
  }

  for (const category of CategoryValuesMap.values()) {
    if (!category.parentId) {
      treeRecursive(category);
    }
  }

  return result;
}

const orderCategory = computed(() => createTree(categoryList.value))

function goToCreateCategory() {
  router.push('/categories/create')
}

function goToEditCategory() {
  if (selectedCategories.value.length == 1) {
    router.push(`/categories/${selectedCategories.value[0]}`)
  } else {
    alert.addAlert('Seleccione una sola categoría para editar.', 'info')
  }
}

async function deleteCategories() {
  if (selectedCategories.value.length == 0) {
    alert.addAlert('Selecciona al menos una categoría para eliminar.', 'info')
    return
  }

  for (let id of selectedCategories.value) {
    try {
      await deleteCategoryById(id)
      alert.addAlert('Categoría eliminada correctamente.', 'success')
    } catch (error) {
      alert.addAlert('No se pudo eliminar la categoría.', 'error')
    }
  }

  categoryList.value = (await getAllCategories()).data
  selectedCategories.value = []
}

function rowClicked(id) {
  const i = selectedCategories.value.indexOf(id)

  if (i > -1) {
    selectedCategories.value.splice(i, 1)
  } else {
    selectedCategories.value.push(id)
  }
}

</script>

<style scoped>
.category-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.category-item-0 {
  display: flex;
  align-items: center;
  gap: 10px;
  background-color: #f0f0f0;
  padding: 8px 12px;
  border-radius: 6px;
}

.category-item-1 {
  display: flex;
  align-items: center;
  gap: 10px;
  background-color: #f0f0f0;
  padding: 8px 12px;
  border-radius: 6px;
  margin-left: 50px;
}

.category-item-0 input {
  margin-top: 0px;
}

.category-item-1 input {
  margin-top: 0px;
}

.category-item:hover {
  background-color: #ececec;
}

.category-name {
  font-weight: bold;
}

.category-description {
  color: #666;
  font-size: 14px;
}
</style>