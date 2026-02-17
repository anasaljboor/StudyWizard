# StudyWizard (Android) — OCR + AI Study Assistant

StudyWizard is an Android application developed with Kotlin and Jetpack Compose that transforms handwritten or printed study notes into structured learning materials.

The application enables users to capture or upload images of notes, extract text using on-device OCR, and generate summaries, flashcards, and quizzes using an AI text generation API (Cohere). User activity and generated content are stored securely per account using Firebase Firestore.

---

## Features

### Authentication
- Email and password authentication using Firebase Authentication
- User profile data stored in Firestore

### Study Tools
- **Text Summarization**
  - Input via manual text or image upload
  - OCR extraction with Google ML Kit
  - AI-generated summaries using Cohere API

- **Flashcard Generation**
  - Automatic question-answer pairs generated from extracted content

- **Quiz Generation**
  - AI-generated multiple-choice questions from study material

- **History Tracking**
  - User activity stored in Firestore under `users/{uid}/history`

### Navigation and User Interface
- Modern UI built with Jetpack Compose and Material 3
- Drawer navigation with bottom navigation integration
- Screens include:
  - Home
  - Summary
  - Flashcards
  - Quiz
  - Profile
  - About
  - Features
  - Team

---

## Technology Stack

- Kotlin
- Jetpack Compose (Material 3)
- Navigation Compose
- Firebase Authentication
- Firebase Firestore
- Google ML Kit Text Recognition (OCR)
- Retrofit and OkHttp
- Cohere API (text generation)

---

## Application Workflow

1. The user uploads an image or enters text manually.
2. OCR extracts text from the image using ML Kit.
3. The application constructs a prompt for the AI model.
4. A request is sent to the Cohere API using Retrofit.
5. The generated output is displayed in the interface.
6. The activity is recorded in Firestore for the user.

---

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/anasaljboor/StudyWizard.git
cd StudyWizard
