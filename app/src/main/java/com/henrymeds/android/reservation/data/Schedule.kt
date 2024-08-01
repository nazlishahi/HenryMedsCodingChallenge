package com.henrymeds.android.reservation.data

import java.util.Date

data class Schedule(
    val providerId: String,
    val date: Date,
    val startTime: String,
    val endTime: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Schedule

        if (providerId != other.providerId) return false
        if (date != other.date) return false
        if (startTime != other.startTime) return false
        if (endTime != other.endTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = providerId.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + startTime.hashCode()
        result = 31 * result + endTime.hashCode()
        return result
    }
}