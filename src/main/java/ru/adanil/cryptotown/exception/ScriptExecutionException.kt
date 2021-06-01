package ru.adanil.cryptotown.exception

class ScriptExecutionException(
    msg: String = "Something went wrong during image processing"
) : RuntimeException(msg) {

}
