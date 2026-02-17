StudyWizard (Android) — OCR + AI Study Assistant

StudyWizard is an Android application developed with Kotlin and Jetpack
Compose that transforms handwritten or printed study notes into
structured learning materials.

The application enables users to capture or upload images of notes,
extract text using on-device OCR, and generate summaries, flashcards,
and quizzes using an AI text generation API (Cohere). User activity and
generated content are stored securely per account using Firebase
Firestore.

------------------------------------------------------------------------

Features

Authentication - Email and password authentication using Firebase
Authentication - User profile data stored in Firestore

Study Tools - Text Summarization - Input via manual text or image
upload - OCR extraction with Google ML Kit - AI-generated summaries
using Cohere API

-   Flashcard Generation
    -   Automatic question-answer pairs generated from extracted content
-   Quiz Generation
    -   AI-generated multiple-choice questions from study material
-   History Tracking
    -   User activity stored in Firestore under users/{uid}/history

Navigation and User Interface - Modern UI built with Jetpack Compose and
Material 3 - Drawer navigation with bottom navigation integration -
Screens include Home, Summary, Flashcards, Quiz, Profile, About,
Features, Team

------------------------------------------------------------------------

Technology Stack

-   Kotlin
-   Jetpack Compose (Material 3)
-   Navigation Compose
-   Firebase Authentication
-   Firebase Firestore
-   Google ML Kit Text Recognition (OCR)
-   Retrofit and OkHttp
-   Cohere API (text generation)

------------------------------------------------------------------------

Application Workflow

1.  The user uploads an image or enters text manually.
2.  OCR extracts text from the image using ML Kit.
3.  The application constructs a prompt for the AI model.
4.  A request is sent to the Cohere API using Retrofit.
5.  The generated output is displayed in the interface.
6.  The activity is recorded in Firestore for the user.

------------------------------------------------------------------------

Setup Instructions

Follow the steps below to run the project locally.

1.  Clone the Repository

git clone https://github.com/anasaljboor/StudyWizard.git cd StudyWizard

------------------------------------------------------------------------

2.  Open the Project in Android Studio

-   Use the latest stable version of Android Studio
-   Select Open Project
-   Choose the cloned StudyWizard folder
-   Wait for Gradle Sync to complete

------------------------------------------------------------------------

3.  Firebase Configuration (Required)

This project uses Firebase Authentication and Firestore.

1.  Go to the Firebase Console: https://console.firebase.google.com

2.  Create a new project.

3.  Add an Android App with the package name:

    com.example.studywizard

4.  Download the file:

    google-services.json

5.  Place it inside the project:

    StudyWizard/app/google-services.json

6.  Enable:

    -   Authentication → Sign-in Method → Email/Password
    -   Firestore Database → Create Database (Test Mode for development)

------------------------------------------------------------------------

4.  Configure Cohere API Key (Important)

Do not store API keys directly in source code.

Step 1 — Add API key to local.properties

COHERE_API_KEY=YOUR_API_KEY_HERE

Step 2 — Expose key to BuildConfig (app/build.gradle.kts)

android { defaultConfig { buildConfigField( “String”, “COHERE_API_KEY”,
“"${project.properties["COHERE_API_KEY"]}"” ) }

    buildFeatures {
        buildConfig = true
    }

}

Step 3 — Use the key in code

val apiKey = “Bearer” + BuildConfig.COHERE_API_KEY

If an API key was previously committed, rotate it in the Cohere
dashboard.

------------------------------------------------------------------------

5.  Permissions

Ensure the following permissions exist in AndroidManifest.xml:

If camera capture is added:

------------------------------------------------------------------------

6.  Run the Application

7.  Connect a physical Android device or start an emulator.

8.  Click Run in Android Studio.

9.  Create an account and test the features.

------------------------------------------------------------------------

7.  Build Requirements

-   Android Studio (latest stable)
-   JDK 17 or newer
-   Android SDK 24+
-   Internet connection (for Firebase and Cohere API)

------------------------------------------------------------------------

Author Anas Al Jboor
