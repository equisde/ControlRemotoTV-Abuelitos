# 🔧 Solución: La TV aparece en diferentes IPs

## 🤔 ¿Por qué la IP cambia?

Tu Android TV está obteniendo IPs diferentes del router cada vez. Esto es normal con DHCP.

---

## ✅ SOLUCIÓN 1: Asignar IP Fija en el Router (RECOMENDADO)

### Paso a Paso:

1. **Entra a la configuración de tu router**
   - Abre un navegador web
   - Escribe: `192.168.1.1` o `192.168.0.1`
   - Usuario/contraseña (común: admin/admin)

2. **Busca la sección de DHCP**
   - Puede llamarse: "DHCP", "LAN", "Red Local"

3. **Encuentra tu Android TV en la lista**
   - Busca el nombre de tu TV
   - O busca por la MAC address

4. **Asigna IP estática/reservada**
   - Opción: "IP Reservation", "Static IP", "IP Fija"
   - Asigna una IP como: `192.168.1.100`
   - Guarda cambios

5. **Reinicia la TV**
   - Ahora siempre tendrá la misma IP

---

## ✅ SOLUCIÓN 2: Usar la IP Actual (Temporal)

La app te muestra la IP encontrada. Simplemente:
1. Usa la IP que la app muestre
2. Si la TV no responde, busca de nuevo
3. La app encontrará la IP actual

---

## ✅ SOLUCIÓN 3: Configurar IP Fija en la TV

### Android TV:

1. **Configuración → Red**
2. **Selecciona tu WiFi → Avanzado**
3. **Cambiar de DHCP a IP Estática**
4. **Configura:**
   - IP: `192.168.1.100` (o la que prefieras)
   - Gateway: `192.168.1.1` (IP del router)
   - DNS: `8.8.8.8`
5. **Guardar**

---

## 🎯 Nueva Detección Mejorada

La app ahora usa **tabla ARP** y detecta por **MAC address**:

- ✅ Identifica fabricantes conocidos (Google, Xiaomi, NVIDIA, Amazon)
- ✅ Verifica puerto ADB 5555
- ✅ Más confiable que port scanning
- ✅ Encuentra la IP correcta aunque cambie

---

## 📝 Fabricantes Detectados

La app reconoce MACs de:
- Google (Chromecast, Google TV)
- Xiaomi (Mi Box)
- NVIDIA (Shield)
- Amazon (Fire TV)

---

## 💡 Recomendación

Para que NUNCA cambie la IP:
1. Configura IP fija en el router (SOLUCIÓN 1)
2. O configura IP estática en la TV (SOLUCIÓN 3)

Después de esto, la app SIEMPRE encontrará la misma IP.
