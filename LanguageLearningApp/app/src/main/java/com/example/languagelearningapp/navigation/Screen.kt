package com.example.languagelearningapp.navigation

const val HOME_ROUTE = "home"
const val COLLECTION_ROUTE = "collections"
const val DOCUMENT_ROUTE = "documents"
const val CAMERA_ROUTE = "camera"
const val PRACTICE_ROUTE = "practice"
const val COLLECTION_DETAILED_ROUTE = "collection_detailed"

sealed class Screen(val route: String) {
    object HomeScreen: Screen(HOME_ROUTE)
    object CollectionScreen: Screen(COLLECTION_ROUTE)
    object DocumentScreen: Screen(DOCUMENT_ROUTE)
    object CameraScreen: Screen(CAMERA_ROUTE)
    object PracticeScreen: Screen(PRACTICE_ROUTE)
    object CollectionDetailedScreen: Screen(COLLECTION_DETAILED_ROUTE)
}