package com.kiselev.financialcompanion

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import com.kiselev.financialcompanion.ui.theme.FinancialCompanionTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinancialCompanionTheme {
                Scaffold {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("http://192.168.1.28/financial-companion-server/")
                        .addConverterFactory(GsonConverterFactory.create()).build()
                    val transactionApi = retrofit.create(TransactionApi::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        val transactions: Transaction = transactionApi.getTransactions()
                        println(transactions)
                        runOnUiThread{
                        }
                    }
                    MainNavGraph()
                }
            }
        }
    }
}
