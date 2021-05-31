package ru.adanil.cryptotown.controller

import ru.adanil.cryptotown.utils.ConfigReader.properties
import spark.Request
import spark.Response
import spark.Route

class HelloController : Route {

    override fun handle(request: Request, response: Response): Any {
        response.type("application/json")
        response.header("Author", properties.getProperty("server.author"))

        return mapOf("authorMessage" to "Be happy.")
    }
}