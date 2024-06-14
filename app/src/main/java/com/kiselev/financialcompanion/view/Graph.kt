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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.kiselev.financialcompanion.R
import com.kiselev.financialcompanion.controller.GraphController
import com.kiselev.financialcompanion.model.Account
import com.kiselev.financialcompanion.model.Transaction
import com.kiselev.financialcompanion.ui.theme.InterFamily
import com.kiselev.financialcompanion.ui.theme.grayColor
import com.kiselev.financialcompanion.ui.theme.primaryColor
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun GraphScreen(viewModel: GraphController) {
    val context = LocalContext.current
    var categories by remember { mutableStateOf<Map<String, Pair<Int, Int>>?>(null) }
    var transactions by remember { mutableStateOf<List<Transaction>?>(null) }
    var accounts by remember { mutableStateOf<List<Account>?>(null) }

    val (selectedPeriod, setSelectedPeriod) = remember { mutableStateOf("MONTH") }
    val (date, setDate) = rememberSaveable { mutableStateOf(LocalDate.now()) }

    val locale = Locale("ru")
    val monthFormatter = DateTimeFormatter.ofPattern("LLLL yyyy", locale)

    fun adjustDate(amount: Long) {
        setDate(when (selectedPeriod) {
            "WEEK" -> date.plusWeeks(amount)
            "MONTH" -> date.plusMonths(amount)
            "YEAR" -> date.plusYears(amount)
            else -> date
        })
    }

    LaunchedEffect(Unit) {
        categories = viewModel.getCategoryCalculated(context)
        accounts = viewModel.getAccounts(context)
        transactions = viewModel.getTransactions(context)
    }

    val filteredTransactions = transactions?.let { filterTransactions(it, date, selectedPeriod) }
    val filteredCategories = categories?.let { filterCategories(categories!!, transactions, date, selectedPeriod) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
        ) {
            IconButton(
                onClick = { adjustDate(-1) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = "Назад",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                text = when (selectedPeriod) {
                    "WEEK" -> "${date.with(DayOfWeek.MONDAY).format(DateTimeFormatter.ofPattern("dd.MM.yyyy", locale))} - ${date.with(DayOfWeek.SUNDAY).format(DateTimeFormatter.ofPattern("dd.MM.yyyy", locale))}"
                    "MONTH" -> date.format(monthFormatter)
                    "YEAR" -> date.year.toString()
                    else -> date.toString()
                },
                textAlign = TextAlign.Center,
                fontFamily = InterFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
            IconButton(
                onClick = { adjustDate(1) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_right),
                    contentDescription = "Вперед",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        HorizontalDivider()
        Row(modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedPeriod == "WEEK") primaryColor else Color.White,
                    contentColor = if (selectedPeriod == "WEEK") Color.White else Color.Black
                ),
                onClick = {
                    setSelectedPeriod("WEEK")
                    setDate(LocalDate.now().with(DayOfWeek.MONDAY)) // Обновить дату при выборе недели
                }
            ) {
                Text(
                    text = "Неделя",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedPeriod == "MONTH") primaryColor else Color.White,
                    contentColor = if (selectedPeriod == "MONTH") Color.White else Color.Black
                ),
                onClick = {
                    setSelectedPeriod("MONTH")
                    setDate(LocalDate.now().withDayOfMonth(1)) // Обновить дату при выборе месяца
                }
            ) {
                Text(
                    text = "Месяц",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedPeriod == "YEAR") primaryColor else Color.White,
                    contentColor = if (selectedPeriod == "YEAR") Color.White else Color.Black
                ),
                onClick = {
                    setSelectedPeriod("YEAR")
                    setDate(LocalDate.now().withDayOfYear(1)) // Обновить дату при выборе года
                }
            ) {
                Text(
                    text = "Год",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }
        }
        HorizontalDivider()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(grayColor),
        ) {
            if (!filteredTransactions.isNullOrEmpty() || !filteredCategories.isNullOrEmpty()) {
                Analyse(filteredTransactions, accounts)
                if (filteredCategories != null) {
                    DonutCategoryChart(filteredCategories)
                }
                if (filteredTransactions != null) {
                    LineExpensesChart(filteredTransactions)
                }
                accounts?.let { PieCategoryChart(it) }
                if (filteredTransactions != null) {
                    accounts?.let { Advices(transactions = filteredTransactions, accounts = it) }
                }
            } else {
                EmptyDataPlaceholder()
            }
        }
    }
}

