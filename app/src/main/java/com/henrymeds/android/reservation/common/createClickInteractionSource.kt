package com.henrymeds.android.reservation.common

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

fun createClickInteractionSource(onClick: () -> Unit): MutableInteractionSource {
    return object : MutableInteractionSource {
        override val interactions = MutableSharedFlow<Interaction>(
            extraBufferCapacity = 16,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )

        override suspend fun emit(interaction: Interaction) {
            if (interaction is PressInteraction.Release) {
                onClick()
            }
            interactions.emit(interaction)
        }

        override fun tryEmit(interaction: Interaction): Boolean {
            return interactions.tryEmit(interaction)
        }
    }
}