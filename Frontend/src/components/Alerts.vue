<template>
  <div class="alert-container">
    <div v-for="(alert, id) in alerts" :key="id" :class="['alert', alert.type]" :style="{ time: `${alert.time }` }">
      {{ alert.msg }}
    </div>
  </div>
</template>

<script setup>
import { alertStore } from '@/stores/alerts';

const alerts = alertStore().alerts;
</script>

<style scoped>
.alert-container {
  position: fixed;
  bottom: 30px;
  right: 20px;
  width: 400px;
  z-index: 9999;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.alert {
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 5px;
  width: 100%;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  opacity: 0;
  animation: fadeInOut 5s ease-in-out forwards;
}

.alert.success {
  background-color: #d4edda;
  color: #155724;
}

.alert.error {
  background-color: #f8d7da;
  color: #721c24;
}

.alert.warning {
  background-color: #fff3cd;
  color: #856404;
}

.alert.info {
  background-color: #d1ecf1;
  color: #0c5460;
}

@keyframes fadeInOut {
  0% {
    opacity: 0;
  }

  20% {
    opacity: 1;
  }

  80% {
    opacity: 1;
  }

  100% {
    opacity: 0;
  }
}
</style>