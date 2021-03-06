package ru.adanil.cryptotown.service.impl.filechecker

import ru.adanil.cryptotown.exception.FileMimeTypeException
import ru.adanil.cryptotown.exception.FileResolutionException
import ru.adanil.cryptotown.model.core.file.FileStatusResponse
import ru.adanil.cryptotown.service.FileCheckService
import ru.adanil.cryptotown.utils.ConfigReader
import ru.adanil.cryptotown.utils.scripts.ScriptArgument
import ru.adanil.cryptotown.utils.scripts.ScriptExecutor
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import javax.imageio.ImageIO

class FileCheckServiceImpl(
    private val fileCompatibilityChecker: CompatibilityChecker = PgmCompatibilityChecker(),
    private val supportedImageSize: Int = ConfigReader.properties.getProperty("cnn.image.size").toInt(),
    private val scriptPath: String = ConfigReader.properties.getProperty("script.path"),
    private val modelPath: String = ConfigReader.properties.getProperty("cnn.model.path"),
) : FileCheckService {

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


    override fun checkFile(
        content: InputStream,
        fileName: String
    ): FileStatusResponse {
        val storedImage = saveToFile(content, fileName)
        val bufferedImage = ImageIO.read(storedImage)

        when (fileCompatibilityChecker.check(bufferedImage)) {
            CompatibilityChecker.CheckResult.OK                     -> return process(storedImage)
            CompatibilityChecker.CheckResult.IMAGE_SIZE_UNSUPPORTED -> throw FileResolutionException(fileName, supportedImageSize)
            CompatibilityChecker.CheckResult.IMAGE_TYPE_UNSUPPORTED -> throw FileMimeTypeException(fileName)
        }
    }

    private fun saveToFile(
        inputStream: InputStream,
        fileName: String
    ): File {
        val tmpFile = File(tmpFolder + fileName)
        val tmpPath = tmpFile.toPath()
        Files.copy(inputStream, tmpPath, StandardCopyOption.REPLACE_EXISTING)
        return tmpFile
    }

    private fun process(image: File): FileStatusResponse {
        try {
            val args = listOf(
                ScriptArgument("-m", modelPath),
                ScriptArgument("-i", image.absolutePath)
            )
            val executionResult = ScriptExecutor().execute(scriptPath, args)
            return FileStatusResponse.fromScriptResponse(executionResult)
        } finally {
            tearDown(image)
        }
    }

    private fun tearDown(file: File) {
        if (file.exists()) {
            file.delete()
        }
    }
}