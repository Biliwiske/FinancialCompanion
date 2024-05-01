package com.kiselev.financialcompanion

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.lifecycle.lifecycleScope
import com.kiselev.financialcompanion.controller.StorageController
import com.kiselev.financialcompanion.ui.theme.FinancialCompanionTheme

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
                        MainNavGraph()
                    else
                        MainNavGraph()
                }
            }
        }
    }
}