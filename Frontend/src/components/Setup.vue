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
          <div v-if="showMfaSetup">
            <h3>Escanea el código para habilitar la autenticación en dos pasos</h3>

            <img :src="qrCode" alt="QR Code" />

            <button @click="showVerifyInput = true">Verificar</button>

            <div v-if="showVerifyInput" class="verify-box">
              <input
                v-model="mfaCode"
                type="text"
                maxlength="6"
                inputmode="numeric"
                pattern="[0-9]*"
              />

              <button @click="verifyMFA">Confirmar código</button>
            </div>
          </div>
        </div>
        <div id="footer">
          <span id="copyright">© 2025 Digital Pyme CRM</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import { setupMFA, confirmMFA } from "@/services/api";
import { profileUser } from "@/stores/users";
import { alertStore } from "@/stores/alerts";
import { onMounted } from "vue";
import QRCode from "qrcode";

onMounted(() => {
  setupMFAData();
});

const router = useRouter();
const route = useRoute();
const alert = alertStore();

const mfaSecret = ref("");
const qrCode = ref("");
const showMfaSetup = ref(false);
const showVerifyInput = ref(false);
const mfaCode = ref("");

const setupMFAData = async () => {
  try {
    const response = await setupMFA();

    const data = response.data;
    mfaSecret.value = data.secret;
    
    const user = profileUser();

    console.log(user.email);

    const otpauth = `otpauth://totp/DigitalPymeCRM:${user.email}?secret=${data.secret}&issuer=DigitalPymeCRM`;

    qrCode.value = await QRCode.toDataURL(otpauth);

    showMfaSetup.value = true;
  } catch (e) {
    if (e.response?.status === 400 && e.response?.data === "MFA already enabled") {
      alert.addAlert("MFA ya registrado", "error");
    } else {
      alert.addAlert("Código incorrecto", "error");
    }
    router.push("/");
    console.error(e);
  }
};

const verifyMFA = async () => {
  try {
    if (!mfaCode.value) {
      alert.addAlert("Introduce el código MFA", "error");
      return;
    }

    await confirmMFA({
      mfaCode: mfaCode.value,
    });

    alert.addAlert("MFA activado correctamente", "success");

    router.push("/");
  } catch (e) {
    alert.addAlert("Código incorrecto", "error");
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
