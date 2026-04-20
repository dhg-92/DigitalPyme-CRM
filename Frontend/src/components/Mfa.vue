<template>
  <div id="login-container">
    <div id="content">
      <div id="background" class="fondo"></div>
    </div>
    <div class="auth">
      <div id="content">
        <div id="head">
          <h1 class="title">Digital Pyme CRM</h1>
        </div>
        <div id="container">
          <div>
            <h3>Introduce el código doble factor para acceder al sistema</h3>

            <div class="verify-box">
              <input
                v-model="mfaCode"
                type="text"
                maxlength="6"
                inputmode="numeric"
                pattern="[0-9]*"
              />

              <button @click="verifyMFAData">Confirmar código</button>
            </div>
          </div>
        </div>
        <div id="footer">
          <span id="copyright">© {{ new Date().getFullYear() }} Digital Pyme CRM</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import { verifyMFA, userInfo } from "@/services/api";
import { profileUser } from "@/stores/users";
import { alertStore } from "@/stores/alerts";
import { onMounted } from "vue";


const router = useRouter();
const route = useRoute();
const alert = alertStore();

const mfaCode = ref("");

const verifyMFAData = async () => {
  try {
    if (!mfaCode.value || mfaCode.value.length !== 6) {
      alert.addAlert("Introduce un código válido de 6 dígitos", "error");
      return;
    }

    const response = await verifyMFA({
      mfaCode: mfaCode.value,
    });

    localStorage.setItem("access_token", response.data.access_token);
    localStorage.setItem(
      "expires_in",
      new Date().getTime() + response.data.expires_in * 1000
    );

    const userResponse = await userInfo();
    const userData = userResponse.data;

    const saveUser = profileUser();

    saveUser.setUser({
      idUser: userData.idUser,
      email: userData.email,
      name: userData.name,
      surname: userData.surname,
      phone: userData.phone,
      isAdmin: userData.isAdmin,
      accessToken: localStorage.getItem("access_token"),
      expiresIn: localStorage.getItem("expires_in"),
    });

    alert.addAlert("Acceso correcto", "success");

    router.push("/");

  } catch (e) {
    alert.addAlert("Código incorrecto", "error");
    console.error(e);
  }
};
</script>

<style scoped>
#container button {
  display: block;
  margin: 0 auto;
}

.verify-box {
  margin-top: 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.verify-box input {
  width: 150px;
  text-align: center;
  letter-spacing: 2px;
}

#login-container {
  display: flex;
  width: 100%;
  height: 100vh;
  margin: 0;
  padding: 0;
}

#container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 15px;
  text-align: center;
  width: 100%;
}

#container img {
  margin: 20px 0;
}

code {
  display: block;
  margin-top: 10px;
  word-break: break-all;
}

#content {
  display: flex;
  width: 100%;
  height: 100%;
  flex-direction: column;
  justify-content: center;
}

#background.fondo {
  width: 100%;
  height: 100%;
  background-image: url("../assets/img/fondo.jpg");
  background-size: cover;
  background-position: center;
  margin: 0;
}

.auth {
  width: 20%;
  background-color: rgba(255, 255, 255, 0.9);
  padding: 30px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  margin: 0;
}

.logo {
  width: 100%;
  margin-bottom: 20px;
}

.info {
  padding-bottom: 10px;
}

input {
  padding: 10px;
  font-size: 16px;
  width: 90%;
  margin: 15px 0;
}

button {
  font-size: 16px;
  background-color: #000000;
  color: white;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-top: 10px;
  padding: 10px 20px;
}

button:hover {
  background-color: #696969;
}

#head {
  width: 100%;
  justify-content: center;
}

#footer {
  position: absolute;
  bottom: 10px;
  text-align: center;
  width: 80%;
}

#copyright {
  text-align: center;
}

.title {
  text-align: center;
  font-size: 3em;
}
</style>
