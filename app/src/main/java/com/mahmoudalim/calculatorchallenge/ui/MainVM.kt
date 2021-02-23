package com.mahmoudalim.calculatorchallenge.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainVM : ViewModel() {

    private lateinit var calculation: String
    val result: MutableLiveData<String> = MutableLiveData()
    val resultHistory: MutableLiveData<MutableList<String>> = MutableLiveData()


    fun calResult(firstOperand: Int, operationSign: String, secondOperand: Int) {
            when (operationSign) {
                "+" -> calculation = (firstOperand.toDouble() + secondOperand.toDouble()).toString()
                "-" -> calculation = (firstOperand.toDouble() - secondOperand.toDouble()).toString()
                "*" -> calculation = (firstOperand.toDouble() * secondOperand.toDouble()).toString()
                "/" -> calculation = (firstOperand.toDouble() / secondOperand.toDouble()).toString()
            }

            result.postValue(calculation)

    }

    fun addToHistory(operationResult: MutableList<String>) =
        resultHistory.postValue(operationResult)

}