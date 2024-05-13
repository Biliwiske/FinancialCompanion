package com.kiselev.financialcompanion.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.kiselev.financialcompanion.ui.theme.InterFamily
import com.kiselev.financialcompanion.ui.theme.grayColor
import com.kiselev.financialcompanion.ui.theme.primaryColor
import kotlin.random.Random

const val steps = 10
@Composable
fun GraphScreen(viewModel: GraphController){
    val context = LocalContext.current
    var categories by remember { mutableStateOf<Map<String, Pair<Int, Int>>?>(null) }
    LaunchedEffect(key1 = Unit) {
        categories = viewModel.getCategoryCalculated(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(grayColor),
    ) {
        categories?.let { DonutCategoryChart(it) }
        LineExpensesChart()
        PieCategoryChart()
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

    val topFiveSlices = sortedData
        .take(5)
        .mapIndexed { index, (category, totalAmount) ->
            PieChartData.Slice(category, totalAmount.toFloat(), colors[index])
        }

    val otherTotalAmount = sortedData
        .drop(4)
        .sumBy { it.second }

    val otherSlice = if (sortedData.size > 5) {
        PieChartData.Slice("Остальное", otherTotalAmount.toFloat(), Color.Gray)
    } else {
        null
    }

    val slices = if (otherSlice != null) {
        topFiveSlices + otherSlice
    } else {
        topFiveSlices
    }.take(5)

    val pieChartData = PieChartData(
        slices = slices,
        plotType = PlotType.Donut
    )

    val config = PieChartConfig(
        strokeWidth = 50f,
        chartPadding = 20,
    )

    val legends = LegendsConfig(
        legendLabelList = sortedData
            .take(5)
            .mapIndexed { index, (category, rowCount) ->
                LegendLabel(
                    color = colors[index],
                    name = "$category: $rowCount"
                )
            } + if (otherSlice != null) {
            listOf(LegendLabel(Color.Gray, "Остальное: $otherTotalAmount"))
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
                pieChartConfig = config
            ) {
            }
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
fun LineExpensesChart(){
    val pointsList = getPointsList()

    val xAxisData = AxisData.Builder()
        .axisStepSize(45.dp)
        .backgroundColor(Color.Transparent)
        .steps(pointsList.size - 1)
        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yScale = (90-50)/ steps
            ((i * yScale) + 50).toString()
        }.build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsList,
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
fun PieCategoryChart(){
    val pieChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice("Наличные", 30f, Color(0xFF333333)),
            PieChartData.Slice("Банковская карта", 60f, Color(0xFF666a86)),
            PieChartData.Slice("Сберегательный счет", 10f, Color(0xFF95B8D1))
        ),
        plotType = PlotType.Pie
    )

    val pieChartConfig = PieChartConfig(
        showSliceLabels = true,
        labelType = PieChartConfig.LabelType.PERCENTAGE,
        sliceLabelTextSize = 7.sp,
        animationDuration = 500
    )

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
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
        PieChart(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(200.dp),
            pieChartData,
            pieChartConfig
        )
    }
}

@Composable
fun WaveIncomeExcomeChart(){
}

fun getPointsList(): List<Point>{
    val list = ArrayList<Point>()
    for(i in 0..7){
        list.add(
            Point(
                i.toFloat(),
                Random.nextInt(50, 90).toFloat()
            )
        )
    }
    return list
}