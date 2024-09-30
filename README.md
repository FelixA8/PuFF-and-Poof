# PuFF-and-Poof Store App

**PuFF-and-Poof** is a simple store app focused on selling dolls. This project is built to explore the Kotlin programming language and its functionality, including REST API integration, Google Maps API, SQLite database management, and Kotlin basics. The app aims to demonstrate how to build an Android app from scratch while utilizing essential Android and Kotlin libraries.

## Features

- **Doll Catalog:** Browse and view a list of available dolls for sale.
- **Google Maps Integration:** View the store location using Google Maps API.
- **SQLite Integration:** Store and retrieve data using the local SQLite database.
- **REST API Integration:** Fetch and display data from an online REST API.
- **User-Friendly Interface:** Simple UI designed to enhance the shopping experience.

## Technology Stack

- **Kotlin:** Main programming language used for Android app development.
- **REST API:** For fetching data related to dolls, prices, and store information.
- **Google Maps API:** Integrated for showing the store location.
- **SQLite:** Local database for saving user and product data.
- **Android SDK:** The app is developed for Android devices using the Android SDK.

## Prerequisites

Before running the project, ensure you have the following:
- Android Studio installed.
- An Android device or emulator set up.
- Basic understanding of Kotlin and Android app development.
- API keys for Google Maps and the REST API (if required).

### Installation
1. Clone the repository:

   ```bash
   git clone https://github.com/FelixA8/PuFF-and-Poof.git
   cd PuFF-and-Poof

2. Open in Android Studio:
    - Click on File > Open and select the PuFF-and-Poof project folder.
    - Let Android Studio sync the Gradle files and dependencies.

3. Set Up API Keys:
    - Get a Google Maps API key from the Google Cloud Console.
    - Set up your REST API URL in the Kotlin api/ files if the app requires it.
    - Add your API keys to the local.properties or in gradle.properties (or as instructed by the project setup).

4. Build and Run
    - Connect your Android device or set up an emulator.
    - Click on the Run button in Android Studio, and the app should start on your device.

## Project Structure

    ```bash
    app/                      # Main Android project folder
    ├── manifests/            # Contains AndroidManifest.xml file for app configuration
    ├── java/                 # Kotlin source code and logic
    │   ├── activities/       # UI components like activities
    │   ├── adapters/         # Contains model classes
    │   ├── data/             # Contains SQLite database helper and other queries
    │   └── fragments/        # UI components for the home activity
    │   └── MainActivity.kt   # Starting File
    └── res/                  # Resources such as layouts, drawables, and values

## Usage
Once installed, you can:
- Browse the doll catalog.
- Add items to your cart.
- Check out the store location using Google Maps.
- Store and retrieve information locally with SQLite.

## Learning Focus
This project focuses on:
- **Kotlin Basics:** Understanding Kotlin syntax and basic structures like functions, classes, and objects.
- **Networking with REST API:** Learning how to fetch data using APIs in Android with Kotlin.
- **Google Maps API:** Implementing maps in Android apps.
- **SQLite Integration:** Saving and querying data locally using SQLite in Kotlin.

<p align="center">Built with ❤️ and Kotlin!</p>
