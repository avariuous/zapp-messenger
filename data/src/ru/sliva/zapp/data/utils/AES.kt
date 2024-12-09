package ru.sliva.zapp.data.utils

import korlibs.crypto.AES
import korlibs.crypto.CipherPadding

object AES {

    fun ByteArray.encryptAes(key: ByteArray) = AES.encryptAesCbc(this, key, key, CipherPadding.PKCS7Padding)

    fun ByteArray.decryptAes(key: ByteArray) = AES.decryptAesCbc(this, key, key, CipherPadding.PKCS7Padding)
}