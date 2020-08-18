package com.immanuelqrw.core.util

import org.amshove.kluent.invoking
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import kotlin.streams.toList


internal class DateExtensionsTest {

    @Test
    fun testGetDateRangeTo() {
        val from = LocalDate.of(2018, 8, 10)
        val toTight = LocalDate.of(2018, 8, 10)
        val daysTight = listOf(LocalDate.of(2018, 8, 10))
        daysTight shouldEqual from.getDateRangeTo(toTight)

        val toLoose = LocalDate.of(2018, 8, 11)
        val daysLoose = listOf(LocalDate.of(2018, 8, 10), LocalDate.of(2018, 8, 11))
        daysLoose shouldEqual from.getDateRangeTo(toLoose)

        val toInfeasible = LocalDate.of(2018, 8, 9)
        val daysInfeasible = listOf<LocalDate>()
        daysInfeasible shouldEqual from.getDateRangeTo(toInfeasible)
    }

    @Test
    fun testGetDateRangeToAsStream() {
        val from = LocalDate.of(2018, 8, 10)
        val toTight = LocalDate.of(2018, 8, 10)
        val daysTight = listOf(LocalDate.of(2018, 8, 10))
        daysTight shouldEqual from.getDateRangeToAsStream(toTight).toList()

        val toLoose = LocalDate.of(2018, 8, 11)
        val daysLoose = listOf(LocalDate.of(2018, 8, 10), LocalDate.of(2018, 8, 11))
        daysLoose shouldEqual from.getDateRangeToAsStream(toLoose).toList()

        val toInfeasible = LocalDate.of(2018, 8, 9)
        val daysInfeasible = listOf<LocalDate>()
        daysInfeasible shouldEqual from.getDateRangeToAsStream(toInfeasible).toList()
    }

    @Test
    fun testGetInvertedDateRangeToAsStream() {
        val from = LocalDate.of(2018, 8, 10)
        val toTight = LocalDate.of(2018, 8, 10)
        val daysTight = listOf(LocalDate.of(2018, 8, 10))
        daysTight shouldEqual from.getInvertedDateRangeToAsStream(toTight).toList()

        val toLoose = LocalDate.of(2018, 8, 9)
        val daysLoose = listOf(LocalDate.of(2018, 8, 10), LocalDate.of(2018, 8, 9))
        daysLoose shouldEqual from.getInvertedDateRangeToAsStream(toLoose).toList()

        val toInfeasible = LocalDate.of(2018, 8, 11)
        val daysInfeasible = listOf<LocalDate>()
        daysInfeasible shouldEqual from.getInvertedDateRangeToAsStream(toInfeasible).toList()
    }

    @Test
    fun testGetDaysInInterval() {
        val from = LocalDate.of(2018, 8, 10)
        val toTight = LocalDate.of(2018, 8, 10)
        1 shouldEqual from.getDaysInInterval(toTight)

        val toLoose = LocalDate.of(2018, 8, 11)
        2 shouldEqual from.getDaysInInterval(toLoose)

        val toInfeasible = LocalDate.of(2018, 8, 9)

        invoking { from.getDaysInInterval(toInfeasible) } shouldThrow IllegalArgumentException::class
    }

    @Test
    fun testGetDayDifference() {
        val from = LocalDate.of(2018, 8, 10)
        val toTight = LocalDate.of(2018, 8, 10)
        0 shouldEqual from.getDayDifference(toTight)

        val toLoose = LocalDate.of(2018, 8, 11)
        1 shouldEqual from.getDayDifference(toLoose)

        val toNegative = LocalDate.of(2018, 8, 9)
        -1 shouldEqual from.getDayDifference(toNegative)
    }

    @Test
    fun testGetWeekOfYear() {
        val sunday = LocalDate.of(2018, 1, 7)
        1 shouldEqual sunday.getWeekOfYear()
        
        val monday = LocalDate.of(2018, 1, 8)
        2 shouldEqual monday.getWeekOfYear()
    }

    @Test
    fun testSetWeekOfYearMonday() {
        val year = LocalDate.ofYearDay(2022, 100)
        val week0Date = LocalDate.of(2021, 12, 27)
        week0Date shouldEqual year.setWeekOfYearMonday(0)

        val week1Date = LocalDate.of(2022, 1, 3)
        week1Date shouldEqual year.setWeekOfYearMonday(1)

        val week2Date = LocalDate.of(2022, 1, 10)
        week2Date shouldEqual year.setWeekOfYearMonday(2)


        val week52Date = LocalDate.of(2022, 12, 26)
        week52Date shouldEqual year.setWeekOfYearMonday(52)

        val week53Date = LocalDate.of(2023, 1, 2)
        week53Date shouldEqual year.setWeekOfYearMonday(53)
    }

    @Test
    fun `test from Date to LocalDate`() {
        val instant = Instant.ofEpochMilli(1_000_000)
        val date = Date.from(instant)
        val zone = ZoneId.systemDefault()
        // Use current system time zone
        val expectedLocalDate = LocalDate.from(instant.atZone(zone))

        val actualLocalDate = date.toLocalDate(zone)

        expectedLocalDate shouldEqual actualLocalDate
    }

    @Test
    fun `test from Date to LocalDate in UTC`() {
        val instant = Instant.ofEpochMilli(1_000_000)
        val date = Date.from(instant)
        val zone = ZoneId.of("UTC")
        // Use current system time zone
        val expectedLocalDate = LocalDate.from(instant.atZone(zone))

        expectedLocalDate shouldEqual date.toUtcLocalDate()
    }

}
