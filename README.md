# LionBlacksap
Guía de Uso de la Aplicación LionPass
Inicio de Sesión
Al ingresar a la aplicación, serás dirigido a la ventana de inicio de sesión. En esta pantalla encontrarás dos opciones:

Iniciar Sesión:
Si ya tienes una cuenta creada, simplemente introduce tus credenciales (correo y contraseña) y presiona el botón Iniciar Sesión.

Crear Cuenta:
Si aún no tienes una cuenta, presiona el botón Crear Cuenta. Esto te llevará a la ventana de registro, donde deberás completar todos los campos obligatorios para crear una cuenta.



Ventana Principal (Home)
Una vez que inicies sesión correctamente, serás redirigido a la ventana principal (Home), donde podrás realizar las siguientes acciones:

Ver Conversaciones Activas:
Aquí encontrarás un listado de todas tus conversaciones actuales.

Iniciar una Nueva Conversación:
Para iniciar una nueva conversación, ingresa el correo electrónico de la persona con la que deseas chatear y presiona el botón Iniciar Conversación.

Cerrar Sesión
Si deseas cerrar tu sesión activa, presiona el botón Cerrar Sesión en la ventana principal. Esto finalizará tu sesión y te redirigirá automáticamente a la ventana de inicio de sesión.


Configuración de Firebase en Android Studio:

Crea un proyecto en Firebase:

Ve a Firebase Console y haz clic en Agregar un proyecto.
Sigue los pasos y habilita Analytics (opcional).
Registra tu aplicación Android

En Firebase, haz clic en Agregar app y selecciona Android.
Escribe el nombre del paquete (lo encuentras en AndroidManifest.xml).
Descarga el archivo google-services.json.
Coloca el archivo en tu proyecto

Pon google-services.json en la carpeta app de tu proyecto.
Configura Gradle

En build.gradle (nivel proyecto), agrega:
gradle
Copiar código
classpath 'com.google.gms:google-services:4.4.0'
En build.gradle (nivel app), agrega:
gradle
Copiar código
apply plugin: 'com.google.gms.google-services'

implementation platform('com.google.firebase:firebase-bom:32.1.0')
implementation 'com.google.firebase:firebase-auth'
implementation 'com.google.firebase:firebase-database'
Habilita servicios en Firebase

En la consola de Firebase, activa Authentication (correo y contraseña) y Realtime Database (modo de pruebas).
Sincroniza y prueba

Sincroniza con Gradle en Android Studio.
Corre la app y verifica que se conecta a Firebase.
