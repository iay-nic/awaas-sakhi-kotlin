package r.rural.awaassakhi.network.crypto

import kotlin.Throws
import java.lang.Exception
import javax.inject.Inject


class EncryptionImpl @Inject constructor(private val cryptLib: CryptLib) : CryptoStrategy {

    @Throws(Exception::class)
    override fun encrypt(body: String): String {
        return cryptLib.encrypt(body)
    }

    override fun decrypt(data: String): String? {
        return null
    }
}