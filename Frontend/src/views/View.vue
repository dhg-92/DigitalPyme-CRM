<template>
  <div class="layout">
    <HeaderComponent v-if="!route.meta.hideHeader" />

    <div class="content">
      <Sidebar v-if="!route.meta.hideSidebar" />

      <main class="viewer">
        <router-view />
        <Alert />
      </main>
    </div>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import HeaderComponent from '../components/HeaderTop.vue'
import Sidebar from '../components/Sidebar.vue'
import Alert from '../components/Alerts.vue'

const route = useRoute()
</script>

<style scoped></style>