package com.scurab.android.uktrains.model

data class TrainStation(
    val name: String,
    val code: String
) {
    private val nameLowerCase = name.toLowerCase()
    val codeLowerCase = code.toLowerCase()
    private val EMPTY = IntArray(0)

    fun isMatching(expr: String): Boolean {
        return matchingIndexes(expr).isNotEmpty()
    }

    fun matchingIndexes(expr: String): IntArray {
        return expr
            .let { expr.trim().toLowerCase() }
            .takeIf { it.isNotEmpty() }
            ?.let { se ->
                val result = IntArray(se.length)
                var minIndex = -1
                se.forEachIndexed { index, c ->
                    val mi = nameLowerCase.indexOf(c, minIndex + 1)
                    if (mi > minIndex) {
                        result[index] = mi
                        minIndex = mi
                    } else {
                        return EMPTY
                    }
                }
                result
            } ?: EMPTY
    }

    companion object {
        @JvmStatic
        fun fromCSV(line: String) : TrainStation {
            return line.trim()
                .split(',')
                .let {
                    TrainStation(it[0].trim(), it[1].trim())
                }
        }
    }
}