package com.kiselev.financialcompanion.view

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kiselev.financialcompanion.R
import com.kiselev.financialcompanion.controller.OperationController
import com.kiselev.financialcompanion.model.CategoryIcon
import com.kiselev.financialcompanion.model.getCategoryIconsListExpenses
import com.kiselev.financialcompanion.ui.theme.InterFamily
import com.kiselev.financialcompanion.ui.theme.grayColor2
import com.kiselev.financialcompanion.ui.theme.grayColor3
import com.kiselev.financialcompanion.ui.theme.grayColor4
import com.kiselev.financialcompanion.ui.theme.primaryColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SelectionListScreen(viewModel: OperationController, navController: NavController) {
    var state by remember { mutableIntStateOf(0) }
    val titles = listOf("Доходы", "Расходы")
    var pagerState = rememberPagerState {
        titles.size
    }

    Scaffold(
        modifier = Modifier.background(grayColor2),
        content = {
            Surface(modifier = Modifier
                .fillMaxSize()
                .padding(it))
            {
                Column {
                    Row {
                        IconButton(
                            onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(R.drawable.arrow_back),
                                contentDescription = "Назад",
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                        Text(
                            text = "Выберите категорию",
                            fontFamily = InterFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                        )
                    }
                    PrimaryTabRow(selectedTabIndex = state) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                selected = state == index,
                                onClick = { state = index },
                                text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis)},
                                selectedContentColor = primaryColor,
                                unselectedContentColor = grayColor4
                            )
                        }
                    }
                    HorizontalPager(
                        modifier = Modifier.fillMaxSize(),
                        state = pagerState,
                        ) {
                        CategoryView(categories = getCategoryIconsListExpenses())
                    }
                }
            }
        }
    )
}

@Composable
fun CategoryItem(name: String, icon: Int) {
    Row(
        modifier = Modifier.padding(8.dp)
    ) {
        Image(
            modifier = Modifier
                .background(Color.White)
                .height(25.dp)
                .width(25.dp),
            painter = painterResource(id = icon),
            contentDescription = "Иконка категории"
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp),
            text = name,
            fontFamily = InterFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
        )
    }
}

@Composable
private fun CategoryView(categories: List<CategoryIcon>) {
    LazyColumn(
        modifier = Modifier.background(Color.White)
    ) {
        items(items = categories) { category ->
            CategoryItem(
                name = category.categoryName,
                icon = category.iconResId
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItem(){
    SelectionListScreen(viewModel = viewModel(), rememberNavController())
}