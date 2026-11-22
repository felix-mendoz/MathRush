# Plan de Refactorización y Mejora del Motor de Generación Matemática de MathRush

Este plan detalla la refactorización completa del motor de generación matemática, incorporando renderizado LaTeX mediante KaTeX local, un modelo de respuestas extensible (`MathAnswer`), generadores separados por categoría, explicaciones paso a paso y mejoras en la interfaz de usuario.

## User Review Required

> [!IMPORTANT]
> Se integrará KaTeX mediante un WebView local utilizando archivos almacenados en los `assets` de la aplicación para garantizar funcionamiento 100% offline y renderizado matemático de alta calidad.

> [!NOTE]
> El teclado numérico de la pantalla de juego se ampliará para permitir la introducción de números decimales y signos negativos necesarios para los niveles intermedios y avanzados.

## Open Questions

- ¿Deseas mantener alguna pregunta heredada específica del generador anterior o migrar completamente al nuevo sistema modular con LaTeX? *(Se migrará al nuevo sistema modular y robusto).*

## Proposed Changes

### Componente de Renderizado LaTeX y Assets
- **[NEW]** `app/src/main/assets/katex/katex_viewer.html`
- **[NEW]** `app/src/main/assets/katex/katex.min.css`
- **[NEW]** `app/src/main/assets/katex/katex.min.js`
- **[NEW]** `app/src/main/java/com/example/mathrush/ui/components/MathView.kt`

### Modelos y Sistema de Respuestas (`MathAnswer` y `MathQuestion`)
- **[MODIFY]** [QuestionGenerator.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/game/QuestionGenerator.kt) (Reestructurado en clases selladas de respuestas, modelo enriquecido con LaTeX y explicaciones).

### Generadores Modulares por Categoría
- **[NEW]** `app/src/main/java/com/example/mathrush/game/generators/QuestionGeneratorStrategy.kt`
- **[NEW]** `app/src/main/java/com/example/mathrush/game/generators/ArithmeticGenerator.kt`
- **[NEW]** `app/src/main/java/com/example/mathrush/game/generators/AlgebraGenerator.kt`
- **[NEW]** `app/src/main/java/com/example/mathrush/game/generators/DifferentialGenerator.kt`
- **[NEW]** `app/src/main/java/com/example/mathrush/game/generators/IntegralGenerator.kt`
- **[NEW]** `app/src/main/java/com/example/mathrush/game/generators/GeometryGenerator.kt`

### Motor de Juego y UI (`GameEngine`, `GameScreen`)
- **[MODIFY]** [GameEngine.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/game/GameEngine.kt) (Actualizado para validar contra `MathAnswer`).
- **[MODIFY]** [GameScreen.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/ui/screens/GameScreen.kt) (Integración de `MathView` y teclado extendido con punto decimal y signo negativo).
- **[MODIFY]** [PracticeSessionManager.kt](file:///C:/Users/Sadlas/AndroidStudioProjects/MathRush/app/src/main/java/com/example/mathrush/game/PracticeSessionManager.kt) (Actualizado para soportar `MathAnswer`).

### Pruebas Unitarias
- **[NEW]** `app/src/test/java/com/example/mathrush/game/QuestionGeneratorsTest.kt`

## Verification Plan

### Automated Tests
- Ejecutar pruebas unitarias de los generadores mediante `./gradlew testDebugUnitTest`.

### Manual Verification
- Desplegar o compilar el proyecto para verificar que la interfaz renderiza correctamente las expresiones LaTeX y el juego fluye sin errores.
