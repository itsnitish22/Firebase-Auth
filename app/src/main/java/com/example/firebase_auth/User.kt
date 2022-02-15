package com.example.firebase_auth

import android.provider.ContactsContract

data class User(
    var fullName: String? = null,
    var email: String? = null,
    var password: String? = null
)
