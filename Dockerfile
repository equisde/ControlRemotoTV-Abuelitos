FROM openjdk:17-slim

# Instalar dependencias
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    git \
    && rm -rf /var/lib/apt/lists/*

# Descargar Android SDK
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools

RUN mkdir -p $ANDROID_HOME && cd $ANDROID_HOME && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-9862592_latest.zip && \
    unzip -q commandlinetools-linux-9862592_latest.zip && \
    rm commandlinetools-linux-9862592_latest.zip && \
    mkdir -p $ANDROID_HOME/cmdline-tools/latest && \
    mv cmdline-tools/* $ANDROID_HOME/cmdline-tools/latest/ && \
    yes | sdkmanager --sdk_root=$ANDROID_HOME "platforms;android-34" "build-tools;34.0.0" "platform-tools" 2>&1 | tail -5

# Descargar Gradle 8.7.1
RUN mkdir -p /opt && cd /opt && \
    wget -q https://services.gradle.org/distributions/gradle-8.7.1-bin.zip && \
    unzip -q gradle-8.7.1-bin.zip && \
    rm gradle-8.7.1-bin.zip

ENV PATH=$PATH:/opt/gradle-8.7.1/bin

# Copiar proyecto
WORKDIR /workspace
COPY . .

# Compilar
RUN gradle clean assembleDebug

# Resultado
CMD ["bash", "-c", "echo '✅ Compilación exitosa!' && ls -lh app/build/outputs/apk/debug/"]
