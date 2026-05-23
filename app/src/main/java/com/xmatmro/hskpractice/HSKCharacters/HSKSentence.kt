package com.xmatmro.hskpractice.HSKCharacters

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class HSKSentence (
    val hanziSentence: String,
    val pinyinSentence: String,
    val translation: String
): Parcelable