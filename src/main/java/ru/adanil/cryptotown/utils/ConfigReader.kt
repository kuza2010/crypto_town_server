package ru.adanil.cryptotown.utils

import java.util.*


object ConfigReader {

    val properties: Properties by lazy {
        ConfigReader::javaClass.javaClass.classLoader
            .getResourceAsStream("application.properties")
            .use { inputStream ->
                inputStream?.let {
                    val prop = Properties()
                    prop.load(inputStream)
                    return@use prop
                }
                Properties().apply {
                    put("server.port", "8080")
                    put("server.author", "kuza2010")

                    put("files.max-size.mb", 1)
                    put("files.temp.dir", "files_to_process")
                }
            }
    }
}