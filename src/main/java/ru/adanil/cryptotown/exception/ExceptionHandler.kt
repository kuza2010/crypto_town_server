package ru.adanil.cryptotown.exception

import ru.adanil.cryptotown.model.core.CoreErrorResponse
import ru.adanil.cryptotown.model.core.CoreStatus
import ru.adanil.cryptotown.utils.GsonProvider
import spark.Route
import spark.Spark.*

object ExceptionHandler {

    private val objectMapper = GsonProvider.provideGson()

    // <---   custom 404 handler  --->
    private var notFound = Route { request, response ->
        val core = CoreErrorResponse(CoreStatus.NOK, "Requested path '${request.pathInfo()}' was not found!")
        response.type("application/json")
        objectMapper.toJson(core)
    }

    // <---   custom 500 handler  --->
    private var internalServerError = Route { _, response ->
        val core = CoreErrorResponse(CoreStatus.NOK, "Oops, something went wrong. Internal server error.")
        response.type("application/json")
        objectMapper.toJson(core)
    }

    fun init() {
        notFound(notFound)
        internalServerError(internalServerError)

        exception(FileException::class.java) { exception, _, response ->
            val core = CoreErrorResponse(CoreStatus.NOK, exception.localizedMessage)

            response.status(400)
            response.body(objectMapper.toJson(core))
            response.type("application/json")
        }

        exception(ScriptExecutionException::class.java) { exception, _, response ->
            val core = CoreErrorResponse(CoreStatus.NOK, exception.message)

            response.status(500)
            response.body(objectMapper.toJson(core))
            response.type("application/json")
        }
    }
}