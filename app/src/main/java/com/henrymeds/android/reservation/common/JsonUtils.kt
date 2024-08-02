package com.henrymeds.android.reservation.common

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.henrymeds.android.reservation.data.Client
import com.henrymeds.android.reservation.data.Reservation
import com.henrymeds.android.reservation.data.Schedule
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter


object JsonUtils {

    private const val SCHEDULES_FILE_NAME = "schedules.json"
    private const val RESERVATIONS_FILE_NAME = "reservations.json"

    fun readSchedulesJsonFromFile(application: Application): List<Schedule> {
        val file = File(application.filesDir, SCHEDULES_FILE_NAME)
        if (!file.exists()) {
            return emptyList()
        }
        val json = file.bufferedReader().use { it.readText() }
        val listType = object : TypeToken<List<Schedule>>() {}.type
        return Gson().fromJson(json, listType)
    }

    fun writeScheduleJsonToFile(application: Application, schedules: List<Schedule>) {
        val file = File(application.filesDir, SCHEDULES_FILE_NAME)
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        val json = Gson().toJson(schedules)
        try {
            bufferedWriter.write(json)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            bufferedWriter.close()
            fileWriter.close()
        }
    }

    fun readReservationsJsonFromFile(application: Application): List<Reservation> {
        val file = File(application.filesDir, RESERVATIONS_FILE_NAME)
        if (!file.exists()) {
            return emptyList()
        }
        val json = file.bufferedReader().use { it.readText() }
        val listType = object : TypeToken<List<Reservation>>() {}.type
        return Gson().fromJson(json, listType)
    }

    fun writeReservationJsonToFile(application: Application, reservations: List<Reservation>) {
        val file = File(application.filesDir, RESERVATIONS_FILE_NAME)
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        val json = Gson().toJson(reservations)
        try {
            bufferedWriter.write(json)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            bufferedWriter.close()
            fileWriter.close()
        }
    }

    fun getClientId(application: Application): String {
        val jsonString = application.assets.open("client.json").bufferedReader().use { it.readText() }
        val listType = object : TypeToken<Client>() {}.type
        val client = Gson().fromJson<Client>(jsonString, listType)
        return client.id
    }
}