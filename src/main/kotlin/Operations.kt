import kotlin.math.pow
import kotlin.math.sin

class Operations {
    fun infixToPostfix(infixExpr: List<String>): List<String> {
        val operators = mutableListOf<String>()
        val postfix = mutableListOf<String>()
        infixExpr.forEach {
            when {
                Regex("[\\d]").containsMatchIn(it) ->
                    postfix.add(it)
                it == "(" ->
                    operators.add(it)
                it == ")" -> {
                    while (operators.isNotEmpty() && operators.last() != "(")
                        postfix.add(operators.removeLast())
                    operators.removeLast()
                }
                else -> {
                    while (operators.isNotEmpty() && priority(it) <= priority(operators.last())) {
                        postfix.add(operators.removeLast())
                    }
                    operators.add(it)
                }
            }
        }
        // Когда перебрали все элементы выражения, то добавляем из стека операторы в очередь
        while (operators.isNotEmpty()) {
            if (operators.last() == "(") throw Error("Неверное выражение!")
            postfix.add(operators.removeLast())
        }
        return postfix
    }

    fun calcPostfixEx(expr: List<String>): Float {
        val st = mutableListOf<Float>() // Создаём стек для чисел
        var r: Float
        var l: Float
        expr.forEach {
            try {
                when (it) { // Проходимся по всем элементам массива
                    "+" -> {  // Если встречается "+", то вынимаем 2 последних числа из стека и добавляем в конец их сумму
                        r = st.removeLast()
                        l = st.removeLast()
                        st.add(l + r)
                    }
                    "-" -> { // Если встречается "-", то вынимаем 2 последних числа из стека и добавляем в него их разность
                        r = st.removeLast()
                        l = st.removeLast()
                        st.add(l - r)
                    }
                    "*" -> {  // Если встречается "*", то вынимаем 2 последних числа из стека и добавляем в него их произведение
                        r = st.removeLast()
                        l = st.removeLast()
                        st.add(l * r)
                    }
                    "/" -> {  // Если встречается "/", то вынимаем 2 последних числа из стека и добавляем в конец их частное
                        r = st.removeLast()
                        l = st.removeLast()
                        st.add(l / r)
                    }
                    "^" -> { // Если встречается "^", то вынимаем 2 последних числа из стека и добавляем в конец предпоследнее число в степени последнего
                        r = st.removeLast()
                        l = st.removeLast()
                        st.add(l.pow(r))
                    }
                    "sin" -> { // Если встречается "sin", то вынимаем последнее число из стека и добавляем в конец sin от него
                        r = st.removeLast()
                        st.add(sin(r))
                    }
                    else -> st.add(it.toFloat()) // В остальных случаях добавляем число в стек
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

    fun calcInfixEx(expr: List<String>): Float {
        return calcPostfixEx(infixToPostfix(expr))
    }
}

fun parseEx(expr: String): List<String> {
    val pattern = "-?(\\d+\\.)?\\d+|\\(|\\)|\\+|-|\\*|/|\\^|sin".toRegex()
    return pattern.findAll(expr).map { it.value }.toList()
}

fun priority(operand: String): Int = when (operand) {
    "+", "-" -> 1
    "*", "/" -> 2
    "^" -> 3
    "sin" -> 4
    else -> -1
}