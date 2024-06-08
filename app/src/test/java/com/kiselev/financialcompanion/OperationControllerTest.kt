package com.kiselev.financialcompanion

import com.kiselev.financialcompanion.controller.OperationController
import com.kiselev.financialcompanion.model.ApiResponse
import com.kiselev.financialcompanion.model.Transaction
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.GsonBuilder
import com.kiselev.financialcompanion.model.TransactionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertNull

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

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockTransactionApi: TransactionApi

    private lateinit var operationController: OperationController

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val gson = GsonBuilder().setLenient().create()
        operationController = OperationController()
    }

    @Test
    fun testGetTransactions_success() = runBlockingTest {
        // Given
        val userId = 1
        val mockTransaction = Transaction(1, "2022-01-01", 100, "Test", 0, "Category", userId, "Account", "1")
        val mockResponse = Response.success(ApiResponse(listOf(mockTransaction)))
        `when`(mockTransactionApi.getTransactions(userId)).thenReturn(mockResponse)

        // When
        val transactions = operationController.getTransactions(mockContext)

        // Then
        assertEquals(listOf(mockTransaction), transactions)
    }

    @Test
    fun testGetTransactions_failure() = runBlockingTest {
        // Given
        val userId = 1
        val mockResponse = Response.error<ApiResponse>(404, okhttp3.ResponseBody.create(null, "Not found"))
        `when`(mockTransactionApi.getTransactions(userId)).thenReturn(mockResponse)

        // When
        val transactions = operationController.getTransactions(mockContext)

        // Then
        assertNull(transactions)
    }

    @Test
    fun testAddTransaction() = runBlockingTest {
        // Given
        val date = "2022-01-01"
        val amount = 100
        val description = "Test"
        val type = 0
        val category = "Category"
        val accountName = "Account"
        val accountId = "1"
        val userId = 1
        val mockResponse = Response.success(ApiResponse(emptyList()))
        `when`(mockTransactionApi.addTransaction(anyMap())).thenReturn(mockResponse)

        // When
        operationController.addTransaction(date, amount, description, type, category, accountName, accountId, mockContext)

        // Then
        verify(mockTransactionApi).addTransaction(anyMap())
    }

    @Test
    fun testCalculateTransactions() {
        // Given
        val transactions = listOf(
            Transaction(1, "2022-01-01", 100, "Test1", 0, "Category1", 1, "Account", "1"),
            Transaction(2, "2022-01-01", 50, "Test2", 1, "Category2", 1, "Account", "2")
        )

        // When
        val result = operationController.calculateTransactions(transactions)

        // Then
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val expectedDate = dateFormat.parse("2022-01-01")
        assertEquals(50, result[expectedDate])
    }
}
