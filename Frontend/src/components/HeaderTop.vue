<template>
  <header class="top-header">
    <div class="app-name">
      <router-link to="/">Digital Pyme CRM</router-link>
    </div>
    <nav class="top-nav">
      <ul>
        <li><router-link to="/logout">Salir</router-link></li>
      </ul>
    </nav>
  </header>
</template>

<script setup>
</script>

<style scoped>
.top-header {
  background-color: #ffffff;
  color: rgb(0, 0, 0);
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: #000000 1px solid;
}

.top-header .app-name a {
  color: rgb(0, 0, 0);
  text-decoration: none;
  font-size: 24px;
  font-weight: bold;
}

.top-header .top-nav ul {
  list-style: none;
  display: flex;
}

.top-header .top-nav li {
  margin-left: 20px;
}

.top-header .top-nav a {
  color: rgb(0, 0, 0);
  text-decoration: none;
}

.top-header .top-nav a:hover {
  text-decoration: underline;
}
</style>