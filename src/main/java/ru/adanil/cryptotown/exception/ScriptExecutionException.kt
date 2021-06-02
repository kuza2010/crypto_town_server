package ru.adanil.cryptotown.exception

class ScriptExecutionException(
    msg: String = "Something went wrong during script processing"
) : RuntimeException(msg) {

}
