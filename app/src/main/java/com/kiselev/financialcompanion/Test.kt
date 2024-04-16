import java.text.SimpleDateFormat
import java.util.*

data class Transaction(
    val idTransaction: Int,
    val date: Date,
    val amount: Int,
    val description: String,
    val type: Int,
    val category: Int,
    val account: Int
)

fun main() {
    val transactionsString = "[Transaction(idTransaction=0, date=2024-04-11 17:26:13, amount=500, description=Покупка 1, type=1, category=1, account=0), Transaction(idTransaction=0, date=2024-04-11 14:15:13, amount=4500, description=Покупка 3, type=1, category=1, account=0), Transaction(idTransaction=0, date=2024-04-11 12:29:13, amount=6300, description=Покупка 2, type=1, category=1, account=0), Transaction(idTransaction=0, date=2024-04-07 04:12:13, amount=310, description=Покупка 4, type=1, category=1, account=0)]"
    val transactions = parseTransactions(transactionsString)
    val result = calculateDailyTransactionSum(transactions)
    println(formatResult(result))
}

fun parseTransactions(transactionsString: String): List<Transaction> {
    val pattern = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val matcher = Regex(pattern).findAll(transactionsString)
    val dates = matcher.map { dateFormat.parse(it.value) }.toList()

    val amounts = transactionsString.split("amount=").drop(1).map { it.substringBefore(',') }.map { it.toInt() }
    val types = transactionsString.split("type=").drop(1).map { it.substringBefore(',') }.map { it.toInt() }

    return dates.zip(amounts.zip(types)).map { (date, pair) ->
        Transaction(0, date, pair.first, "", pair.second, 0, 0)
    }
}

fun calculateDailyTransactionSum(transactions: List<Transaction>): List<Pair<Date, Int>> {
    val dailyTransactionSum = mutableMapOf<Date, Int>()

    transactions.forEach { transaction ->
        val date = transaction.date
        val sum = dailyTransactionSum[date] ?: 0
        if (transaction.type == 1) {
            dailyTransactionSum[date] = sum - transaction.amount
        } else {
            dailyTransactionSum[date] = sum + transaction.amount
        }
    }

    return dailyTransactionSum.toList()
}


fun formatResult(result: List<Pair<Date, Int>>): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val formattedResult = StringBuilder()

    result.forEach { (date, sum) ->
        formattedResult.append("${dateFormat.format(date)}: ${if (sum >= 0) "+" else "-"}${Math.abs(sum)}\n")
    }

    return formattedResult.toString()
}
