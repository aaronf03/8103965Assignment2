package com.example.assignment2

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class annotated with @HiltAndroidApp so Hilt can generate
 * the dependency graph (SingletonComponent) used throughout the app.
 */
@HiltAndroidApp
class MainApplication : Application()
