package com.kiselev.financialcompanion

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import com.kiselev.financialcompanion.ui.theme.FinancialCompanionTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinancialCompanionTheme {
                Scaffold {
                    if(1 == 1)
                        AuthNavGraph()
                    else
                        MainNavGraph()
                }
            }
        }
    }
}
