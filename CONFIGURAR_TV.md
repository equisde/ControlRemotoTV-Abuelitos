# 📺 Configuración de Android TV

## ⚠️ IMPORTANTE: Habilitar ADB en tu Android TV

Para que la app pueda conectarse a tu Android TV, **debes habilitar ADB** primero. Es muy fácil:

---

## 🔧 Pasos para Habilitar ADB en Android TV

### 1️⃣ Acceder a Configuración

- En tu Android TV, ve a **Configuración** (⚙️)
- Baja hasta **Sistema** o **Dispositivo**

### 2️⃣ Activar Opciones de Desarrollador

- Ve a **Acerca de** o **Información del dispositivo**
- Busca **Número de compilación** o **Build number**
- **Presiona 7 veces** sobre él
- Verás un mensaje: "Ahora eres un desarrollador"

### 3️⃣ Habilitar Depuración ADB

- Vuelve al menú anterior
- Verás una nueva opción: **Opciones de desarrollador**
- Entra en **Opciones de desarrollador**
- Activa estas opciones:
  - ✅ **Depuración USB** o **USB debugging**
  - ✅ **Depuración de red ADB** o **Network ADB debugging**

### 4️⃣ Conectar por Red (Opcional pero Recomendado)

Algunos modelos tienen la opción de conectar ADB por WiFi:

- Busca **Depuración inalámbrica** o **Wireless debugging**
- Actívala
- Aparecerá la IP y puerto de tu TV

---

## 📱 Marcas Específicas

### **Google Chromecast con Google TV:**
1. Configuración → Sistema → Acerca de
2. Presiona 7 veces en "Versión de Android TV OS"
3. Vuelve y entra en "Opciones de desarrollador"
4. Activa "Depuración USB"

### **Mi Box / Xiaomi:**
1. Configuración → Ajustes del dispositivo → Acerca de
2. Presiona 7 veces en "Compilación"
3. Opciones de desarrollador → Depuración USB

### **Fire TV Stick:**
1. Configuración → Mi Fire TV → Acerca de
2. Presiona 7 veces en la parte superior
3. Opciones de desarrollador → Depuración ADB

### **NVIDIA Shield:**
1. Configuración → Preferencias del dispositivo → Acerca de
2. Presiona 7 veces en "Compilación"
3. Opciones de desarrollador → Depuración de red

---

## ✅ Verificar que Funciona

Después de habilitar ADB:

1. **Asegúrate** de que la TV está encendida
2. **Conecta** tu teléfono y TV al **mismo WiFi**
3. **Abre** la app Control Remoto TV
4. **Presiona** "BUSCAR MI TV"
5. **Espera** unos segundos
6. **Debería encontrar** tu Android TV

---

## 🔍 ¿Por qué la app encontró mi PC?

Si la app encuentra tu PC en lugar de la TV, es porque:

- Tu PC tiene **ADB habilitado** (si usas Android Studio)
- Tu PC tiene algún **servidor web** en los puertos 8008 o 9000

**Solución:** 
- Habilita ADB en tu Android TV siguiendo los pasos de arriba
- La app ahora encontrará la TV primero

---

## 🆘 Solución de Problemas

### "No encuentra mi TV"

✅ **Verifica:**
1. La TV está **encendida**
2. TV y teléfono en el **mismo WiFi**
3. **ADB está habilitado** en la TV
4. La TV no está en modo de **ahorro de energía**

### "Encontró dispositivo equivocado"

✅ **Solución:**
1. Apaga temporalmente otros dispositivos Android en la red
2. Habilita ADB en la TV
3. La app priorizará dispositivos con ADB activo

### "Se conectó pero no responde"

✅ **Verifica:**
1. La TV tiene ADB habilitado
2. En la TV, aparece un mensaje pidiendo autorizar la conexión
3. Acepta la autorización

---

## 📝 Puertos que Usa la App

La app busca Android TV en estos puertos:

- **5555** - ADB (Android Debug Bridge) ⭐ Principal
- **8008** - Google Cast
- **9000** - Android TV Remote Service

Si tu TV tiene ADB habilitado en el puerto 5555, la app la encontrará sin problemas.

---

## 💡 Consejo para Abuelitos

**Si no sabes cómo hacer esto:**

1. Pide ayuda a un familiar joven
2. Solo hay que hacerlo **una vez**
3. Después la app funcionará siempre
4. Es seguro y no daña la TV

---

**¿Necesitas más ayuda?** La configuración solo toma 2-3 minutos y es muy sencilla.

---

## 🔍 ¿Por qué detecta 2 IPs para la misma TV?

Esto puede pasar por varias razones:

### 1️⃣ Tu TV tiene múltiples interfaces de red:
- Conexión WiFi (una IP)
- Conexión Ethernet (otra IP)

**Solución:** La app ahora detecta solo la PRIMERA TV encontrada y se detiene.

### 2️⃣ Dispositivos conectados a la TV:
- Un Chromecast conectado a la TV
- Un Android TV Box conectado a la TV
- Ambos responden en la red

**Solución:** Desconecta temporalmente otros dispositivos Android y busca de nuevo.

### 3️⃣ DHCP asignó múltiples IPs:
A veces el router asigna temporalmente más de una IP al mismo dispositivo.

**Solución:** 
1. Reinicia tu Android TV
2. Reinicia tu router WiFi
3. Busca de nuevo

---

## ✅ Cómo saber cuál IP es la correcta:

1. En tu Android TV, ve a: **Configuración → Red**
2. Mira la **dirección IP** mostrada
3. Usa esa IP en la app

O simplemente:
- **Prueba con la primera IP** que encuentre la app
- Si no funciona, prueba con la segunda

---
