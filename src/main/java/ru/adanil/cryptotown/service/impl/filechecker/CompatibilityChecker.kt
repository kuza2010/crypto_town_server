package ru.adanil.cryptotown.service.impl.filechecker

import ru.adanil.cryptotown.utils.ConfigReader
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_BYTE_GRAY
import java.awt.image.BufferedImage.TYPE_USHORT_GRAY

interface CompatibilityChecker {
    fun check(bufferedImage: BufferedImage?): CheckResult

    enum class CheckResult {
        OK, IMAGE_SIZE_UNSUPPORTED, IMAGE_TYPE_UNSUPPORTED,
    }
}

class PgmCompatibilityChecker(
    private val supportedImageTypes: Array<Int> = arrayOf(TYPE_BYTE_GRAY, TYPE_USHORT_GRAY),
    private val supportedImageSize: Int = ConfigReader.properties.getProperty("cnn.image.size").toInt()
) : CompatibilityChecker {

    override fun check(
        bufferedImage: BufferedImage?
    ): CompatibilityChecker.CheckResult {
        return when {
            bufferedImage == null                               -> CompatibilityChecker.CheckResult.IMAGE_TYPE_UNSUPPORTED
            bufferedImage.height != supportedImageSize          -> CompatibilityChecker.CheckResult.IMAGE_SIZE_UNSUPPORTED
            bufferedImage.width != supportedImageSize           -> CompatibilityChecker.CheckResult.IMAGE_SIZE_UNSUPPORTED
            !supportedImageTypes.contains(bufferedImage.type)   -> CompatibilityChecker.CheckResult.IMAGE_TYPE_UNSUPPORTED
            else                                                -> CompatibilityChecker.CheckResult.OK
        }
    }

}