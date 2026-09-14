# 8103965Assignment2

This is the Android app for the NIT3213 final assignment. It logs a user in against
the `nit3213api`, shows a list of entities on a Dashboard screen, and lets you tap into one
to see its full details.

## The three screens

1. **Login** — you type in your student username and password, the app sends them to the
   auth endpoint, and if they're correct you're taken to the Dashboard along with a
   `keypass` returned by the API.
2. **Dashboard** — fetches `/dashboard/{keypass}` and lists every entity in a scrollable
   `RecyclerView`. Each row shows a quick summary.
3. **Details** — shows everything about that one entity, including the `description`.

## How it's put together

- **MVVM**: `LoginViewModel` and `DashboardViewModel` hold the screen's state and expose it
  as `LiveData<Resource<T>>`, so the UI just observes and reacts.
- **`Resource<T>`** (`util/Resource.kt`) is a small sealed class with `Loading`, `Success`,
  and `Error` states. It keeps the loading/success/error handling consistent everywhere.
- **Repositories** (`AuthRepository`, `DashboardRepository`) sit between the ViewModels and
  the network layer. They call the API and turn any exceptions into a `Resource.Error`
  with a readable message.
- **Networking** is Retrofit + Moshi for parsing JSON, with an OkHttp logging interceptor so
  you can see requests & responses whilst debugging.
- **Dependency injection** is done with Hilt. `@HiltAndroidApp` on the Application class,
  `@AndroidEntryPoint` on the Activities, `@HiltViewModel` + `@Inject` constructors on the
  ViewModels and Repositories, and a `NetworkModule` that provides the shared Retrofit/OkHttp/
  Moshi instances.
- **Entities are parsed as `List<Map<String, Any>>`**, not a fixed data class. The reason is
  that the actual field names depend on which topic your `keypass` points to. The
  only field treated specially is `description`. This is hidden in the list and shown in Details.
- **View Binding** is used everywhere instead of `findViewById`.

## Where everything lives

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

## How to set it up

The base URL is already pointed at the right server, but two things depend on the user.

1. Open `app/src/main/java/com/example/assignment2/data/remote/ApiService.kt` and check this
   line:
   ```kotlin
   @POST("footscray/auth")
   ```
   Change `"footscray"` to `"sydney"` or `"br"` if that's your campus.

2. When you open the app, log in with your own student ID (no leading `s`) as the username,
   and your first name (case-sensitive) as the password — for example:
   ```
   Username: 8103965
   Password: Aaron
   ```

## Building and running it

1. You'll need Android Studio, JDK 17, and either an emulator or a
   physical device running Android 7.0 (API 24) or higher.
2. Open the project's root folder (`8103965Assignment2`) in Android Studio. It'll show up as
   project **8103965Assignment2**.
3. Let Gradle sync. If Android Studio offers to set up the Gradle wrapper, say yes.
4. Hit Run and pick a device/emulator.
5. Log in, have a look through the Dashboard, and tap an item to see its Details.

## Running the tests

The easiest way is to right-click `app/src/test` in Android Studio and choose **Run 'Tests in
java'**.

Or from a terminal, once the Gradle wrapper's set up:
```
./gradlew testDebugUnitTest
```

What's covered:
- `LoginViewModelTest` — checks that blank input is rejected, a successful login emits the
  keypass, and a failed login emits an error message. Uses MockK to fake `AuthRepository` so
  no real network calls happen.
- `DashboardViewModelTest` — checks a successful dashboard load and a failed one, same idea
  with `DashboardRepository` mocked out.

## Dependencies

These live in `app/build.gradle.kts`:

```kotlin
// Retrofit
implementation("com.squareup.retrofit2:retrofit:3.0.0")
implementation("com.squareup.retrofit2:converter-moshi:3.0.0")
implementation("com.squareup.moshi:moshi-kotlin:1.15.2")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Hilt
implementation("com.google.dagger:hilt-android:2.52")
ksp("com.google.dagger:hilt-android-compiler:2.52")
```

And the plugin declarations:
- Root `build.gradle.kts`: `com.google.devtools.ksp` (`2.0.21-1.0.26`, apply false) and
  `com.google.dagger.hilt.android` (`2.52`, apply false).
- `app/build.gradle.kts`: `com.google.devtools.ksp` and `com.google.dagger.hilt.android`.

One small note: the class originally shared `logging-interceptor:5.5.0`, but that version
pulls in a split `okhttp-android` artifact that needs `compileSdk 37`, which isn't
supported by the current Android Gradle Plugin yet. I dropped it to `4.12.0` instead, which
behaves identically for what we're using it for.

## Git history

I committed this in stages rather than all at once, roughly:
1. Intitial project — Gradle setup, Hilt, dependencies
2. Data models and the Retrofit API interface
3. Repositories, the `Resource` wrapper, and the Hilt network module
4. Login screen — UI, ViewModel, and its tests
5. Dashboard screen — UI, ViewModel, adapter, and its tests
6. Details screen, plus the toolbar/back-navigation
7. Remaining resources and the README

## A couple of things worth knowing

- Error messages distinguish between an actual bad login/HTTP error and a network problem
  (no internet, timeout), so the message you see should give an indication of what needs fixing.
- The API is hosted on Render's free tier, which goes to sleep when idle. Your very first
  login attempt can take slightly longer than usual, around 30–60 seconds.