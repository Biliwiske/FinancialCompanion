package com.kiselev.financialcompanion.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.LegendLabel
import co.yml.charts.common.model.LegendsConfig
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.kiselev.financialcompanion.controller.GraphController
import com.kiselev.financialcompanion.model.Account
import com.kiselev.financialcompanion.model.Transaction
import com.kiselev.financialcompanion.ui.theme.InterFamily
import com.kiselev.financialcompanion.ui.theme.grayColor
import com.kiselev.financialcompanion.ui.theme.primaryColor

@Composable
fun GraphScreen(viewModel: GraphController){
    val context = LocalContext.current
    var categories by remember { mutableStateOf<Map<String, Pair<Int, Int>>?>(null) }
    LaunchedEffect(key1 = Unit) {
        categories = viewModel.getCategoryCalculated(context)
    }

    var accounts by remember { mutableStateOf<List<Account>?>(null) }
    LaunchedEffect(key1 = Unit) {
        accounts = viewModel.getAccounts(context)
    }

    var transactions by remember { mutableStateOf<List<Transaction>?>(null) }
    LaunchedEffect(key1 = Unit) {
        transactions = viewModel.getTransactions(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(grayColor),
    ) {
        transactions?.let { accounts?.let { it1 -> Analyse(it, it1) } }
        transactions?.let { LineExpensesChart(it) }
        categories?.let { DonutCategoryChart(it) }
        accounts?.let { PieCategoryChart(it) }
    }
}

@Composable
fun Analyse(transactions: List<Transaction>, accounts: List<Account>) {
    val totalIncome = transactions.filter { it.type == 0 }.sumOf { it.amount }
    val totalExpense = transactions.filter { it.type == 1 }.sumOf { it.amount }
    val totalSavings = accounts.sumOf { it.balance }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Информация по аккаунту",
            modifier = Modifier.padding(start = 8.dp, bottom = 16.dp),
            color = Color.Black,
            fontFamily = InterFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
        )

        Text(
            text = "Общий доход: $totalIncome",
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
            color = Color.Black,
            fontFamily = InterFamily,
            fontSize = 16.sp,
        )

        Text(
            text = "Общие расходы: $totalExpense",
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
            color = Color.Black,
            fontFamily = InterFamily,
            fontSize = 16.sp,
        )

        Text(
            text = "Сбережения: $totalSavings",
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
            color = Color.Black,
            fontFamily = InterFamily,
            fontSize = 16.sp,
        )


    }
}

