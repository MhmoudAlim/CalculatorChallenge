package com.mahmoudalim.calculatorchallenge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainVM : ViewModel() {

    val result : MutableLiveData<String> = MutableLiveData()

    fun calResult(firstOperand: Int , secondOperand: Int) {
        val calculation = (firstOperand + secondOperand).toString()
        result.postValue(calculation)
    }
}