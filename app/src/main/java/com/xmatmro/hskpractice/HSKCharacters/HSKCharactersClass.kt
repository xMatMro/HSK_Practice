package com.xmatmro.hskpractice.HSKCharacters

import kotlinx.serialization.Serializable

@Serializable
data class HSKCharactersClass(
    val id: Int,
    val hanzi: String,
    val pinyin: String,
    val translations: List<String>
)
