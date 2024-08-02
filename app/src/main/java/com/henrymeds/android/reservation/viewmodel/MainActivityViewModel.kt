package com.henrymeds.android.reservation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henrymeds.android.reservation.common.JsonUtils
import com.henrymeds.android.reservation.data.Reservation
import com.henrymeds.android.reservation.data.Schedule
import com.henrymeds.android.reservation.data.TimeSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject constructor(private val application: Application) : ViewModel() {

    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())

    private val _reservations = MutableStateFlow<List<Reservation>>(emptyList())

    private val _uiMessageState = MutableStateFlow<String?>(null)
    val uiMessageState: StateFlow<String?> get() = _uiMessageState

    private val clientId by lazy {
        JsonUtils.getClientId(application)
    }

    fun loadData() {
        viewModelScope.launch {
            loadSchedules()
            loadReservations()
        }
    }

    private fun loadSchedules() {
        viewModelScope.launch {
            val scheduleList = JsonUtils.readSchedulesJsonFromFile(application)
            _schedules.value =
                scheduleList
                    .sortedBy { it.date }
                    .filter { it.date.time >= System.currentTimeMillis() }
        }
    }

    private fun loadReservations() {
        viewModelScope.launch {
            val reservations = JsonUtils.readReservationsJsonFromFile(application)
            _reservations.value = reservations
        }
    }

    fun setProviderSchedule(providerId: String, date: Date?, startTime: String, endTime: String) {
        viewModelScope.launch {
            date?.let {
                val schedule = Schedule(
                    providerId = providerId,
                    date = it,
                    startTime = startTime,
                    endTime = endTime
                )
                val updatedSchedules = _schedules.value.toMutableList()
                if (updatedSchedules.contains(schedule)) {
                    _uiMessageState.value = "Schedule already added"
                } else {
                    updatedSchedules.add(schedule)
                    val shiftData = "${schedule.date} ${schedule.startTime} - ${schedule.endTime}"
                    _uiMessageState.value = "Successfully added shift $shiftData"
                    JsonUtils.writeScheduleJsonToFile(application, updatedSchedules)
                }

                _schedules.value = updatedSchedules
            }
        }
    }

    fun groupTimeSlotsByDate(): Map<String, List<TimeSlot>> {
        val schedules = _schedules.value
        return schedules.flatMap { createTimeSlots(it) }.groupBy { it.date }
    }

    private fun createTimeSlots(schedule: Schedule): List<TimeSlot> {
        val slots = mutableListOf<TimeSlot>()
        val dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

        val startDate = schedule.date
        val startDateValue = dateFormatter.format(startDate)
        val startTimeValue = timeFormatter.parse(schedule.startTime) ?: return emptyList()
        val endTime = timeFormatter.parse(schedule.endTime) ?: return emptyList()

        val startCalendar = Calendar.getInstance().apply {
            time = startDate
            val tempCalendar = Calendar.getInstance().apply { time = startTimeValue }
            set(Calendar.HOUR_OF_DAY, tempCalendar.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, tempCalendar.get(Calendar.MINUTE))
        }

        val endCalendar = Calendar.getInstance().apply {
            time = startDate
            val tempCalendar = Calendar.getInstance().apply { time = endTime }
            set(Calendar.HOUR_OF_DAY, tempCalendar.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, tempCalendar.get(Calendar.MINUTE))
        }

        val reservations = _reservations.value

        while (startCalendar.time.before(endCalendar.time)) {
            val slotStartTime = timeFormatter.format(startCalendar.time)
            if (reservations.isEmpty()) {
                slots.add(TimeSlot(dateFormatter.format(startCalendar.time), timeFormatter.format(startCalendar.time), schedule.providerId))
                startCalendar.add(Calendar.MINUTE, 15)
            } else {
                val index = reservations.indexOfFirst {
                    it.date == startDateValue && it.time == slotStartTime && it.providerId == schedule.providerId
                }
                val timeSlotDate = dateFormatter.format(startCalendar.time)
                val timeSlotStartTime = timeFormatter.format(startCalendar.time)
                val providerId = schedule.providerId
                if (index >= 0) {
                    val reservation = reservations[index]
                    if (timeSlotDate != reservation.date || timeSlotStartTime != reservation.time || providerId != reservation.providerId) {
                        slots.add(TimeSlot(timeSlotDate, timeSlotStartTime, providerId))
                    }
                } else {
                    slots.add(TimeSlot(timeSlotDate, timeSlotStartTime, providerId))
                }
                startCalendar.add(Calendar.MINUTE, 15)
            }
        }

        return slots
    }

    fun reserveSlot(date: String, time: String, providerId: String) {
        viewModelScope.launch {
            val newReservation = Reservation(
                date = date,
                time = time,
                clientId = clientId,
                providerId = providerId,
                confirmed = false,
                creationTimestamp = System.currentTimeMillis()
            )
            _reservations.value = _reservations.value.plus(newReservation)
            JsonUtils.writeReservationJsonToFile(application, _reservations.value)
        }
    }

    fun confirmReservation(date: String, time: String, providerId: String) {
        viewModelScope.launch {
            val updatedReservations = _reservations.value.toMutableList()

            val index = updatedReservations.indexOfFirst {
                it.date == date && it.time == time && it.clientId == clientId && it.providerId == providerId
            }

            if (index != -1) {
                val reservation = updatedReservations[index]
                val updatedReservation = reservation.copy(confirmed = true)
                updatedReservations[index] = updatedReservation
            }

            JsonUtils.writeReservationJsonToFile(application, updatedReservations)
        }
    }

    fun clearUiMessageState() {
        _uiMessageState.value = null
    }
}