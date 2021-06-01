package ru.adanil.cryptotown.utils.scripts

data class ScriptArgument(
    val key: String,
    val value: String
) {
    val argument: List<String> by lazy { listOf(key.trim(), value.trim()) }

    init {
        if (key.isEmpty()) {
            throw IllegalArgumentException("key can not be empty")
        }
    }
}