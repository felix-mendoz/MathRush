# Implementación del modo Offline y Online en MathRush mediante Firebase y Ampliación de Funcionalidades Matemáticas

Este plan detalla la arquitectura y los pasos para integrar Firebase (Authentication y Cloud Firestore), sincronización offline/online robusta, sistema de dificultades, categorías matemáticas ampliadas, modo práctica con reportes y estadísticas, y pantallas de perfil y clasificación global en **MathRush**.

## User Review Required

> [!IMPORTANT]
> **Firebase Setup**: Se requerirá configurar un proyecto en Firebase Console (Authentication con Email/Password, Cloud Firestore en modo de producción con reglas de seguridad adecuadas) y añadir el archivo `google-services.json` en el módulo `app/`.

> [!NOTE]
> **Compatibilidad Offline**: El almacenamiento local en SQLite seguirá siendo el núcleo principal de funcionamiento. La app funcionará 100% offline sin requerir inicio de sesión, y sincronizará las puntuaciones automáticamente cuando haya conexión y un usuario autenticado.

## Open Questions

- ¿Desea que el archivo `google-services.json` se configure con credenciales de prueba o dejarás la guía paso a paso para añadir el tuyo de Firebase Console? (Se proporcionará la guía detallada para Firebase Console).

## Proposed Changes

### 1. Dependencias y Configuración de Firebase
#### [MODIFY] [build.gradle.kts (project)](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/build.gradle.kts)
#### [MODIFY] [build.gradle.kts (app)](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/build.gradle.kts)
#### [MODIFY] [libs.versions.toml](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/gradle/libs.versions.toml)

### 2. Capa de Datos y Modelos (SQLite + Firestore + Sincronización)
#### [MODIFY] [ScoreDatabaseHelper.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/data/ScoreDatabaseHelper.kt) - Actualizar esquema SQLite para incluir categoría, subcategoría, modo, estado de sincronización (`is_synced`), etc.
#### [NEW] [AuthRepository.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/data/AuthRepository.kt) - Gestión de Firebase Auth (Login, Registro, Cierre de sesión, Estado de usuario).
#### [NEW] [FirestoreRepository.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/data/FirestoreRepository.kt) - Gestión de puntuaciones globales, perfiles de usuario y sincronización.
#### [NEW] [SyncManager.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/data/SyncManager.kt) - Detección de conectividad, reintentos e idempotencia en sincronización de partidas pendientes.

### 3. Motor de Juego, Dificultades y Categorías Matemáticas
#### [MODIFY] [QuestionGenerator.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/game/QuestionGenerator.kt) - Ampliar categorías (Aritmética, Álgebra, Cálculo Diferencial, Cálculo Integral, Geometría y Trigonometría) con subcategorías y generación dinámica robusta.
#### [MODIFY] [GameEngine.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/game/GameEngine.kt) - Soporte para tiempos por dificultad (Fácil 15s, Intermedio 10s, Avanzado personalizable) y registro con metadatos completos.

### 4. Modo Práctica y Seguimiento del Aprendizaje
#### [NEW] [PracticeSessionManager.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/game/PracticeSessionManager.kt) - Lógica para registrar respuestas, calcular precisión, tiempos promedio y generar recomendaciones basadas en reglas deterministas.
#### [NEW] [PracticeReportScreen.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/ui/screens/PracticeReportScreen.kt) - Pantalla de reporte de rendimiento.

### 5. Interfaz de Usuario y Navegación
#### [MODIFY] [Screen.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/navigation/Screen.kt) - Nuevas rutas (Perfil, Clasificación Global, Modo Práctica, Reporte).
#### [MODIFY] [HomeScreen.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/ui/screens/HomeScreen.kt) - Accesos a perfil, clasificación, modo práctica e indicador de conectividad/sincronización.
#### [NEW] [ProfileScreen.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/ui/screens/ProfileScreen.kt) - Gestión de cuenta, sesión y estado offline/online.
#### [NEW] [LeaderboardScreen.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/ui/screens/LeaderboardScreen.kt) - Clasificación global con Firestore.

## Verification Plan

### Automated Tests
- Ejecutar pruebas unitarias existentes (`./gradlew test`) y verificar compilación limpia.
- Añadir pruebas unitarias para el generador de preguntas matemáticas y cálculo de estadísticas.

### Manual Verification
- Desplegar la app en el emulador o dispositivo.
- Jugar en modo offline (comprobar almacenamiento local).
- Iniciar sesión / Registrarse con Firebase Auth.
- Jugar en modo online, verificar sincronización con Firestore y consulta de clasificación global.
- Probar Modo Práctica y visualización de reportes.
