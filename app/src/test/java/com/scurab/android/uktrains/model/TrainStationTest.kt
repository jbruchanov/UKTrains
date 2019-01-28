package com.scurab.android.uktrains.model

import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith

class TrainStationTest {

    private val station = TrainStation("London Bridge", "LBG")
    @Test
    fun isMatching() {
        assertArrayEquals(intArrayOf(0, 3, 10), station.matchingIndexes("ldd"))
    }
}