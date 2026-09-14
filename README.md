# 8103965Assignment2

An Android application built for the NIT3213 final assignment. It authenticates against
the `nit3213api`, displays a list of entities on a Dashboard screen, and shows full details
for a selected entity.

## Screens

1. **Login** — enter your student username/password, POSTs to the auth endpoint, and on
   success navigates to the Dashboard with the returned `keypass`.
2. **Dashboard** — GETs `/dashboard/{keypass}` and shows every entity in a `RecyclerView`
   (summary only — the `description` field is intentionally left out of this view). Tapping
   an item opens the Details screen.
3. **Details** — shows every field for the selected entity, including `description`.

## Architecture

- **MVVM**: `LoginViewModel` / `DashboardViewModel` expose UI state via `LiveData<Resource<T>>`.
- **Resource<T>** (`util/Resource.kt`): a small sealed class (`Loading` / `Success` / `Error`)
  used to represent network state cleanly, without try/catch in the UI layer.
- **Repository layer**: `AuthRepository` and `DashboardRepository` wrap `ApiService` calls
  and translate exceptions into `Resource.Error`.
- **Networking**: Retrofit + Moshi (`converter-moshi`, `moshi-kotlin` reflection adapter) +
  OkHttp logging interceptor.
- **Dependency Injection**: Hilt (`@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`,
  `@Inject` constructors, and a `NetworkModule` that provides Moshi/OkHttp/Retrofit/ApiService
  as singletons).
- **Generic entity model**: Dashboard entities are parsed as `List<Map<String, Any>>` rather
  than a fixed data class, because the actual property names (e.g. `name`, `location`,
  `price`) depend on the topic tied to each student's `keypass`. Only `description` is
  treated specially (excluded from the summary list, shown in Details). This means the app
  works regardless of which dataset your `keypass` resolves to.
- **View Binding** is used throughout instead of `findViewById`.

## Project structure

```
app/src/main/java/com/example/assignment2/
├── MainApplication.kt              # @HiltAndroidApp entry point
├── data/
│   ├── model/                      # LoginRequest, LoginResponse, DashboardResponse
│   ├── remote/ApiService.kt        # Retrofit interface
│   └── repository/                 # AuthRepository, DashboardRepository
├── di/NetworkModule.kt             # Hilt module providing networking singletons
├── ui/
│   ├── login/                      # LoginActivity, LoginViewModel
│   ├── dashboard/                  # DashboardActivity, DashboardViewModel, EntityAdapter
│   └── details/                    # DetailsActivity
└── util/Resource.kt

app/src/test/java/com/example/assignment2/
├── ui/login/LoginViewModelTest.kt
└── ui/dashboard/DashboardViewModelTest.kt
```

## Before you build: configure your campus and credentials

The API base URL has already been updated to the new host, but the **auth endpoint path**
depends on your class/campus, and the login credentials depend on you.

1. Open `app/src/main/java/com/example/assignment2/data/remote/ApiService.kt` and change:
   ```kotlin
   @POST("footscray/auth")
   ```
   to `"sydney/auth"` or `"br/auth"` if that matches your class.

2. Log in using your own student ID (no leading `s`) as the username and your first name
   (case-sensitive) as the password, e.g.:
   ```
   Username: 8103965
   Password: Aaron
   ```
   These are typed into the Login screen at runtime — they are **not** hardcoded anywhere
   in the app.

## How to build and run

1. **Requirements**: Android Studio (Ladybug/Koala or newer), JDK 17, an emulator or device
   running API 24+.
2. Open the project root folder (`8103965Assignment2`) in Android Studio — it will show up
   as project name **8103965Assignment2**.
3. Let Gradle sync. If prompted to create the Gradle wrapper, accept it (or run
   `gradle wrapper` from the project root once if you have Gradle installed locally).
4. Update the auth endpoint path in `ApiService.kt` as described above if needed.
5. Run the `app` configuration on an emulator/device.
6. Log in with your student ID and first name, browse the Dashboard, and tap any item to
   see its Details.

## Running the unit tests

From Android Studio: right-click `app/src/test` → **Run 'Tests in java'**.

From the command line (after generating the Gradle wrapper):
```
./gradlew testDebugUnitTest
```

Tests cover:
- `LoginViewModelTest` — blank-input validation, successful login (keypass emitted),
  and failed login (error message emitted), using MockK to fake `AuthRepository`.
- `DashboardViewModelTest` — successful dashboard load and failed dashboard load, using
  MockK to fake `DashboardRepository`.

## Dependencies

Declared in `app/build.gradle.kts`:

```kotlin
// Retrofit
implementation("com.squareup.retrofit2:retrofit:3.0.0")
implementation("com.squareup.retrofit2:converter-moshi:3.0.0")
implementation("com.squareup.moshi:moshi-kotlin:1.15.2")
implementation("com.squareup.okhttp3:logging-interceptor:5.5.0")

// Hilt
implementation("com.google.dagger:hilt-android:2.52")
ksp("com.google.dagger:hilt-android-compiler:2.52")
```

Plugin declarations:
- Project-level `build.gradle.kts`: `com.google.devtools.ksp` version `2.0.21-1.0.26` (apply false),
  `com.google.dagger.hilt.android` version `2.52` (apply false).
- App-level `app/build.gradle.kts`: `com.google.devtools.ksp` and `com.google.dagger.hilt.android`.

## Git usage

This project should be committed with an incremental, meaningful history, e.g.:
1. `Initial project scaffold with Gradle and Hilt setup`
2. `Add data models and Retrofit ApiService`
3. `Implement AuthRepository and LoginViewModel with unit tests`
4. `Build Login screen UI and navigation`
5. `Implement DashboardRepository, DashboardViewModel and RecyclerView adapter`
6. `Build Dashboard screen UI and navigation to Details`
7. `Implement Details screen`
8. `Add unit tests for DashboardViewModel`
9. `Add README and finalise project`

Push to a remote (GitHub/GitLab) and submit the repository link as required.

## Known limitations / notes

- Launcher icon assets are simple placeholder vector drawables (adaptive icon) rather than
  designed artwork — replace via Android Studio's Image Asset Studio if you'd like a custom icon.
- Error handling distinguishes between HTTP errors (e.g. bad credentials) and network/IO
  errors (e.g. no internet) for clearer user-facing messages.
