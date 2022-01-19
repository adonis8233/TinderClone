package com.example.mytinder.activities

import com.google.firebase.database.DatabaseReference

interface TinderCallback {

    fun onSignout()
    fun onGetUserId(): String
    fun getUserDatabase(): DatabaseReference
    fun profileComplete()
    fun startActivityForPhoto()
    fun getChatDatabase(): DatabaseReference
}