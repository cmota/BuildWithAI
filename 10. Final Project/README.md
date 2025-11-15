# QuizSpark

QuizSpark is an Android app that lets users generate AI-powered quizzes on any topic, play through the questions, and submit their scores to a shared leaderboard. It is built entirely with Jetpack Compose and integrates Firebase for AI generation and persistence.

## Features

- **AI quiz creation** – Gemini generates multiple-choice questions based on the topic the user provides on the intro screen.
- **Interactive quiz flow** – A Compose-powered UI walks the user through the questions one at a time with progress indicators and option selection states.
- **Score submission** – After the last question, users can enter a username and submit their points.
- **Live leaderboard** – Scores are stored in Cloud Firestore and displayed in a dedicated leaderboard screen.
- **Material 3 design** – Custom theming and dark styling using Material 3 components and icons.

## Tech Stack

- Kotlin, Jetpack Compose, Material 3
- Firebase AI SDK (Gemini), Firebase Firestore
- Android Navigation (navigation3 runtime/UI)
- KotlinX Serialization

## Project Structure

```
app/
 ├─ src/main/java/com/cmota/quizspark/
 │   ├─ MainActivity.kt           # Hosts Compose navigation
 │   ├─ model/                    # Question & User data classes
 │   ├─ ui/questions/             # Quiz generation + state management
 │   ├─ ui/leaderboard/           # Leaderboard state management
 │   ├─ ui/navigation/            # Navigation graph & routes
 │   ├─ ui/screens/               # Intro, Quiz, Leaderboard screens
 │   └─ ui/theme/                 # Color, typography, theme definitions
 ├─ src/main/res/                 # Strings, drawables, theming XML
 └─ build.gradle.kts              # Module configuration & dependencies
```

## Prerequisites

- Android Studio Ladybug (AGP 8.13.1) or newer Canary build supporting API 36 preview features.
- JDK 17 configured for Gradle builds.
- Firebase project with Firestore enabled and Gemini access (for `firebase-ai`).

## Getting Started

1. **Clone the repo**
   ```bash
   git clone https://github.com/<your-account>/QuizSpark.git
   cd QuizSpark
   ```
2. **Configure Firebase**
   - Place your `google-services.json` file under `app/`.
   - Ensure Firestore has a `leaderboard` collection or enable security rules that allow creation on first write.
3. **Sync dependencies**
   - Open the project in Android Studio and let Gradle sync.
4. **Run the app**
   - Use the **Run**/`app` configuration to install on a device or emulator with Google Play Services.

## Firebase Setup Notes

- **Gemini access**: The Firebase project must have the Generative AI (Gemini) API enabled. Update billing if required.
- **Firestore rules**: During development you can use permissive rules, but for production restrict writes to authenticated users and validate score ranges.
- **AI quota**: `generateContent` requests count against your project's quota—monitor usage if you demo frequently.

## License

QuizSpark 2.0 is licensed under the Apache License 2.0. See the LICENSE file for details.