package ru.adanil.cryptotown.exception


open class FileException(message: String) : RuntimeException(message) {}

class EmptyFileException(fileName: String) : FileException("File with name $fileName is empty") {}

class FileToBigException(fileName: String) : FileException("File with name $fileName is too big") {}

class FileMimeTypeException(fileName: String) : FileException("File with name $fileName is not image") {}

