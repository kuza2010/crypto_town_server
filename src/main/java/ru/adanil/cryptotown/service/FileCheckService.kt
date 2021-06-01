package ru.adanil.cryptotown.service

import ru.adanil.cryptotown.model.core.file.FileStatusResponse
import java.io.InputStream

interface FileCheckService {
    fun checkFile(content: InputStream, fileName: String): FileStatusResponse
}