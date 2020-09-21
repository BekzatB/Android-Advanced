package com.example.calculator.presenter

import android.util.Log

class CalculatorPresenter(
        private var view: CalculatorContract.View?
) : CalculatorContract.Presenter {
    private var inputView = ""
    private var tempVal = ""
    private var tempOperation = ""
    private var firstValue: String = ""
    private var secondValue: String = ""

    override fun addValue(value: String) {
        if (value == ".") {
            if (!tempVal.contains(value) && tempVal.isNotEmpty()) {
                inputView += value
                tempVal += value
            }
        } else if (tempVal == "0") {
            tempVal = value
        } else {
            tempVal += value
            inputView += value
        }

        view?.setValueView(inputView)
    }

    override fun setOperation(operation: String) {
        if (firstValue.isEmpty()) {
            inputView += operation
            firstValue = tempVal
            if (firstValue.last() == '.') {
                firstValue = firstValue.dropLast(1)
                inputView.filter {
                    it != '.'
                }
            }
            tempVal = ""
        } else if (firstValue.isNotEmpty() && secondValue.isEmpty() && tempVal.isNotEmpty()) {
            secondValue = tempVal
            if (secondValue.last() == '.') {
                secondValue = secondValue.dropLast(1)
                inputView.filter {
                    it != '.'
                }
            }
            tempVal = ""
        } else if (firstValue.isNotEmpty() && secondValue.isEmpty()) {
            if (tempOperation.isNotEmpty()) {
                inputView = inputView.dropLast(1)
            }
            inputView += operation
        }
        if (firstValue.isNotEmpty() && secondValue.isNotEmpty()) {
            when (tempOperation) {
                "+" -> firstValue = add(firstValue, secondValue)
                "-" -> firstValue = minus(firstValue, secondValue)
                "*" -> firstValue = multiply(firstValue, secondValue)
                "/" -> firstValue = divide(firstValue, secondValue)
            }
            inputView = firstValue + operation
            secondValue = ""
        }
        tempOperation = operation
        view?.setValueView(inputView)

    }

    private fun add(value1: String, value2: String): String {
        return (value1.toDouble() + value2.toDouble()).toString()
    }

    private fun minus(value1: String, value2: String): String {
        return (value1.toDouble() - value2.toDouble()).toString()
    }

    private fun multiply(value1: String, value2: String): String {
        return (value1.toDouble() * value2.toDouble()).toString()
    }

    private fun divide(value1: String, value2: String): String {
        return (value1.toDouble() / value2.toDouble()).toString()
    }

    override fun calculate() {
        if (!inputView.last().isDigit()) {
            view?.setError()
        } else if (firstValue.isNotEmpty() && secondValue.isEmpty() && tempVal.isNotEmpty()) {
            secondValue = tempVal
            tempVal = ""
        } else if (firstValue.isEmpty() || secondValue.isEmpty()) {
            view?.setError()
        }

        if (firstValue.isNotEmpty() && secondValue.isNotEmpty()) {
            when (tempOperation) {
                "+" -> inputView = add(firstValue, secondValue)
                "-" -> inputView = minus(firstValue, secondValue)
                "*" -> inputView = multiply(firstValue, secondValue)
                "/" -> inputView = divide(firstValue, secondValue)
            }
            tempOperation = ""
            tempVal = inputView
            firstValue = ""
            secondValue = ""
            view?.setValueView(inputView)
        }
    }

    override fun onDestroy() {
        view = null
    }

    override fun delete() {
        if (inputView.isNotEmpty()) {
            Log.d("Beks", "$inputView $tempVal $firstValue")
            if (inputView.last().isDigit() && tempVal.isNotEmpty() && firstValue.isNotEmpty()) {
                tempVal = tempVal.dropLast(1)
                inputView = inputView.dropLast(1)
            } else if (inputView.last().isDigit() && tempVal.isEmpty() && firstValue.isNotEmpty()) {
                inputView = inputView.dropLast(1)
                firstValue = firstValue.dropLast(1)
            } else if (inputView.last().isDigit() && tempVal.isNotEmpty() && firstValue.isEmpty()) {
                tempVal = tempVal.dropLast(1)
                inputView = inputView.dropLast(1)
            } else if (inputView.last() == '.') {
                inputView = inputView.dropLast(1)
                tempVal = tempVal.dropLast(1)
            } else if (!inputView.last().isDigit() && !inputView.last().isLetter()) {
                inputView = inputView.dropLast(1)
                tempOperation = tempOperation.dropLast(1)
            } else {
                clear()
            }
        }
        view?.setValueView(inputView)
    }

    override fun clear() {
        firstValue = ""
        secondValue = ""
        tempVal = ""
        inputView = ""
        tempOperation = ""
        view?.setValueView(inputView)
    }
}

interface CalculatorContract {
    interface Presenter {
        fun addValue(value: String)
        fun setOperation(operation: String)
        fun calculate()
        fun delete()
        fun clear()
        fun onDestroy()
    }

    interface View {
        fun setValueView(value: String)
        fun setError()
    }
}
