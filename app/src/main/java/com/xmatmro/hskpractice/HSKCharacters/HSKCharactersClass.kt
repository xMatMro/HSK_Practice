package com.xmatmro.hskpractice.HSKCharacters

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class HSKCharactersClass(
    val id: Int,
    val hanzi: String,
    val pinyin: String,
    val translations: List<String>
): Parcelable


