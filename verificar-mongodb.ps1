# Script para verificar MongoDB Atlas accesibilidad
# Ejecutar desde PowerShell

Write-Host "╔════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║         VERIFICADOR DE MONGODB ATLAS                      ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# Variables
$MONGO_USER = "ccanabalr_db_user"
$MONGO_PASS = "ptELeKry8mbUCFJw"
$MONGO_CLUSTER = "cluster0.jahbc3i.mongodb.net"
$MONGO_DB = "control_financiero"

# Construir URI
$MONGO_URI = "mongodb+srv://$($MONGO_USER):$($MONGO_PASS)@$($MONGO_CLUSTER)/$($MONGO_DB)?retryWrites=true&w=majority"

Write-Host "📋 INFORMACIÓN DE CONEXIÓN" -ForegroundColor Yellow
Write-Host "───────────────────────────────────────────────────────────" -ForegroundColor Gray
Write-Host "Usuario:    $MONGO_USER" -ForegroundColor Green
Write-Host "Contraseña: ••••••••" -ForegroundColor Green
Write-Host "Cluster:    $MONGO_CLUSTER" -ForegroundColor Green
Write-Host "Base de Datos: $MONGO_DB" -ForegroundColor Green
Write-Host ""

Write-Host "🔗 URI DE CONEXIÓN COMPLETA:" -ForegroundColor Yellow
Write-Host "───────────────────────────────────────────────────────────" -ForegroundColor Gray
Write-Host $MONGO_URI -ForegroundColor Cyan
Write-Host ""

Write-Host "✅ VERIFICACIONES:" -ForegroundColor Yellow
Write-Host "───────────────────────────────────────────────────────────" -ForegroundColor Gray

# Verificar formato URI
if ($MONGO_URI -match "mongodb\+srv://") {
    Write-Host "✓ URI comienza con 'mongodb+srv://'" -ForegroundColor Green
} else {
    Write-Host "✗ URI no comienza correctamente" -ForegroundColor Red
}

if ($MONGO_URI -match "@$MONGO_CLUSTER") {
    Write-Host "✓ Cluster es correcto: $MONGO_CLUSTER" -ForegroundColor Green
} else {
    Write-Host "✗ Cluster parece incorrecto" -ForegroundColor Red
}

if ($MONGO_URI -match "/$MONGO_DB\?") {
    Write-Host "✓ Base de datos especificada: /$MONGO_DB" -ForegroundColor Green
} else {
    Write-Host "✗ Base de datos NO está especificada en la URI" -ForegroundColor Red
}

if ($MONGO_URI -match "retryWrites=true") {
    Write-Host "✓ retryWrites activado" -ForegroundColor Green
} else {
    Write-Host "✗ retryWrites no está activado" -ForegroundColor Red
}

Write-Host ""
Write-Host "📝 PASOS A VERIFICAR EN MONGODB ATLAS:" -ForegroundColor Yellow
Write-Host "───────────────────────────────────────────────────────────" -ForegroundColor Gray
Write-Host "1. Ve a: https://cloud.mongodb.com/" -ForegroundColor Cyan
Write-Host "2. Selecciona Cluster0" -ForegroundColor Cyan
Write-Host "3. Database Access:" -ForegroundColor Cyan
Write-Host "   - Busca usuario: $MONGO_USER" -ForegroundColor Cyan
Write-Host "   - Debe estar ACTIVE (verde)" -ForegroundColor Cyan
Write-Host "4. Network Access:" -ForegroundColor Cyan
Write-Host "   - Debe tener 0.0.0.0/0 (Allow access from anywhere)" -ForegroundColor Cyan
Write-Host "   - Debe estar ACTIVE (verde)" -ForegroundColor Cyan
Write-Host ""

Write-Host "🚀 PARA PROBAR LOCALMENTE:" -ForegroundColor Yellow
Write-Host "───────────────────────────────────────────────────────────" -ForegroundColor Gray
Write-Host "Ejecuta desde PowerShell en la carpeta del backend:" -ForegroundColor Cyan
Write-Host ""
Write-Host ".\gradlew bootRun" -ForegroundColor Magenta
Write-Host ""
Write-Host "Busca en los logs:" -ForegroundColor Cyan
Write-Host "✓ 'MongoClient with metadata created'" -ForegroundColor Green
Write-Host "✓ 'Adding discovered server'" -ForegroundColor Green
Write-Host "✓ 'Started ControlFinancieroAplicacion'" -ForegroundColor Green
Write-Host ""

Write-Host "❓ SI HAY ERROR, BUSCA EN LOS LOGS:" -ForegroundColor Yellow
Write-Host "───────────────────────────────────────────────────────────" -ForegroundColor Gray
Write-Host "• 'Exception authenticating' → Contraseña incorrecta" -ForegroundColor Red
Write-Host "• 'SocketTimeoutException' → IP no permitida en Atlas" -ForegroundColor Red
Write-Host "• 'Database name must not be empty' → Falta /control_financiero" -ForegroundColor Red
Write-Host ""

Write-Host "✅ GUARDA ESTA URI PARA RENDER:" -ForegroundColor Yellow
Write-Host "───────────────────────────────────────────────────────────" -ForegroundColor Gray
Write-Host "Copia exactamente:" -ForegroundColor Cyan
Write-Host $MONGO_URI -ForegroundColor Yellow
Write-Host ""
Write-Host "Y pégalo en Render Dashboard → Environment → MONGODB_URI" -ForegroundColor Green