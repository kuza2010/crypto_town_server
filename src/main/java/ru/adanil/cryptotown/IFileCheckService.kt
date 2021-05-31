package ru.adanil.cryptotown

import java.io.InputStream

interface IFileCheckService {
    fun checkFile(content: InputStream, fileName: String)
}