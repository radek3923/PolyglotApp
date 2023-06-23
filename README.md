# PolyglotApp
Foreign Language Learning Mobile Application

## Description

PolyglotApp is a foreign language learning mobile application that provides users with interactive tools to enhance their language skills. The app offers features such as flashcards and quizzes, making the learning process enjoyable and engaging.

## Features

- Flashcards: The flashcards feature allows users to learn foreign language words and their translations. Users can swipe through the flashcards to navigate between different words, helping them practice pronunciation and learn meanings. The interactive nature of the flashcards makes it easier for users to memorize vocabulary and improve their language proficiency.

- Quizzes: PolyglotApp also includes quizzes to test users' language knowledge. In the quiz mode, users are presented with a definition or description in the language they are learning and have to choose the correct word out of four options. The quiz randomly selects words from the user's vocabulary and provides immediate feedback on the correctness of the answers. 

## Language Selection
Upon starting the app, users are prompted to select the languages they already know and the language they want to learn. This initial selection enables the app to personalize the learning experience according to the user's language preferences.

## Word Management
PolyglotApp allows users to manage their vocabulary by adding and tracking words they have learned. The app provides a dedicated fragment, "AllWordsFragment," where users can view a list of all the words they have learned and those they are still working on. Users can update the learning status of words and easily delete entries when desired.

## GPS Localization
The app includes a feature that utilizes GPS localization to determine the user's current location. By obtaining the latitude and longitude coordinates, the app retrieves the corresponding city name using reverse geocoding. This information can be useful for tailoring language learning content based on the user's location or providing location-specific language exercises.


## Technical Details
PolyglotApp utilizes the following key technologies and architectural components:

- `Android Architecture Components`: The app follows the recommended architecture guidelines using ViewModel and LiveData for data management and separation of concerns.

- `Room Database`: The app incorporates the Room persistence library for local storage and management of word data. It includes an abstract class AppDatabase that extends RoomDatabase and provides access to the WordDao interface for performing database operations.

- `Dagger Hilt`: ependency injection is implemented using Dagger Hilt, allowing for efficient and modular development of the application.

- `Location Services`: The app utilizes the Android LocationManager and LocationListener to obtain the user's current location coordinates. It then uses Geocoder to retrieve the corresponding city name through reverse geocoding.

- `Navigation Component`: The app utilizes the Navigation Component provided by the Android Jetpack library to handle navigation between different fragments and ensure a consistent user experience.

## APIs
PolyglotApp utilizes the following APIs to enhance its language learning features:

- `DeepL API`: Provides translation services and language-related functionalities.
- `RandomWord API`:: Generates random words for language exercises.
- `WordsDefinitions API`:: Offers word definitions and related information.
These APIs are integrated into the application to provide language data, perform text translations, fetch random words, and retrieve comprehensive word definitions.

Please refer to the API documentation for more details on their specific usage and requirements.

## Installation

To install PolyglotApp, follow these steps:

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Build and run the app on an Android device or emulator.

Note: Ensure that the necessary Android permissions are granted for the app to function correctly, such as location access if the GPS localization feature is enabled.
