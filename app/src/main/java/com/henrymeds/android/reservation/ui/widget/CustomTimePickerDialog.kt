package com.henrymeds.android.reservation.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.henrymeds.android.reservation.common.createClickInteractionSource
import java.util.Locale

@Composable
fun CustomTimePickerDialog(
    onDismissRequest: () -> Unit,
    onTimeSelected: (Int, Int, String) -> Unit
) {
    val hours = (1..12).toList()
    val minutes = listOf(0, 30)
    val periods = listOf("AM", "PM")

    var selectedHour by remember { mutableIntStateOf(hours.first()) }
    var selectedMinute by remember { mutableIntStateOf(minutes.first()) }
    var selectedPeriod by remember { mutableStateOf(periods.first()) }

    var expandedHour by remember { mutableStateOf(false) }
    var expandedMinute by remember { mutableStateOf(false) }
    var expandedPeriod by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Time") },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)) {
                    Text(text = "Hour")
                    Box {
                        OutlinedTextField(
                            value = String.format(Locale.getDefault(), "%02d", selectedHour),
                            onValueChange = {},
                            readOnly = true,
                            interactionSource = createClickInteractionSource { expandedHour = true }
                        )

                        if (expandedHour) {
                            DropdownMenu(
                                expanded = expandedHour,
                                onDismissRequest = { expandedHour = false }
                            ) {
                                hours.forEach { hour ->
                                    DropdownMenuItem(
                                        text = { Text(String.format(Locale.getDefault(), "%02d", hour)) },
                                        onClick = {
                                            selectedHour = hour
                                            expandedHour = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)) {
                    Text(text = "Minute")
                    Box {
                        OutlinedTextField(
                            value = String.format(Locale.getDefault(), "%02d", selectedMinute),
                            onValueChange = {},
                            readOnly = true,
                            interactionSource = createClickInteractionSource { expandedMinute = true }
                        )
                        DropdownMenu(
                            expanded = expandedMinute,
                            onDismissRequest = { expandedMinute = false }
                        ) {
                            minutes.forEach { minute ->
                                DropdownMenuItem(
                                    text = { Text(String.format(Locale.getDefault(), "%02d", minute)) },
                                    onClick = {
                                        selectedMinute = minute
                                        expandedMinute = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Text(text = "AM/PM")
                    Box {
                        OutlinedTextField(
                            value = selectedPeriod,
                            onValueChange = {},
                            readOnly = true,
                            interactionSource = createClickInteractionSource { expandedPeriod = true }
                        )
                        if (expandedPeriod) {
                            DropdownMenu(
                                expanded = expandedPeriod,
                                onDismissRequest = { expandedPeriod = false }
                            ) {
                                periods.forEach { period ->
                                    DropdownMenuItem(
                                        text = { Text(period) },
                                        onClick = {
                                            selectedPeriod = period
                                            expandedPeriod = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onTimeSelected(selectedHour, selectedMinute, selectedPeriod) }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}