package com.henrymeds.android.reservation.data

data class Reservation(
    val date: String,
    val time: String,
    val clientId: String,
    val providerId: String,
    var confirmed: Boolean,
    val creationTimestamp: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Reservation

        if (date != other.date) return false
        if (time != other.time) return false
        if (clientId != other.clientId) return false
        if (providerId != other.providerId) return false
        if (confirmed != other.confirmed) return false
        if (creationTimestamp != other.creationTimestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + clientId.hashCode()
        result = 31 * result + providerId.hashCode()
        result = 31 * result + confirmed.hashCode()
        result = 31 * result + creationTimestamp.hashCode()
        return result
    }
}
