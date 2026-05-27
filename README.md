# HSK Practice

A comprehensive Android application for practicing and mastering Chinese Hanzi (HSK 1-6). This app provides a modern, interactive way to learn vocabulary, pinyin, meanings, and character writing.

##Features

- **HSK 1-6 Coverage**: Complete word lists for all HSK levels.
- **Interactive Learning**: Study mode with flashcards and detailed character information.
- **Multiple Exercise Modes**:
    - **Meaning Quiz**: Match characters with their English meanings.
    - **Pinyin Quiz**: Practice character pronunciation.
    - **Sentence Practice**: See how words are used in context.
    - **Writing Practice**: Interactive Hanzi drawing exercises using `HanziWriter`.
- **Text-to-Speech (TTS)**: Listen to character pronunciations.
- **Modern UI**: Built with Jetpack Compose and Material 3 for a sleek, responsive experience.

## Technologies Used

- **Kotlin**: Primary programming language.
- **Jetpack Compose**: For the modern, declarative UI.
- **Material 3**: Design system for a consistent and accessible interface.
- **Kotlin Serialization & Gson**: For efficient data handling.
- **Android WebKit**: Powers the interactive Hanzi writing components.
- **Navigation Compose**: Handles smooth transitions between screens.
- **Hanzi Writer**: JavaScript library integrated for character stroke animations and writing practice.

## Project Content

- `app/src/main/assets/`: Contains HSK word lists (HSK 1-6), sentence data, and character stroke data.
- `app/src/main/java/`:
    - `Components/`: Reusable UI elements like buttons and cards.
    - `Screens/`: Major application views (Home, Study, Exercises, etc.).
    - `ViewModels/`: Business logic and state management.
    - `HSKCharacters/`: Data classes for HSK content.
    - `Navigation/`: App routing and navigation logic.
    - `Utils/`: Utility functions like Text-to-Speech.

## How to Build on Your Own Device

### Prerequisites

1.  **Android Studio**: Download and install the latest version of [Android Studio](https://developer.android.com/studio).
2.  **Android SDK**: Ensure you have SDK version 36 (or compatible) installed via the SDK Manager.
3.  **Physical Device or Emulator**: An Android device with developer options enabled or a configured AVD (Android Virtual Device).

### Building with Android Studio

1.  **Clone the Repository**:
    ```bash
    git clone https://github.com/your-username/hsk_practice.git
    ```
2.  **Open Project**: Launch Android Studio and select **Open**, then navigate to the cloned directory.
3.  **Sync Gradle**: Wait for the project to sync. Android Studio will download the necessary dependencies.
4.  **Run**:
    - Connect your Android device via USB or start an emulator.
    - Click the **Run** button (green play icon) in the toolbar.
    - Select your device/emulator and click **OK**.

### Building from Command Line

If you prefer using the terminal:

1.  Navigate to the project root:
    ```bash
    cd hsk_practice
    ```
2.  Build the APK:
    - **Windows**: `.\gradlew.bat assembleDebug`
    - **macOS/Linux**: `./gradlew assembleDebug`
3.  Install on connected device:
    - **Windows**: `.\gradlew.bat installDebug`
    - **macOS/Linux**: `./gradlew installDebug`

## License

This project includes data from various sources. Please refer to `app/src/main/assets/dataset_license.txt` for details regarding the characters datasets.

