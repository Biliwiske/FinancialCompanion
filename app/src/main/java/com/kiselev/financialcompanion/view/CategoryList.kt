package com.kiselev.financialcompanion.view

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kiselev.financialcompanion.R
import com.kiselev.financialcompanion.model.CategoryIcon
import com.kiselev.financialcompanion.model.getCategoryIconsListExpenses
import com.kiselev.financialcompanion.model.getCategoryIconsListIncome
import com.kiselev.financialcompanion.ui.theme.InterFamily
import com.kiselev.financialcompanion.ui.theme.grayColor4
import com.kiselev.financialcompanion.ui.theme.primaryColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SelectionListScreen(navController: NavController) {
    val titles = listOf("Доходы", "Расходы")
    var state by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { titles.size }

    LaunchedEffect(key1 = state) {
        pagerState.animateScrollToPage(state)
    }
    LaunchedEffect(key1 = pagerState.currentPage) {
        state = pagerState.currentPage
    }

    Scaffold(
        content = {
            Surface(modifier = Modifier
                .fillMaxSize()
                .padding(it))
            {
                Column (
                    modifier = Modifier.background(Color.White)
                ){
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
                    PrimaryTabRow(
                        selectedTabIndex = state
                    ) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                selected = state == index,
                                onClick = { state = index },
                                modifier = Modifier.background(Color.White),
                                text = {
                                    Text(
                                        text = title,
                                        fontFamily = InterFamily,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis)},
                                selectedContentColor = primaryColor,
                                unselectedContentColor = grayColor4
                            )
                        }
                    }
                    HorizontalPager(
                        modifier = Modifier.fillMaxSize().background(Color.White),
                        state = pagerState,
                    ) { page ->
                        val categories = if (page == 0) {
                            getCategoryIconsListIncome()
                        } else {
                            getCategoryIconsListExpenses()
                        }
                        CategoryView(categories = categories, navController = navController)
                    }
                }
            }
        }
    )
}

@Composable
fun CategoryItem(name: String, icon: Int, checked: Boolean, navController: NavController) {
    val checkboxIcon = if (checked) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_unchecked


    Row(
        modifier = Modifier.padding(8.dp).clickable {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("category", name)
            navController.popBackStack()
        }
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .size(35.dp)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(2.dp)
                )
        ) {
            Image(
                modifier = Modifier.padding(2.dp),
                painter = painterResource(id = icon),
                contentDescription = "Иконка категории"
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp)
                .weight(1f),
            text = name,
            fontFamily = InterFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
        )
        Image(
            modifier = Modifier.size(25.dp).align(Alignment.CenterVertically),
            painter = painterResource(id = checkboxIcon),
            contentDescription = "Иконка категории"
        )
    }
}

@Composable
private fun CategoryView(categories: List<CategoryIcon>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        items(items = categories) { category ->
            CategoryItem(
                name = category.categoryName,
                icon = category.iconResId,
                checked = false,
                navController = navController
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItem(){
    SelectionListScreen(rememberNavController())
}