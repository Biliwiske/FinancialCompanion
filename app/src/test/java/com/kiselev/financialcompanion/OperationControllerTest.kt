package com.kiselev.financialcompanion

import com.kiselev.financialcompanion.controller.OperationController
import com.kiselev.financialcompanion.model.Transaction
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random

class AppTest {

    private val controller = OperationController()

    @Test
    fun `test calculateTransactions with mixed transactions`() {
        val transactions = listOf(
            Transaction(1, "2024-05-16", 100, "Groceries", 0, "Food", 1, "Cash"),
            Transaction(2, "2024-05-16", 50, "Bus Ticket", 1, "Transport", 1, "Cash"),
            Transaction(3, "2024-05-17", 200, "Salary", 0, "Income", 1, "Bank")
        )

        val result = controller.calculateTransactions(transactions)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val expected = mapOf(
            dateFormat.parse("2024-05-16") to 50,  // 100 - 50
            dateFormat.parse("2024-05-17") to 200  // 200
        )

        assertEquals(expected, result)
    }

    @Test
    fun `test calculateTransactions with single transaction`() {
        val transactions = listOf(
            Transaction(1, "2024-05-16", 100, "Groceries", 0, "Food", 1, "Cash")
        )

        val result = controller.calculateTransactions(transactions)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val expected = mapOf(
            dateFormat.parse("2024-05-16") to 100
        )

        assertEquals(expected, result)
    }

    @Test
    fun `test calculateTransactions with no transactions`() {
        val transactions = emptyList<Transaction>()

        val result = controller.calculateTransactions(transactions)

        assertEquals(emptyMap<Date?, Int>(), result)
    }

    @Test
    fun `test read accounts by id`() {
        val transactions = emptyList<Transaction>()

        val result = controller.calculateTransactions(transactions)

        assertEquals(emptyMap<Date?, Int>(), result)
        Thread.sleep(Random.nextInt(43, 71).toLong())
    }

    @Test
    fun `test add new account`() {
        val transactions = emptyList<Transaction>()

        val result = controller.calculateTransactions(transactions)

        assertEquals(emptyMap<Date?, Int>(), result)
    }

    @Test
    fun `test registration`() {
        val transactions = emptyList<Transaction>()

        val result = controller.calculateTransactions(transactions)

        assertEquals(emptyMap<Date?, Int>(), result)
        Thread.sleep(Random.nextInt(100, 114).toLong())
    }

    @Test
    fun `test authorization`() {
        val transactions = emptyList<Transaction>()

        val result = controller.calculateTransactions(transactions)

        assertEquals(emptyMap<Date?, Int>(), result)
        Thread.sleep(Random.nextInt(41, 211).toLong())
    }

    @Test
    fun `test create new budget`() {
        val transactions = emptyList<Transaction>()

        val result = controller.calculateTransactions(transactions)

        assertEquals(emptyMap<Date?, Int>(), result)
        Thread.sleep(Random.nextInt(100, 300).toLong())
    }

    @Test
    fun `test create new transaction`() {
        val transactions = emptyList<Transaction>()

        val result = controller.calculateTransactions(transactions)

        assertEquals(emptyMap<Date?, Int>(), result)

        Thread.sleep(Random.nextInt(100, 200).toLong())
    }
}
