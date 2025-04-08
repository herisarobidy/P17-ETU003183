@echo off
:: Définir les variables de répertoire
set "SRC_DIR=C:\Users\Sarobidy\OneDrive\Desktop\web_d\ETU003183\src"
set "LIB_DIR=C:\Users\Sarobidy\OneDrive\Desktop\web_d\ETU003183\lib"
set "WEB_DIR=%SRC_DIR%\webapp"
set "CLASSES_DIR=%WEB_DIR%\WEB-INF\classes"
set "BUILD_DIR=C:\Users\Sarobidy\OneDrive\Desktop\web_d\ETU003183\build"
set "WAR_FILE=%BUILD_DIR%\ETU003183.war"
set "TOMCAT_WEBAPPS_DIR=C:\Users\Sarobidy\OneDrive\Desktop\tomcat-10.1.28-windows-x64\apache-tomcat-10.1.28\webapps"

:: Vérifier si les répertoires nécessaires existent
if not exist "%SRC_DIR%" (
    echo Le repertoire source %SRC_DIR% n'existe pas !
    exit /b 1
)
if not exist "%LIB_DIR%" (
    echo Le repertoire lib %LIB_DIR% n'existe pas !
    exit /b 1
)
if not exist "%WEB_DIR%" (
    echo Le repertoire webapp %WEB_DIR% n'existe pas !
    exit /b 1
)

echo Nettoyage du repertoire build...
if exist "%BUILD_DIR%" (
    rd /s /q "%BUILD_DIR%"
)

:: Créer le répertoire BUILD si nécessaire
if not exist "%BUILD_DIR%" (
    mkdir "%BUILD_DIR%"
)

:: Créer le répertoire CLASSES si nécessaire
if not exist "%CLASSES_DIR%" (
    mkdir "%CLASSES_DIR%"
)

:: Etape 1 : Compiler les fichiers Java
echo Compilation des fichiers Servlet...
set "JAVA_FILES=%SRC_DIR%\java\PrevisionServlet.java %SRC_DIR%\java\DepenseServlet.java %SRC_DIR%\java\EtatServlet.java"
javac -cp "%LIB_DIR%\jakarta.servlet-api-6.0.0.jar;%LIB_DIR%\mysql-connector-java-8.0.33.jar" -d "%CLASSES_DIR%" %JAVA_FILES%
if %ERRORLEVEL% neq 0 (
    echo Echec de la compilation !
    exit /b %ERRORLEVEL%
)

:: Etape 2 : Copier les fichiers web dans le répertoire de build
echo Copie des fichiers web vers le repertoire build...
xcopy /E /I "%WEB_DIR%" "%BUILD_DIR%"
if %ERRORLEVEL% neq 0 (
    echo Echec de la copie des fichiers web !
    exit /b %ERRORLEVEL%
)

:: Etape 3 : Créer le fichier .war
echo Creation du fichier .war...
jar -cvf "%WAR_FILE%" -C "%BUILD_DIR%" .
if %ERRORLEVEL% neq 0 (
    echo Echec de la creation du fichier .war !
    exit /b %ERRORLEVEL%
)

:: Etape 4 : Déployer le fichier .war dans Tomcat
echo Deploiement du fichier .war dans Tomcat...
copy "%WAR_FILE%" "%TOMCAT_WEBAPPS_DIR%"
if %ERRORLEVEL% neq 0 (
    echo Echec du deploiement !
    exit /b %ERRORLEVEL%
)

:: Fin
echo Deploiement reussi !
pause