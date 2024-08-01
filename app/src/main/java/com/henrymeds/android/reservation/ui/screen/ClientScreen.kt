package com.henrymeds.android.reservation.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.henrymeds.android.reservation.data.TimeSlot
import com.henrymeds.android.reservation.viewmodel.MainActivityViewModel
import kotlinx.coroutines.Dispatchers

@Composable
fun ClientScreen(
    navHostController: NavHostController,
    viewModel: MainActivityViewModel
) {

    val context = LocalContext.current
    var selectedTimeSlot by remember { mutableStateOf<TimeSlot?>(null) }
    var showReserveSlotButton by remember { mutableStateOf(false) }
    var showConfirmButton by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        val groupedTimeSlots = viewModel.groupTimeSlotsByDate()
        groupedTimeSlots.forEach { (date, timeSlots) ->
            item {
                Text(
                    text = date,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            items(timeSlots) { timeSlot ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    onClick = {
                        selectedTimeSlot = timeSlot
                        showReserveSlotButton = true
                    }
                ) {
                    Text(
                        text = timeSlot.time,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }

    if (showReserveSlotButton) {
        selectedTimeSlot?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.reserveSlot(
                            date = it.date,
                            time = it.time,
                            providerId = it.providerId
                        )
                        Toast.makeText(context, "Reserved slot at ${it.date} ${it.time}", Toast.LENGTH_LONG).show()
                        showReserveSlotButton = false
                        showConfirmButton = true
                    }
                ) {
                    Text("Reserve Slot at ${it.date} ${it.time}")
                }
            }
        }
    }

    if (showConfirmButton) {
        selectedTimeSlot?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.confirmReservation(it.date, it.time, it.providerId)
                        Toast.makeText(context, "Confirmed reservation", Toast.LENGTH_LONG).show()
                        navHostController.navigate("home")
                    }
                ) {
                    Text("Confirm reservation at ${it.date} ${it.time}")
                }
            }
        }
    }
}