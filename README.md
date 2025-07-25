# MatchMate

## Project Description

MatchMate is a matrimonial-style Android app that displays user match cards similar to Shaadi.com. The app fetches data from an API, allows Accept/Decline actions, stores user decisions locally using Room, and supports offline mode.

---

## Project Setup Instructions

1. Clone the repository.
2. Open the project in Android Studio.
3. Build the project and run on an emulator or physical device.

---

## Libraries Used

- Retrofit – for API communication.
- Glide – for loading user profile images.
- Room – for local database storage and offline support.
- LiveData – for observing data changes between ViewModel and UI.

---

## Architecture

The app uses the MVVM (Model-View-ViewModel) architecture:

- Model: UserProfile data class, Room DAO, Retrofit API.
- ViewModel: Handles business logic and exposes LiveData.
- View: MainActivity observes ViewModel and updates UI.

This pattern ensures a clean separation of concerns and easier maintenance.

---

## Justification for Added Fields

Two fields were added to each profile:

- Education: Important for users to assess compatibility based on academic background.
- Religion: Commonly used in matrimonial apps as a key filter criteria.

These were manually inserted into the parsed API response.

---

## Match Score Logic

Match score is calculated out of 100 based on the following:

- Age Score (max 50): Based on proximity to the preferred age (28). Subtract 2 points per year difference.
- City Score (max 50):
  - India: 50
  - Germany: 40
  - Canada, Australia: 30
  - Brazil: 0
  - Others: 10

If the profile's gender is "male", score is 0.

---

## Offline and Error Handling Strategy

- All profiles and user actions are stored in Room database.
- On app launch, data is fetched from API and stored locally.
- The app simulates a flaky network with 30% failure.
- Errors are handled gracefully with retries and partial data loading.
- If offline, the app continues to function using the local database.

---

## Design Constraint Response

If profile images cannot be shown due to legal reasons:

- The app displays a placeholder with initials of the user’s name.
- Layout was updated to ensure proper alignment without images.

---

## Reflection

If given more time, the following features would be implemented:

- User Profile section to allow users to create their own profile.
- Preferences screen to define matching criteria like age, religion, education.
- Filter functionality to sort or limit visible matches based on preferences.

---

## Project Structure
 
      ├── app
      │   └── src
      │       ├── main
      │       │   ├── AndroidManifest.xml
      │       │   ├── java
      │       │   │   └── com
      │       │   │       └── shreyash
      │       │   │           └── matchmate
      │       │   │               ├── db
      │       │   │               │   ├── AppDatabase.kt
      │       │   │               │   └── UserProfileDao.kt
      │       │   │               ├── model
      │       │   │               │   └── UserProfile.kt
      │       │   │               ├── network
      │       │   │               │   ├── ApiResponse.kt
      │       │   │               │   └── ApiService.kt
      │       │   │               ├── repository
      │       │   │               │   └── UserRepository.kt
      │       │   │               ├── ui
      │       │   │               │   ├── MainActivity.kt
      │       │   │               │   └── MatchAdapter.kt
      │       │   │               └── viewmodel
      │       │   │                   └── UserViewModel.kt



## Screenshots

<div align="center">
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/shreyashp47/MatchMate/blob/main/ss/ss1.png" alt="Tasks Screen" width="200"/>
      <br><b>Tasks Overview</b>
    </td>
    <td align="center">
      <img src="https://github.com/shreyashp47/MatchMate/blob/main/ss/ss2.png" alt="Add Task" width="200"/>
      <br><b>Add/Edit Task</b>
    </td>
    
  </tr>
</table>
</div>