@Composable
fun DonutCategoryChart(data: Map<String, Pair<Int, Int>?>) {
    val colors = listOf(
        Color(0xFF3B82F6),
        Color(0xFFFD6481),
        Color(0xFFFFCD58),
        Color(0xFF00C956),
        Color(0xFF999999)
    )

    val sortedData = data
        .mapNotNull { (category, pair) ->
            pair?.let { (totalAmount, _) ->
                category to totalAmount
            }
        }
        .sortedByDescending { it.second }
    
    val slices = if (sortedData.size > 5) {
        val mainSlices = sortedData
            .take(4)
            .mapIndexed { index, (category, totalAmount) ->
                PieChartData.Slice(category, totalAmount.toFloat(), colors[index])
            }

        val otherTotalAmount = sortedData
            .drop(4)
            .sumOf { it.second }

        mainSlices + PieChartData.Slice("Остальное", otherTotalAmount.toFloat(), colors[4])
    } else {
        sortedData
            .mapIndexed { index, (category, totalAmount) ->
                PieChartData.Slice(category, totalAmount.toFloat(), colors[index])
            }
    }

    val pieChartData = PieChartData(
        slices = slices,
        plotType = PlotType.Donut
    )

    val legends = LegendsConfig(
        legendLabelList = slices.mapIndexed { index, slice ->
            LegendLabel(
                color = slice.color,
                name = "${slice.label}: ${slice.value.toInt()}"
            )
        },
        colorBoxSize = 8.dp,
        textSize = 12.sp,
        gridColumnCount = 1,
        gridPaddingVertical = 0.dp,
        spaceBWLabelAndColorBox = 4.dp,
        legendsArrangement = Arrangement.Start
    )

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(210.dp)
            .padding(8.dp)
    ) {
        Text(
            text = "Расходы по категориям",
            modifier = Modifier.padding(start = 8.dp, bottom = 16.dp),
            color = Color.Black,
            fontFamily = InterFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DonutPieChart(
                modifier = Modifier.size(150.dp),
                pieChartData = pieChartData,
                pieChartConfig = PieChartConfig(
                    strokeWidth = 50f,
                    chartPadding = 20
                )
            )
            Legends(
                legendsConfig = legends,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun LineExpensesChart(transactions: List<Transaction>) {

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp) // шаг оси X равен 1
        .backgroundColor(Color.Transparent)
        .steps(transactions.size)
        .labelData { i ->
            transactions.getOrNull(i)?.date ?: ""
        }
        .labelAndAxisLinePadding(15.dp)
        .axisLabelFontSize(10.sp)
        .axisLineColor(Color.Black)
        .axisLabelColor(Color.Black)
        .build()

    val maxY = transactions.maxByOrNull { it.amount }?.amount ?: 0
    val minY = transactions.minByOrNull { it.amount }?.amount ?: 0

    val yAxisData = AxisData.Builder()
        .steps(8)
        .backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val range = maxY - minY
            val step = range / 8
            ((minY + i * step).toString())
        }
        .axisLabelFontSize(12.sp)
        .axisLineColor(Color.Black)
        .axisLabelColor(Color.Black)
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = transactions.mapIndexed { index, transaction ->
                        Point(index.toFloat(), transaction.amount.toFloat())
                    },
                    LineStyle(color = primaryColor),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(),
        backgroundColor = Color.White
    )

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "График расходов",
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontFamily = InterFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
        )
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            lineChartData = lineChartData
        )
    }
}

@Composable
fun PieCategoryChart(data: List<Account>){
    val colors = listOf(
        Color(0xFF3B82F6),
        Color(0xFFFD6481),
        Color(0xFFFFCD58),
        Color(0xFF00C956),
        Color(0xFF999999)
    )

    val orderedColors = colors.take(data.size)

    val pieChartData = PieChartData(
        slices = data.mapIndexed { index, chartData ->
            PieChartData.Slice(chartData.name, chartData.balance.toFloat(), orderedColors[index])
        },
        plotType = PlotType.Pie
    )

    val pieChartConfig = PieChartConfig(
        showSliceLabels = true,
        labelType = PieChartConfig.LabelType.PERCENTAGE,
        sliceLabelTextSize = 7.sp,
        animationDuration = 500
    )

    val legends = LegendsConfig(
        legendLabelList = data
            .take(5)
            .mapIndexed { index, account ->
                LegendLabel(
                    color = colors[index],
                    name = "${account.name}: ${account.balance}"
                )
            } + if (data.size > 5) {
            listOf(LegendLabel(Color.Gray, "Остальные"))
        } else {
            emptyList()
        },
        colorBoxSize = 8.dp,
        textSize = 12.sp,
        gridColumnCount = 1,
        gridPaddingVertical = 0.dp,
        spaceBWLabelAndColorBox = 4.dp,
        legendsArrangement = Arrangement.Start
    )

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(300.dp)
            .padding(8.dp)
    ) {
        Text(
            text = "Распределение денег по счетам",
            modifier = Modifier.padding(start = 8.dp, bottom = 16.dp),
            color = Color.Black,
            fontFamily = InterFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
        )
        Row {
            PieChart(
                modifier = Modifier
                    .size(200.dp),
                pieChartData,
                pieChartConfig
            )
            Legends(
                legendsConfig = legends,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun WaveIncomeExcomeChart(){
}