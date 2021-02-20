package com.mahmoudalim.calculatorchallenge

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mahmoudalim.calculatorchallenge.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainVM
    private var operationIsSelected = false
    private var oneOperationSelected = false
    private var displayIsNotEmpty = false
    private var firstOperand = 0
    private var secondOperand = 0
    private lateinit var operationSign: String
    private var n = 1
    private var allOperHistory = mutableListOf<String>("0")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainVM::class.java)
        inItLayout()
        viewModel.result.observe(this, Observer {
            binding.resultTv.text = it
            firstOperand = it.toDouble().roundToInt()
//            allOperHistory.add(allOperHistory.lastIndex+1, it.toString())
            allOperHistory.add(n, it.toString())
            n++
            Log.i("cc", allOperHistory.toString())
        })

    }

    private fun inItLayout() {
        binding.displayEt.addTextChangedListener {
            var input = it.toString()
            if (it.toString().isNotEmpty()) {
                displayIsNotEmpty = true
                secondOperand = Integer.parseInt(input)
                binding.equaleBtn.isEnabled = displayIsNotEmpty && operationIsSelected
            } else binding.equaleBtn.isEnabled = false
        }
    }


    fun multiply(view: View) {
        operationSign = "*"
        operationIsSelected = true
        oneOperationSelected = true
        if (oneOperationSelected) {
            binding.plusBtn.isEnabled = false
            binding.minusBtn.isEnabled = false
            binding.divideBtn.isEnabled = false
        }
    }


    fun divide(view: View) {
        operationSign = "/"
        operationIsSelected = true
        oneOperationSelected = true
        if (oneOperationSelected) {
            binding.plusBtn.isEnabled = false
            binding.minusBtn.isEnabled = false
            binding.divideBtn.isEnabled = false
        }
    }

    fun minus(view: View) {
        operationSign = "-"
        operationIsSelected = true
        oneOperationSelected = true
        if (oneOperationSelected) {
            binding.plusBtn.isEnabled = false
            binding.multiplyBrn.isEnabled = false
            binding.divideBtn.isEnabled = false
        }
    }

    fun plus(view: View) {
        operationSign = "+"
        operationIsSelected = true
        oneOperationSelected = true
        if (oneOperationSelected) {
            binding.multiplyBrn.isEnabled = false
            binding.minusBtn.isEnabled = false
            binding.divideBtn.isEnabled = false
        }

    }

    fun undo(view: View) {
        if (!binding.redoBtn.isEnabled)
        binding.redoBtn.isEnabled = true
        if (n > 1) {
            binding.resultTv.text = allOperHistory[n - 2]
            allOperHistory.add(allOperHistory.lastIndex+1 , allOperHistory[n - 2])
            Log.i("cc", allOperHistory.toString())
            firstOperand = allOperHistory[n - 2].toDouble().roundToInt()
            n--

        } else binding.undoBtn.isEnabled = false

    }

    fun redo(view: View) {
        binding.redoBtn.isEnabled = false
        binding.undoBtn.isEnabled = true
        binding.resultTv.text = allOperHistory[n]
        allOperHistory.add(allOperHistory.lastIndex+1 , allOperHistory[n])
        Log.i("cc", allOperHistory.toString())
        firstOperand = allOperHistory[n].toDouble().roundToInt()
        n++

    }

    fun equal(view: View) {
        binding.undoBtn.isEnabled = true
        viewModel.calResult(firstOperand, operationSign, secondOperand)
        enableAllOperationBtns()
        binding.displayEt.text.clear()
       n = allOperHistory.lastIndex+1
//        allhistory.add(n , operationSign + secondOperand)
//        Log.i("cc" , allhistory[n])
//        val allhistoryViews = mutableListOf<TextView>()
//        allhistoryViews.add(n , TextView(this))
//        allhistoryViews[n].text = allhistory[n]

    }


    private fun enableAllOperationBtns() {
        binding.multiplyBrn.isEnabled = true
        binding.minusBtn.isEnabled = true
        binding.divideBtn.isEnabled = true
        binding.plusBtn.isEnabled = true
    }


}