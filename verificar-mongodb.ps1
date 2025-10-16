# Script para verificar MongoDB Atlas accesibilidad
# Ejecutar desde PowerShell

Write-Host "VERIFICADOR DE MONGODB ATLAS" -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host ""

# Variables
$MONGO_USER = "ccanabalr_db_user"
$MONGO_PASS = "ptELeKry8mbUCFJw"
$MONGO_CLUSTER = "cluster0.jahbc3i.mongodb.net"
$MONGO_DB = "control_financiero"

# Construir URI - Escapar el ampersand
$MONGO_URI = "mongodb+srv://$($MONGO_USER):$($MONGO_PASS)@$($MONGO_CLUSTER)/$($MONGO_DB)?retryWrites=true" + "&w=majority"

Write-Host "INFORMACION DE CONEXION" -ForegroundColor Yellow
Write-Host "Usuario:    $MONGO_USER" -ForegroundColor Green
Write-Host "Contraseña: ••••••••" -ForegroundColor Green
Write-Host "Cluster:    $MONGO_CLUSTER" -ForegroundColor Green
Write-Host "Base de Datos: $MONGO_DB" -ForegroundColor Green
Write-Host ""

Write-Host "URI DE CONEXION COMPLETA:" -ForegroundColor Yellow
Write-Host "═════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host $MONGO_URI -ForegroundColor Yellow
Write-Host "═════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

Write-Host "VERIFICACIONES:" -ForegroundColor Yellow

# Verificar formato URI
if ($MONGO_URI -like "*mongodb+srv://*") {
    Write-Host "[OK] URI comienza con 'mongodb+srv://'" -ForegroundColor Green
} else {
    Write-Host "[ERROR] URI no comienza correctamente" -ForegroundColor Red
}

if ($MONGO_URI -like "*$MONGO_CLUSTER*") {
    Write-Host "[OK] Cluster es correcto: $MONGO_CLUSTER" -ForegroundColor Green
} else {
    Write-Host "[ERROR] Cluster parece incorrecto" -ForegroundColor Red
}

if ($MONGO_URI -like "*/$MONGO_DB*") {
    Write-Host "[OK] Base de datos especificada: /$MONGO_DB" -ForegroundColor Green
} else {
    Write-Host "[ERROR] Base de datos NO esta especificada en la URI" -ForegroundColor Red
}

if ($MONGO_URI -like "*retryWrites=true*") {
    Write-Host "[OK] retryWrites activado" -ForegroundColor Green
} else {
    Write-Host "[ERROR] retryWrites no esta activado" -ForegroundColor Red
}

Write-Host ""
Write-Host "PROXIMOS PASOS:" -ForegroundColor Yellow
Write-Host "1. Copia la URI de arriba" -ForegroundColor Cyan
Write-Host "2. Ve a: https://dashboard.render.com" -ForegroundColor Cyan
Write-Host "3. Tu servicio → Environment → MONGODB_URI" -ForegroundColor Cyan
Write-Host "4. Pega la URI y guarda" -ForegroundColor Cyan
Write-Host ""