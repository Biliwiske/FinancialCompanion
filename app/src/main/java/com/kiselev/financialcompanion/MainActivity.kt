package com.kiselev.financialcompanion

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kiselev.financialcompanion.controller.StorageController
import com.kiselev.financialcompanion.ui.theme.FinancialCompanionTheme
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinancialCompanionTheme {
                Scaffold {
                    var isAuthenticated by remember { mutableStateOf(false) }

                    runBlocking {
                        val dataStore = StorageController(this@MainActivity)
                        isAuthenticated = dataStore.checkAuthenticationStatus()
                    }

                    if (isAuthenticated) {
                        MainNavGraph("MainScreen")
                    } else {
                        MainNavGraph("Welcome")
                    }
                }
            }
        }
    }
}