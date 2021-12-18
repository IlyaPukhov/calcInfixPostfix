import kotlin.math.pow
import kotlin.math.sin

class Operations {
    fun infixToPostfix(infixString: String): String {
        val infixExpr = parseExpr(infixString)
        val stackOperators = mutableListOf<String>()
        val postfix = mutableListOf<String>()
        infixExpr.forEach {
            when {
                Regex("[\\d]").containsMatchIn(it) ->
                    postfix.add(it)
                it == "(" ->
                    stackOperators.add(it)
                it == ")" -> { // Добавляем в стек все операторы от последнего в стеке пока не дойдем до открывающей скобки
                    while (stackOperators.isNotEmpty() && stackOperators.last() != "(")
                        postfix.add(stackOperators.removeLast())
                    stackOperators.removeLast() // '(' удаляем
                }
                else -> { // Сравниваем приоритет последнего оператора с данным
                    while (stackOperators.isNotEmpty() && priority(it) <= priority(stackOperators.last())) {
                        postfix.add(stackOperators.removeLast())
                    }
                    stackOperators.add(it)
                }
            }
        }
        // Когда перебрали все элементы выражения, то добавляем операторы из стека в очередь
        while (stackOperators.isNotEmpty()) {
            if (stackOperators.last() == "(") throw Error("Неверное выражение!")
            postfix.add(stackOperators.removeLast())
        }
        return postfix.joinToString(" ")
    }

    fun calcPostfixExpr(postfixString: String): Float {
        val expr = parseExpr(postfixString)
        val st = mutableListOf<Float>()
        var r: Float
        var l: Float
        expr.forEach {
            try {
                when (it) {
                    "+" -> {
                        r = st.removeLast()
                        l = st.removeLast()
                        st.add(l + r)
                    }
                    "-" -> {
                        r = st.removeLast()
                        l = st.removeLast()
                        st.add(l - r)
                    }
                    "*" -> {
                        r = st.removeLast()
                        l = st.removeLast()
                        st.add(l * r)
                    }
                    "/" -> {
                        r = st.removeLast()
                        l = st.removeLast()
                        st.add(l / r)
                    }
                    "^" -> {
                        r = st.removeLast()
                        l = st.removeLast()
                        st.add(l.pow(r))
                    }
                    "sin" -> {
                        r = st.removeLast()
                        st.add(sin(r))
                    }
                    else -> st.add(it.toFloat())
                }
            } catch (e: NoSuchElementException) { // Если количество чисел в стеке недостаточно для операции, выдаём ошибку
                throw Error("Неверное выражение!")
            }
        }
        if (st.size != 1) { // Если после всех операций в стеке осталось более 1 числа, выдаём ошибку
            throw Error("Неверное выражение!")
        }
        return st[0] // Если в стеке осталось 1 число, то возвращаем его (это и есть результат выражения)
    }

    fun calcInfixExpr(infixString: String): Float {
        val postfixExpr = infixToPostfix(infixString)
        println("Преобразуем инфиксную форму записи в постфиксную: $postfixExpr\n" +
        "Считаем значение постфиксного выражения...")
        return calcPostfixExpr(postfixExpr)
    }
}

fun parseExpr(expr: String): List<String> {
    val pattern = "-?(\\d+\\.)?\\d+|\\(|\\)|\\+|-|\\*|/|\\^|sin".toRegex()
    return pattern.findAll(expr).map { it.value }.toList()
}

fun priority(operator: String): Int = when (operator) {
    "+", "-" -> 1
    "*", "/" -> 2
    "^" -> 3
    "sin" -> 4
    else -> -1
}