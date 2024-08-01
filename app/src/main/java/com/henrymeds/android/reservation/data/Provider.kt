package com.henrymeds.android.reservation.data

data class Provider(
    val id: String,
    val name: String,
    val schedule: List<Schedule>
)