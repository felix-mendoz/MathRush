package com.example.mathrush.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    val currentUserId: String
        get() = auth.currentUser?.uid ?: ""

    val currentUserEmail: String
        get() = auth.currentUser?.email ?: ""

    val currentUserName: String
        get() = auth.currentUser?.displayName ?: auth.currentUser?.email?.substringBefore("@") ?: "Jugador"

    suspend fun signIn(email: String, pass: String): Result<FirebaseUser?> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, pass: String, username: String): Result<FirebaseUser?> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            val user = result.user
            if (user != null) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username.ifEmpty { email.substringBefore("@") })
                    .build()
                user.updateProfile(profileUpdates).await()

                val userData = hashMapOf(
                    "username" to username,
                    "email" to email,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "totalScore" to 0,
                    "gamesPlayed" to 0,
                    "bestScore" to 0
                )

                db.collection("users")
                    .document(user.uid)
                    .set(userData)
                    .await()

                Result.success(user)

            }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
