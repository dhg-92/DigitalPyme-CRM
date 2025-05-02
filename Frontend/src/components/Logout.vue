<template>
</template>

<script setup>
import { profileUser } from '@/stores/users'
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const userStore = profileUser()

onMounted(() => {
  userStore.resetUser()
  localStorage.clear()
  router.push('/login')
})
</script>
