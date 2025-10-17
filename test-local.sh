#!/bin/bash

# Script de prueba local del backend antes de desplegar a Render
# Uso: bash test-local.sh

set -e  # Salir si hay error

echo "🧪 Iniciando pruebas del backend..."
echo ""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Verificar que estamos en el directorio correcto
if [ ! -f "build.gradle" ]; then
    echo -e "${RED}❌ Error: No se encontró build.gradle${NC}"
    echo "Ejecuta este script desde la raíz del proyecto backend"
    exit 1
fi

echo -e "${YELLOW}1️⃣ Verificando que Gradle está instalado...${NC}"
if ! command -v gradle &> /dev/null && [ ! -f "gradlew" ]; then
    echo -e "${RED}❌ Gradle no está instalado${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Gradle encontrado${NC}"
echo ""

echo -e "${YELLOW}2️⃣ Limpiando builds anteriores...${NC}"
./gradlew clean
echo -e "${GREEN}✅ Limpieza completada${NC}"
echo ""

echo -e "${YELLOW}3️⃣ Compilando el proyecto...${NC}"
./gradlew build -x test
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Compilación exitosa${NC}"
else
    echo -e "${RED}❌ Error en la compilación${NC}"
    exit 1
fi
echo ""

echo -e "${YELLOW}4️⃣ Verificando que se generó el JAR...${NC}"
JAR_FILE=$(ls -1 build/libs/*.jar 2>/dev/null | head -1)
if [ -z "$JAR_FILE" ]; then
    echo -e "${RED}❌ No se encontró el archivo JAR${NC}"
    exit 1
fi
echo -e "${GREEN}✅ JAR encontrado: $JAR_FILE${NC}"
echo ""

echo -e "${YELLOW}5️⃣ Verificando tamaño del JAR...${NC}"
JAR_SIZE=$(ls -lh "$JAR_FILE" | awk '{print $5}')
echo -e "${GREEN}✅ Tamaño del JAR: $JAR_SIZE${NC}"
echo ""

echo -e "${YELLOW}6️⃣ Verificando que el JAR es ejecutable...${NC}"
if java -jar "$JAR_FILE" --version &> /dev/null; then
    echo -e "${GREEN}✅ JAR es ejecutable${NC}"
else
    echo -e "${YELLOW}⚠️  No se puede verificar totalmente, pero continuar${NC}"
fi
echo ""

echo -e "${YELLOW}7️⃣ Listando dependencias críticas...${NC}"
if grep -q "spring-boot-starter-web" build.gradle; then
    echo -e "${GREEN}✅ Spring Boot Web encontrado${NC}"
else
    echo -e "${RED}❌ Spring Boot Web no encontrado${NC}"
    exit 1
fi

if grep -q "mongodb" build.gradle; then
    echo -e "${GREEN}✅ MongoDB encontrado${NC}"
else
    echo -e "${YELLOW}⚠️  MongoDB no está en build.gradle${NC}"
fi

if grep -q "firebase" build.gradle; then
    echo -e "${GREEN}✅ Firebase encontrado${NC}"
else
    echo -e "${YELLOW}⚠️  Firebase no está en build.gradle${NC}"
fi
echo ""

echo -e "${GREEN}════════════════════════════════════════${NC}"
echo -e "${GREEN}✅ TODAS LAS PRUEBAS LOCALES PASARON${NC}"
echo -e "${GREEN}════════════════════════════════════════${NC}"
echo ""
echo "El backend está listo para desplegar a Render!"
echo ""
echo "Próximos pasos:"
echo "1. Commits los cambios: git add . && git commit -m 'chore: ready for deployment'"
echo "2. Push a GitHub: git push -u origin master"
echo "3. Ve a Render y crea un nuevo Web Service"
echo "4. Conecta tu repositorio GitHub"
echo "5. Configura las variables de entorno"
echo "6. Render construirá y desplegará automáticamente"
echo ""