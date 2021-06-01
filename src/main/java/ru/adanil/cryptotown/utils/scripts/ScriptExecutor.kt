package ru.adanil.cryptotown.utils.scripts

import com.google.gson.Gson
import org.slf4j.LoggerFactory
import ru.adanil.cryptotown.exception.ScriptExecutionException
import ru.adanil.cryptotown.utils.GsonProvider
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class ScriptExecutor(
    private val objectMapper: Gson = GsonProvider.provideGson(),
    private val interpreter: String = "python"
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
        val mappedArgs = args.flatMap { it.argument }.toTypedArray()
        val processBuilder = defaultProcessBuilder(scriptPath, *mappedArgs)

        val process = processBuilder.start()
        val result = InputStreamReader(process.inputStream).use { it.readText() }

        return if (process.waitFor(10, TimeUnit.SECONDS) && process.exitValue() == SUCCESS) {
            log.debug("Script result: $result")
            objectMapper.fromJson(result, Array<ScriptResponse>::class.java).first()
        } else {
            log.error("Script execution have failed (timeout or exit code), with message: $result")
            throw ScriptExecutionException()
        }
    }

    private fun defaultProcessBuilder(
        scriptPath: String,
        vararg arg: String
    ): ProcessBuilder {
        val processBuilder = ProcessBuilder(interpreter, scriptPath, *arg)
        processBuilder.redirectErrorStream(true)
        return processBuilder
    }

}