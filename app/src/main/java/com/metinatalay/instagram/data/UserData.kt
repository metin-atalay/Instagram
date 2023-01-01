package com.metinatalay.instagram.data

data class UserData(
    val userId: String? = null,
    val name: String? = null,
    val username: String? = null,
    val imageUrl: String? = null,
    var bio: String? = null,
    var following: List<String>? = null
) {
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "username" to username,
        "imageUrl" to imageUrl,
        "bio" to bio,
        "following" to following
    )
}