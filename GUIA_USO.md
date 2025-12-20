# GUÍA PASO A PASO - CONTROL REMOTO PARA ABUELITOS

## 🎯 Para el Abuelito/Abuelita

### ¿Qué necesito?

1. Un teléfono Android
2. Una TV Android (Smart TV)
3. Conexión WiFi (ambos conectados al mismo WiFi)

### ¿Cómo uso la app?

#### PASO 1: Abrir la app
- Toca el ícono azul de la TV en tu teléfono
- Verás letras GRANDES que dicen "Control TV"

#### PASO 2: Buscar la TV
- Toca el botón azul que dice "BUSCAR MI TV"
- Espera unos segundos (verás una ruedita girando)
- La app te dirá cuando encuentre tu TV

#### PASO 3: Conectar
- Cuando veas "TV Encontrada"
- Toca el botón verde que dice "CONECTAR"
- ¡Listo! Verás el control remoto

#### PASO 4: Usar el control
Ahora puedes controlar tu TV:

- **Botón ROJO GRANDE** arriba = Encender o Apagar la TV
- **Botones VERDES** = Subir o Bajar el volumen
  - 🔊 + = Más fuerte
  - 🔉 - = Más bajo
  - 🔇 = Sin sonido
- **Botones NARANJAS** = Cambiar canales
  - 📺 ▲ = Canal siguiente
  - 📺 ▼ = Canal anterior
- **Botones de FLECHAS** = Moverse por los menús
  - ▲ = Arriba
  - ▼ = Abajo
  - ◀ = Izquierda
  - ▶ = Derecha
  - **OK verde** = Aceptar/Seleccionar
- **Otros botones**:
  - 🏠 INICIO = Ir a la pantalla principal
  - ◀ ATRÁS = Volver atrás
  - ☰ MENÚ = Abrir el menú

### 💡 Consejos Importantes

1. **Mantén la TV encendida** cuando busques conectar
2. **Los dos deben estar en el mismo WiFi** (TV y teléfono)
3. **Los botones hacen "pip"** cuando los tocas (feedback)
4. **Si no funciona**, cierra la app y ábrela de nuevo
5. **Toca UNA VEZ** cada botón, no lo mantengas presionado

### ❓ Problemas Comunes

**"No encuentra mi TV"**
- Asegúrate que la TV esté encendida
- Revisa que ambos estén en el mismo WiFi
- Intenta de nuevo tocando "BUSCAR MI TV"

**"Los botones no responden"**
- Cierra la app completamente
- Ábrela de nuevo
- Vuelve a buscar y conectar

**"Se desconectó"**
- Normal si apagas la TV
- Solo vuelve a abrir la app cuando enciendas la TV

---

## 🛠️ Para el Familiar/Técnico

### Instalación

1. **Compilar** (si es necesario):
   ```bash
   cd ControlRemotoTV
   ./compilar.sh
   ```

2. **Instalar en el teléfono**:
   - Transferir el APK via USB, Bluetooth o correo
   - En el teléfono: Configuración → Seguridad → Permitir instalación de apps desconocidas
   - Abrir el APK y seguir las instrucciones

3. **Configurar la TV Android**:
   - Debe tener ADB habilitado (Puerto 5555) O
   - Alguna app de servidor remoto instalada

### Características Técnicas

- **Min SDK**: Android 5.0 (API 21)
- **Target SDK**: Android 14 (API 34)
- **Protocolo**: HTTP sobre red local
- **Puertos**: 5555 (ADB), 8080, 9090 (alternativos)
- **Descubrimiento**: Escaneo de red local

### Personalización

Para cambiar el tamaño de los botones o texto, editar:
- `app/src/main/res/values/styles.xml`

Tamaños actuales:
- Texto botones: 20-28sp
- Altura botones: 80-90dp
- Márgenes: 8dp

### Solución de Problemas

1. **TV no responde a comandos**:
   - Verificar que ADB esté habilitado en la TV
   - Puerto 5555 abierto
   - Firewall no bloqueando

2. **No encuentra la TV**:
   - Ambos en misma red
   - Red no aislada (algunos routers aíslan dispositivos)
   - Verificar rango IP correcto

3. **App crashea**:
   - Revisar permisos de red
   - Logcat para detalles

---

**💚 Esta app fue hecha con mucho amor para facilitar la vida de nuestros adultos mayores**
