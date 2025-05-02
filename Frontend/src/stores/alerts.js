import { defineStore } from 'pinia';
import { ref } from 'vue';

export const alertStore = defineStore('alert', () => {
  const alerts = ref([]);

  const addAlert = (msg, type = 'success') => {
    alerts.value.push({ msg, type });

    setTimeout(() => {
      alerts.value.shift();
    }, 5000);
  };

  return { alerts, addAlert };
});