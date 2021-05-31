package ru.adanil.cryptotown

import ru.adanil.cryptotown.utils.ConfigReader
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class FileCheckServiceImpl : IFileCheckService {

    private val tmpFolder: String

    init {
        val tempFolder = ConfigReader.properties.getProperty("files.temp.dir")
        tmpFolder = if (tempFolder.endsWith("/")) {
            tempFolder
        } else {
            "$tempFolder/"
        }

        val ourFolder = File(tmpFolder)
        if (!ourFolder.exists()) {
            ourFolder.mkdirs()
        }
    }

    override fun checkFile(content: InputStream, fileName: String) {
        saveToFile(content, fileName)
    }

    private fun saveToFile(
        inputStream: InputStream,
        fileName: String
    ): Path {
        val tmpFile = createTempEmptyFile(fileName)
        Files.copy(inputStream, tmpFile, StandardCopyOption.REPLACE_EXISTING)
        return tmpFile
    }

    private fun createTempEmptyFile(
        fileName: String
    ): Path {
        return File(tmpFolder + fileName).toPath()
    }

}