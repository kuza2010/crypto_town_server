package ru.adanil.cryptotown.utils

import com.google.gson.Gson
import org.slf4j.LoggerFactory
import ru.adanil.cryptotown.exception.ScriptExecutionException
import ru.adanil.cryptotown.model.core.file.FileStatus
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class ScriptExecutor(
    private val objectMapper: Gson = GsonProvider.provideGson()
) {

    companion object {
        const val SUCCESS = 0
    }


    private val log = LoggerFactory.getLogger(ScriptExecutor::class.java)

    @Throws(Exception::class)
    fun execute(
        scriptPath: String,
        args: List<ScriptArgument> = emptyList()
    ): ScriptResponse {
        val mappedArgs = args.flatMap { listOf(it.argument.first, it.argument.second) }
        val processBuilder = defaultProcessBuilder(scriptPath, *(mappedArgs.toTypedArray()))

        val process = processBuilder.start()
        val result = InputStreamReader(process.inputStream).use { it.readText() }

        return if (process.waitFor(10_000, TimeUnit.SECONDS) && process.exitValue() == SUCCESS) {
            log.debug("Script result: $result")
            objectMapper.fromJson(result, Array<ScriptResponse>::class.java).first()
        } else {
            log.error("Script execution have failed, with message: $result")
            throw ScriptExecutionException()
        }
    }

    private fun defaultProcessBuilder(scriptPath: String, vararg arg: String): ProcessBuilder {
        val processBuilder = ProcessBuilder("python", scriptPath, *arg)
        processBuilder.redirectErrorStream(true)
        return processBuilder
    }


    data class ErrorScriptResponse(val errorMessage: String)
    data class ScriptResponse(val imageName: String, val probability: String, val result: FileStatus)
}

data class ScriptArgument(
    val key: String,
    val value: String
) {
    val argument: Pair<String, String> by lazy { key to value }

    init {
        if (key.isEmpty()) {
            throw IllegalArgumentException("key can not be empty")
        }
    }
}
