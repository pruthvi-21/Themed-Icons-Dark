package com.jw.tip.dark.models

data class FirebaseRequestData(
    val userId: String,
    val data: List<FirebaseRequestItemData>,
    val timestamp: Long
)