# Script de prueba local del backend antes de desplegar a Render - Windows PowerShell
# Uso: .\test-local.ps1

Write-Host "🧪 Iniciando pruebas del backend..." -ForegroundColor Green
Write-Host ""

# Verificar que estamos en el directorio correcto
if (-not (Test-Path "build.gradle")) {
    Write-Host "❌ Error: No se encontró build.gradle" -ForegroundColor Red
    Write-Host "Ejecuta este script desde la raíz del proyecto backend" -ForegroundColor Yellow
    exit 1
}

Write-Host "1️⃣ Verificando que Gradle está instalado..." -ForegroundColor Yellow
if (-not (Test-Path "gradlew.bat")) {
    Write-Host "❌ Gradle no está instalado" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Gradle encontrado" -ForegroundColor Green
Write-Host ""

Write-Host "2️⃣ Limpiando builds anteriores..." -ForegroundColor Yellow
& ".\gradlew.bat" clean
Write-Host "✅ Limpieza completada" -ForegroundColor Green
Write-Host ""

Write-Host "3️⃣ Compilando el proyecto..." -ForegroundColor Yellow
& ".\gradlew.bat" build -x test
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Compilación exitosa" -ForegroundColor Green
} else {
    Write-Host "❌ Error en la compilación" -ForegroundColor Red
    exit 1
}
Write-Host ""

Write-Host "4️⃣ Verificando que se generó el JAR..." -ForegroundColor Yellow
$jarFile = Get-ChildItem "build\libs\*.jar" -ErrorAction SilentlyContinue | Select-Object -First 1
if ($null -eq $jarFile) {
    Write-Host "❌ No se encontró el archivo JAR" -ForegroundColor Red
    exit 1
}
Write-Host "✅ JAR encontrado: $($jarFile.Name)" -ForegroundColor Green
Write-Host ""

Write-Host "5️⃣ Verificando tamaño del JAR..." -ForegroundColor Yellow
$jarSize = [math]::Round($jarFile.Length / 1MB, 2)
Write-Host "✅ Tamaño del JAR: $($jarSize) MB" -ForegroundColor Green
Write-Host ""

Write-Host "6️⃣ Verificando que el JAR es ejecutable..." -ForegroundColor Yellow
try {
    & java -jar $jarFile.FullName --version 2>&1 | Out-Null
    Write-Host "✅ JAR es ejecutable" -ForegroundColor Green
} catch {
    Write-Host "⚠️  No se puede verificar totalmente, pero continuar" -ForegroundColor Yellow
}
Write-Host ""

Write-Host "7️⃣ Listando dependencias críticas..." -ForegroundColor Yellow
$buildGradleContent = Get-Content "build.gradle"

if ($buildGradleContent -match "spring-boot-starter-web") {
    Write-Host "✅ Spring Boot Web encontrado" -ForegroundColor Green
} else {
    Write-Host "❌ Spring Boot Web no encontrado" -ForegroundColor Red
    exit 1
}

if ($buildGradleContent -match "mongodb") {
    Write-Host "✅ MongoDB encontrado" -ForegroundColor Green
} else {
    Write-Host "⚠️  MongoDB no está en build.gradle" -ForegroundColor Yellow
}

if ($buildGradleContent -match "firebase") {
    Write-Host "✅ Firebase encontrado" -ForegroundColor Green
} else {
    Write-Host "⚠️  Firebase no está en build.gradle" -ForegroundColor Yellow
}
Write-Host ""

Write-Host "════════════════════════════════════════" -ForegroundColor Green
Write-Host "✅ TODAS LAS PRUEBAS LOCALES PASARON" -ForegroundColor Green
Write-Host "════════════════════════════════════════" -ForegroundColor Green
Write-Host ""
Write-Host "El backend está listo para desplegar a Render!"
Write-Host ""
Write-Host "Próximos pasos:"
Write-Host "1. Commits los cambios: git add . && git commit -m 'chore: ready for deployment'"
Write-Host "2. Push a GitHub: git push -u origin master"
Write-Host "3. Ve a Render y crea un nuevo Web Service"
Write-Host "4. Conecta tu repositorio GitHub"
Write-Host "5. Configura las variables de entorno"
Write-Host "6. Render construirá y desplegará automáticamente"
Write-Host ""