import kotlin.math.pow
import kotlin.math.sin

fun main() {
    println("Добро пожаловать в Калькулятор \"Infix to postfix\", он может преобразовывать инфиксные выражения в постфиксные, а также вычислять значение постфиксного выражения")
    println("Обладает довольно скудным функционалом и может предложить только следующие операции:")
    println("Сложение(+), Вычитание(-), Умножение(*), Деление(/), Возведение в степень(^), Нахождение sin от числа(sin)")

    println("Входное выражение: ")
    val expr: String = readLine().toString()

    println("Что вы хотите сделать с данным выражением?")
    println("1) Преобразовать инфиксное выражение в постфиксное")
    println("2) Вычислить значение постфиксного выражения")
    println("3) Вычислить значение инфиксного выражения")
    print("Введите номер нужной вам операции: ")

    val mode: String = readLine().toString()
    when {
        mode.contains("1") ->
            println("Постфиксная форма записи заданного выражения: ${Operations().infixToPostfix(parseEx(expr)).joinToString(" ")}")
        mode.contains("2") ->
            println("Значение постфиксного выражения: ${Operations().calcPostfixEx(parseEx(expr))}")
        mode.contains("3") ->
            println("Значение инфиксного выражения: ${Operations().calcInfixEx(parseEx(expr))}")
        else ->
            throw Error("Ошибка! Попробуйте ещё раз.")
    }
}


