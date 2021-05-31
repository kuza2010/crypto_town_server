package ru.adanil.cryptotown.model.core

import java.time.LocalDateTime

data class CoreResponse(
    val status: CoreStatus,
    val message: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
