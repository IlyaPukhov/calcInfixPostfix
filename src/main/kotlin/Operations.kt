import kotlin.math.pow
import kotlin.math.sin

class Operations {
    fun infixToPostfix(infixExpr: List<String>): List<String> {
        val stackOperators = mutableListOf<String>() // создаём стек для операторов
        val postfix = mutableListOf<String>() // создаем очередь для чисел ( => и результата)
        infixExpr.forEach {
            when {
                Regex("[\\d]").containsMatchIn(it) -> // если данный элемент выражения число
                    postfix.add(it) // то добавляем его в очередь
                it == "(" -> // если данный элемент выражения открывающая скобка
                    stackOperators.add(it) // то добавляем её в стек
                it == ")" -> { // если данный элемент выражения закрывающая скобка
                    while (stackOperators.isNotEmpty() && stackOperators.last() != "(") // пока стек не пустой и последний элемент не открывающая скобка
                        postfix.add(stackOperators.removeLast()) // pop в очередь оператора из стека
                    stackOperators.removeLast() // удаляем из стека открывающую скобку
                }
                else -> { // в остальных случаях
                    while (stackOperators.isNotEmpty() && priority(it) <= priority(stackOperators.last())) { // пока стек не пустой и приоритет данной операции не превыщает приоритета операции из стека
                        postfix.add(stackOperators.removeLast()) // pop в очередь оператора из стека
                    }
                    stackOperators.add(it) // добавляем данный оператор в стек
                }
            }
        }
        // Когда перебрали все элементы выражения, то добавляем операторы из стека в очередь
        while (stackOperators.isNotEmpty()) {
            if (stackOperators.last() == "(") throw Error("Неверное выражение!")
            postfix.add(stackOperators.removeLast())
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
        return calcPostfixEx(infixToPostfix(expr)) // Преобразуем инфиксное выражение в постфиксное и вычисляем его значение
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