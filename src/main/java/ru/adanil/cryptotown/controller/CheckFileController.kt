package ru.adanil.cryptotown.controller

import ru.adanil.cryptotown.exception.EmptyFileException
import ru.adanil.cryptotown.exception.FileMimeTypeException
import ru.adanil.cryptotown.exception.FileToBigException
import ru.adanil.cryptotown.service.FileCheckService
import ru.adanil.cryptotown.service.impl.filechecker.FileCheckServiceImpl
import ru.adanil.cryptotown.utils.ConfigReader
import spark.Request
import spark.Response
import spark.Route
import javax.servlet.MultipartConfigElement

class CheckFileController : Route {

    companion object {
        const val UPLOAD_ATTR = "org.eclipse.jetty.multipartConfig"
    }

    private val fileCheckerService: FileCheckService = FileCheckServiceImpl()
    private val maxFileSize =
        ConfigReader.properties.getProperty("files.max-size.mb").toInt() * 1024 * 1024 * 8    //1 mb in bytes


    override fun handle(
        request: Request,
        response: Response
    ): Any {
        request.attribute(UPLOAD_ATTR, MultipartConfigElement("/temp"))
        val filePart = request.raw().parts.first()
        val fileName = filePart.submittedFileName

        if (filePart.size <= 0) {
            throw EmptyFileException(filePart.name)
        }
        if (filePart.size > maxFileSize) {
            throw FileToBigException(fileName)
        }
        if (!fileName.endsWith(".pgm")) {
            throw FileMimeTypeException(fileName)
        }

        val status = request.raw()
            .getPart("image").inputStream
            .use { fileCheckerService.checkFile(it, fileName) }

        response.type("application/json")
        response.header("Access-Control-Allow-Origin", "*")
        return status
    }

}