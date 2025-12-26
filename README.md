# Control Remoto TV - Para Abuelos

Una aplicación Android diseñada específicamente para personas mayores que deseen controlar su Android TV de forma fácil e intuitiva.

## 🎯 Características

- **Interfaz Grande y Clara**: Botones grandes y legibles, perfectos para personas con visión reducida
- **Controles Básicos**: Navegación (arriba, abajo, izquierda, derecha), selección
- **Control de Volumen y Poder**: Botones dedicados para volumen y encendido/apagado
- **Números del 0-9**: Para cambiar canales rápidamente
- **Gestor de Aplicaciones**: Lista y lanza aplicaciones instaladas en el TV
- **Diseño Accesible**: Colores contrastados, texto grande, sin complejidades

## 📱 Requisitos

- Android 5.0 (API 21) o superior
- Permiso de acceso a red (para posible conectividad futura)

## 🚀 Instalación

1. Descarga el APK desde la sección de Releases
2. Instala en tu teléfono o tablet Android
3. Abre la aplicación
4. ¡Comienza a controlar tu TV!

## 🎮 Cómo Usar

### Pantalla Principal (Remoto)
- **↑↓←→**: Navega por los menús del TV
- **OK (Centro)**: Selecciona la opción
- **Poder**: Enciende/apaga el TV
- **Vol +/Vol -**: Aumenta o disminuye el volumen
- **Mute**: Silencia el TV
- **Inicio**: Vuelve a la pantalla de inicio del TV
- **Atrás**: Retrocede a la pantalla anterior
- **Apps**: Abre el gestor de aplicaciones
- **0-9**: Cambia directamente a un canal

### Pantalla de Aplicaciones
1. Toca el botón "Apps" en el remoto
2. Se mostrará una lista de todas las aplicaciones instaladas
3. Toca cualquier app para abrirla en el TV
4. Toca "Atrás" para volver al remoto

## 🛠️ Compilación

### Requisitos
- Java JDK 11 o superior
- Android SDK
- Gradle

### Compilar APK
```bash
./gradlew assembleRelease
```

El APK se generará en: `app/build/outputs/apk/release/app-release-unsigned.apk`

## 📋 Estructura del Proyecto

```
ControlRemotoTV-Abuelitos/
├── app/
│   ├── src/main/
│   │   ├── java/com/abuelos/controlremototv/
│   │   │   ├── MainActivity.java
│   │   │   ├── AppManager.java
│   │   │   ├── AppAdapter.java
│   │   │   └── AppInfo.java
│   │   └── res/
│   │       ├── layout/
│   │       ├── values/
│   │       └── drawable/
│   └── build.gradle
├── .github/workflows/
│   └── build.yml (CI/CD con GitHub Actions)
└── build.gradle
```

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:
1. Haz un fork del proyecto
2. Crea una rama con tu feature
3. Commit tus cambios
4. Push a la rama
5. Abre un Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia MIT.

## 👨‍👩‍👧‍👦 Dedicado

Dedicado a todas las personas mayores que merecen tecnología fácil de usar.

---

**¿Preguntas o sugerencias?** Abre un issue en el repositorio.
