<script setup>

import View from './views/View.vue';
import { profileUser } from '@/stores/users'

const userStore = profileUser()
userStore.initUserFromStorage()
</script>

<template>
  <View />
</template>

<style scoped></style>