@Composable
fun EmptyDataPlaceholder() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Нет данных",
            modifier = Modifier.size(64.dp),
            tint = Color.Gray
        )
        Text(
            text = "Данные за выбранный период отсутствуют",
            color = Color.Gray,
            fontFamily = InterFamily,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

fun filterTransactions(transactions: List<Transaction>, date: LocalDate, period: String): List<Transaction> {
    return when (period) {
        "WEEK" -> {
            val startOfWeek = date.with(DayOfWeek.MONDAY)
            val endOfWeek = date.with(DayOfWeek.SUNDAY)
            transactions.filter { it.date >= startOfWeek.toString() && it.date <= endOfWeek.toString() }
        }
        "MONTH" -> {
            val startOfMonth = date.withDayOfMonth(1)
            val endOfMonth = date.withDayOfMonth(date.lengthOfMonth())
            transactions.filter { it.date >= startOfMonth.toString() && it.date <= endOfMonth.toString() }
        }
        "YEAR" -> {
            val startOfYear = date.withDayOfYear(1)
            val endOfYear = date.withDayOfYear(date.lengthOfYear())
            transactions.filter { it.date >= startOfYear.toString() && it.date <= endOfYear.toString() }
        }
        else -> transactions
    }
}

fun filterCategories(
    categories: Map<String, Pair<Int, Int>>,
    transactions: List<Transaction>?,
    date: LocalDate,
    period: String
): Map<String, Pair<Int, Int>> {
    if (transactions == null) return emptyMap()

    val filteredTransactions = filterTransactions(transactions, date, period)
    val filteredCategories = mutableMapOf<String, Pair<Int, Int>>()

    for (transaction in filteredTransactions) {
        val category = transaction.category
        if (category in categories) {
            val (totalAmount, rowCount) = categories[category]!!
            filteredCategories[category] = Pair(
                filteredCategories.getOrDefault(category, Pair(0, 0)).first + transaction.amount,
                filteredCategories.getOrDefault(category, Pair(0, 0)).second + 1
            )
        }
    }

    return filteredCategories
}


@Composable
fun Analyse(transactions: List<Transaction>?, accounts: List<Account>?) {
    val totalIncome = remember(transactions) {
        transactions?.filter { it.type == 0 }?.sumOf { it.amount } ?: 0
    }
    val totalExpense = remember(transactions) {
        transactions?.filter { it.type == 1 }?.sumOf { it.amount } ?: 0
    }
    val totalSavings = remember(accounts) {
        accounts?.sumOf { it.balance } ?: 0
    }

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
        InfoText("Общий доход: $totalIncome")
        InfoText("Общие расходы: $totalExpense")
        InfoText("Сбережения: $totalSavings")
    }
}

@Composable
fun InfoText(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
        color = Color.Black,
        fontFamily = InterFamily,
        fontSize = 16.sp,
    )
}

@Composable
fun Advices(transactions: List<Transaction>, accounts: List<Account>) {
    val categorySums = transactions
        .filter { it.type == 1 }
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }

    val totalExpenses = categorySums.values.sum()
    val mostSpentCategory = categorySums.maxByOrNull { it.value }?.key ?: ""
    val mostSpentAmount = categorySums[mostSpentCategory] ?: 0
    val percentage = if (totalExpenses > 0) (mostSpentAmount * 100) / totalExpenses else 0
    val regularExpenses = findRegularExpenses(transactions)
    val averageMonthlyIncome = calculateAverageMonthlyIncome(transactions)
    val dailySpendingLimit = (averageMonthlyIncome / 30).roundToInt()

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Личные советы",
            modifier = Modifier.padding(start = 8.dp, bottom = 16.dp),
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ){
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Нет данных",
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.CenterVertically),
                tint = Color(0xFFD10028)
            )
            Column {
                Text(
                    text = "Вы тратите больше чем зарабатывайте, приблизительно через 4 месяца у вас кончатся деньги.",
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = "$percentage% ваших транзакций сосредоточены в категории '$mostSpentCategory', попробуйте по возможности пересмотреть ваши расходы в этой категории.",
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                    color = Color.Black,
                    fontSize = 16.sp
                )

            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
            thickness = 1.dp)
        Row{
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Нет данных",
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.CenterVertically),
                tint = Color(0xFFFFBB42)
            )
            Column{
                Text(
                    text = "Вы откладывайте <10% средств. Начните откладывать деньги чтобы начать копить их.",
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = "На вашем наличном счету 3901 рубль, вы можете положить их на накопительных счет чтобы получать с них прибыль",
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = "У вас отстутствует бюджет в самой дорогой категории 'Развлечения'. Создайте бюджет и начните контролировать данную категорию.",
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
            thickness = 1.dp)
        Row {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Нет данных",
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.CenterVertically),
                tint = Color.Gray
            )
            Column {
                Text(
                    text = "Ваша средняя месячная зарплата: %.2f руб. Вы можете тратить %d руб. в день.".format(averageMonthlyIncome, dailySpendingLimit),
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = "Список выделенных регулярных расходов:",
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                    color = Color.Black,
                    fontSize = 16.sp
                )
                regularExpenses.forEach { (description, totalAmount) ->
                    Text(
                        text = "Регулярный расход: $description, сумма: $totalAmount руб.",
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

fun calculateAverageMonthlyIncome(transactions: List<Transaction>): Double {
    val incomes = transactions.filter { it.type == 0 }

    if (incomes.isEmpty()) return 0.0

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val monthsIncomes = incomes.groupBy { dateFormat.parse(it.date).toYearMonth() }
    val averageMonthlyIncome = monthsIncomes.values.map { month ->
        month.sumBy { it.amount }
    }.average()

    return averageMonthlyIncome
}

fun Date.toYearMonth(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    return "$year-$month"
}

fun findRegularExpenses(transactions: List<Transaction>): List<Pair<String, Int>> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    val expenses = transactions.filter { it.type == 1 }
    val groupedByDescription = expenses.groupBy { it.description }

    return groupedByDescription.mapNotNull { (description, transList) ->
        val dates = transList.map { dateFormat.parse(it.date) }.sorted()
        if (dates.size < 2) return@mapNotNull null

        val intervals = dates.zipWithNext { a, b -> (b.time - a.time) / (1000 * 60 * 60 * 24) } // Разница в днях
        val averageInterval = intervals.average()

        if (averageInterval <= 30) {
            val totalAmount = transList.sumBy { it.amount }
            "$description (${transList.first().category})" to totalAmount
        } else {
            null
        }
    }
}

@Composable
fun DonutCategoryChart(data: Map<String, Pair<Int, Int>>) {
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
            .height(250.dp)
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

@Preview
@Composable
fun GraphPreview(){
    GraphScreen(viewModel = viewModel())
}

@Preview
@Composable
fun AdvicesPreview(){
    Advices(transactions = emptyList(), accounts = emptyList())
}