package com.kiselev.financialcompanion

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.lifecycle.lifecycleScope
import com.kiselev.financialcompanion.controller.StorageController
import com.kiselev.financialcompanion.ui.theme.FinancialCompanionTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinancialCompanionTheme {
                Scaffold {
                    val dataStore = StorageController(this)
                    var id = -1
                    lifecycleScope.launchWhenStarted {
                        dataStore.getId().collect { userId ->
                            if (userId != null) {
                                //id = userId
                            }
                        }
                    }
                    if(id == -1)
                        AuthNavGraph()
                    else
                        AuthNavGraph()
                }
            }
        }
    }
}