package ru.adanil.cryptotown.utils.scripts

import ru.adanil.cryptotown.model.core.file.FileStatus

data class ScriptResponse(
    val imageName: String,
    val probability: String,
    val result: FileStatus
)