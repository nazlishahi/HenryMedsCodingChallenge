package com.henrymeds.android.reservation.ui.widget

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.henrymeds.android.reservation.common.createClickInteractionSource

@Composable
fun DateTimePickerTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label) },
        readOnly = true,
        singleLine = true,
        interactionSource = createClickInteractionSource { onClick() }
    )
}