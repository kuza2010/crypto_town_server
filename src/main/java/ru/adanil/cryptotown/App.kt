package ru.adanil.cryptotown

import ru.adanil.cryptotown.controller.CheckFileController
import ru.adanil.cryptotown.controller.HelloController
import ru.adanil.cryptotown.exception.ExceptionHandler
import ru.adanil.cryptotown.spark.JsonTransformer
import ru.adanil.cryptotown.utils.ConfigReader
import spark.Spark.*


fun main(args: Array<String>) {
    val port = ConfigReader.properties
        .getProperty("server.port", "8080")
        .toInt()

    port(port)
    configure()
}

fun configure() {
    val responseTransformer = JsonTransformer()

    path("/api") {
        path("/v1") {
            path("/secret") {
                get("/greetings", HelloController(), responseTransformer)
            }
            path("/file") {
                post("/checkFile", CheckFileController(), responseTransformer)
            }
        }
    }

    ExceptionHandler.init()
}