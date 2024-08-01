package com.henrymeds.android.reservation.ui.screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.henrymeds.android.reservation.ui.widget.CustomTimePickerDialog
import com.henrymeds.android.reservation.ui.widget.DateTimePickerTextField
import com.henrymeds.android.reservation.viewmodel.MainActivityViewModel
import java.util.*

@Composable
fun ProviderScreen(mainActivityViewModel: MainActivityViewModel, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val uiMessageState by mainActivityViewModel.uiMessageState.collectAsState()

    var providerId by remember { mutableStateOf(TextFieldValue("")) }
    var dateText by remember { mutableStateOf(TextFieldValue("")) }
    var startTimeText by remember { mutableStateOf(TextFieldValue("")) }
    var endTimeText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDate by remember { mutableStateOf<Date?>(null) }

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            dateText = TextFieldValue("${month + 1}/$dayOfMonth/$year")
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            selectedDate = cal.time
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
    )

    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = providerId,
            onValueChange = { providerId = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter provider id") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        DateTimePickerTextField(
            value = dateText,
            onValueChange = { dateText = it },
            label = "Select date",
            modifier = Modifier.fillMaxWidth(),
            onClick = { datePickerDialog.show() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DateTimePickerTextField(
            value = startTimeText,
            onValueChange = { startTimeText = it },
            label = "Select start time",
            modifier = Modifier.fillMaxWidth(),
            onClick = { showStartTimePicker = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DateTimePickerTextField(
            value = endTimeText,
            onValueChange = { endTimeText = it },
            label = "Select end time",
            modifier = Modifier.fillMaxWidth(),
            onClick = { showEndTimePicker = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Save the provider's schedule
                mainActivityViewModel.setProviderSchedule(
                    providerId = providerId.text,
                    date = selectedDate,
                    startTime = startTimeText.text,
                    endTime = endTimeText.text
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedDate != null
                    && providerId.text.isNotEmpty()
                    && startTimeText.text.isNotEmpty()
                    && endTimeText.text.isNotEmpty()
        ) {
            Text("Save schedule")
        }
    }

    if (showStartTimePicker) {
        CustomTimePickerDialog(
            onDismissRequest = { showStartTimePicker = false },
            onTimeSelected = { hour, minute, period ->
                startTimeText = TextFieldValue(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, period))
                showStartTimePicker = false
            }
        )
    }

    if (showEndTimePicker) {
        CustomTimePickerDialog(
            onDismissRequest = { showEndTimePicker = false },
            onTimeSelected = { hour, minute, period ->
                endTimeText = TextFieldValue(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, period))
                showEndTimePicker = false
            }
        )
    }

    uiMessageState?.let {
        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        mainActivityViewModel.clearUiMessageState()
    }
}