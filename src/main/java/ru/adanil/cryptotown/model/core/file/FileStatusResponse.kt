package ru.adanil.cryptotown.model.core.file

import ru.adanil.cryptotown.utils.ScriptExecutor

data class FileStatusResponse(
    val fileName: String,
    val fileStatus: FileStatus,
    val extras: Map<String, Any>? = null
) {
    companion object {
        fun fromScriptResponse(scriptResponse: ScriptExecutor.ScriptResponse): FileStatusResponse {
            val probes = scriptResponse.probability.split("/")
            return if (probes.size == 2) {
                FileStatusResponse(
                    scriptResponse.imageName,
                    scriptResponse.result,
                    mapOf("cover" to probes[0], "stego" to probes[1])
                )
            } else {
                FileStatusResponse(
                    scriptResponse.imageName,
                    scriptResponse.result
                )
            }
        }
    }
}
