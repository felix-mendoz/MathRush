# Walkthrough - Implementación de Modo Offline y Online en MathRush y Ampliación Matemática

Se han completado todas las tareas planificadas para dotar a **MathRush** de un sistema dual de funcionamiento offline/online mediante Firebase, sincronización robusta, dificultades y categorías matemáticas avanzadas, modo práctica con reportes y pantallas de perfil y clasificación global.

## Cambios Realizados

### 1. Dependencias de Firebase
- **[build.gradle.kts (app)](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/build.gradle.kts)** y **[libs.versions.toml](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/gradle/libs.versions.toml)**: Se añadieron el BoM de Firebase, Firebase Authentication y Cloud Firestore.

### 2. Capa de Datos y Sincronización
- **[ScoreEntity.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/data/ScoreEntity.kt)** y **[ScoreDatabaseHelper.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/data/ScoreDatabaseHelper.kt)**: Actualizado el esquema SQLite (v2) para soportar identificadores únicos de partida (`game_id`), estado de sincronización (`is_synced`), categorías y subcategorías.
- **[AuthRepository.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/data/AuthRepository.kt)**: Gestión de Firebase Auth (inicio de sesión, registro, cierre de sesión, sesión persistente).
- **[FirestoreRepository.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/data/FirestoreRepository.kt)**: Guardado de puntuaciones y consulta de clasificación global en Cloud Firestore.
- **[SyncManager.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/data/SyncManager.kt)**: Mecanismo offline-first para detectar partidas pendientes y sincronizarlas de forma idempotente con Firebase.

### 3. Motor de Juego, Dificultades y Categorías
- **[QuestionGenerator.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/game/QuestionGenerator.kt)**: Ampliación modular para soportar Aritmética, Álgebra, Cálculo Diferencial, Cálculo Integral y Geometría y Trigonometría con subcategorías y dificultad dinámica.
- **[GameEngine.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/game/GameEngine.kt)**: Configuración de temporizadores por nivel de dificultad (Fácil: 15s, Intermedio: 10s, Avanzado: 20s) y registro con metadatos completos.

### 4. Modo Práctica y Reportes
- **[PracticeSessionManager.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/game/PracticeSessionManager.kt)**: Seguimiento de precisión, tiempos promedio y generación de recomendaciones inteligentes basadas en reglas deterministas.
- **[PracticeReportScreen.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/ui/screens/PracticeReportScreen.kt)**: Pantalla visual de reporte y estadísticas por categoría.

### 5. Interfaz de Usuario y Navegación
- **[ProfileScreen.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/ui/screens/ProfileScreen.kt)**: Sección de perfil, gestión de cuenta, cierre de sesión y botón de sincronización de partidas pendientes.
- **[LeaderboardScreen.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/ui/screens/LeaderboardScreen.kt)**: Clasificación global con Cloud Firestore.
- **[MainActivity.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/MainActivity.kt)**: Navegación integrada entre Inicio, Login, Registro, Perfil, Clasificación Global y Juego.

## Validación y Pruebas
- **Compilación Exitosa**: Se ejecutó `./gradlew assembleDebug` obteniendo un build exitoso sin errores.
- **Modo Offline**: La aplicación funciona completamente offline utilizando SQLite de forma predeterminada.

## Configuración en Firebase Console (Opcional para habilitar modo online)
1. Crear un proyecto en [Firebase Console](https://console.firebase.google.com/).
2. Añadir una aplicación Android con el package name `com.example.mathrush`.
3. Descargar el archivo `google-services.json` y colocarlo en el directorio `app/`.
4. Habilitar **Authentication** (Email/Password) y **Cloud Firestore**.
