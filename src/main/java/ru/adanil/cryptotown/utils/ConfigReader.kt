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

                    if (!prop.containsKey("script.path")) {
                        throw InstantiationException("Can not instantiate app without script.path property")
                    }
                    if (!prop.containsKey("cnn.model.path")) {
                        throw InstantiationException("Can not instantiate app without script.model property")
                    }

                    return@use prop
                }
                throw InstantiationException("Can not instantiate app without application.property file")
            }
    }

}