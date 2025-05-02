<template>
  <div class="sidebar">
    <nav class="sidebar-menu">
      <ul>
        <li>
          <router-link to="/offers">
            Ofertas
          </router-link>
        </li>
        <li>
          <router-link to="/clients">
            Clientes
          </router-link>
        </li>
        <li>
          <router-link to="/products">
            Productos
          </router-link>
        </li>
        <li>
          <router-link to="/categories" v-if="profileUserData.isAdmin">
            Categorías
          </router-link>
        </li>
        <li>
          <router-link to="/users" v-if="profileUserData.isAdmin">
            Usuarios
          </router-link>
        </li>
        <li>
          <router-link to="/config" v-if="profileUserData.isAdmin">
            Configuración
          </router-link>
        </li>
      </ul>
    </nav>
  </div>
</template>1

<script setup>
import { profileUser } from '@/stores/users'

const profileUserData = profileUser()
</script>

<style scoped>
.sidebar {
  width: 150px;
  background-color: #46474a;
  color: white;
  height: 100%;
}

.sidebar-menu ul {
  list-style: none;
  padding: 0;
}

.sidebar-menu ul li a {
  color: white;
  text-decoration: none;
  display: flex;
  align-items: center;
  padding: 10px 15px;
  transition: background-color 0.3s;
}

.sidebar-menu ul li a:hover {
  background-color: #696969;
}
</style>