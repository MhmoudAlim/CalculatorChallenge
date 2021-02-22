package com.mahmoudalim.calculatorchallenge.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudalim.calculatorchallenge.adapter.MyAdapter
import com.mahmoudalim.calculatorchallenge.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainVM
    private var firstOperand = 0
    private var secondOperand = 0
    private lateinit var operationSign: String
    private var n = 0
    private var operationsHistory = mutableListOf("0")
    private lateinit var historyAdapter: MyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainVM::class.java)
        inItLayout()
        viewModel.result.observe(this, Observer {
            if (operationsHistory[0] == "0") {
                binding.resultTv.text = it
                firstOperand = it.toDouble().roundToInt()
                if (n != 0)
                    operationsHistory.add(n, it.toString())
            } else operationsHistory.removeAt(0)
            viewModel.addToHistory(operationsHistory)
            n++
            Log.i("cc", "ONCREATE ${operationsHistory.toString()}")
            historyAdapter.notifyDataSetChanged()
        })
    }

    private var operationIsSelected = false
    private var oneOperationSelected = false
    private var displayIsNotEmpty = false

    private fun inItLayout() {
        binding.displayEt.addTextChangedListener {
            val input = it.toString()
            if (it.toString().isNotEmpty()) {
                displayIsNotEmpty = true
                secondOperand = Integer.parseInt(input)
                binding.equaleBtn.isEnabled = displayIsNotEmpty && operationIsSelected
            } else binding.equaleBtn.isEnabled = false
        }

        viewModel.resultHistory.observe(this, Observer {
            operationsHistory = it
            Log.i("cc", "list updated to (INIT) ${it}")
            binding.resultTv.text = operationsHistory[operationsHistory.lastIndex]
            firstOperand = operationsHistory[operationsHistory.lastIndex].toDouble().roundToInt()
            setUpRecyclerView()
        })

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
            binding.resultTv.text = operationsHistory[n - 2]
            operationsHistory.add(operationsHistory.lastIndex + 1, operationsHistory[n - 2])
            historyAdapter.notifyDataSetChanged()
            viewModel.addToHistory(operationsHistory)
            Log.i("cc", operationsHistory.toString())
            firstOperand = operationsHistory[n - 2].toDouble().roundToInt()
            n--
        } else binding.undoBtn.isEnabled = false
        view.hideKeyboard()


    }

    fun redo(view: View) {
        binding.redoBtn.isEnabled = false
        binding.undoBtn.isEnabled = true
        if (n < operationsHistory.size)
            binding.resultTv.text = operationsHistory[n]

        operationsHistory.add(operationsHistory.lastIndex + 1, operationsHistory[n])
        viewModel.addToHistory(operationsHistory)
        historyAdapter.notifyDataSetChanged()
        Log.i("cc", operationsHistory.toString())
        firstOperand = operationsHistory[n].toDouble().roundToInt()
        n++
        view.hideKeyboard()

    }

    fun equal(view: View) {
        binding.undoBtn.isEnabled = true
        viewModel.calResult(firstOperand, operationSign, secondOperand)
        enableAllOperationBtns()
        binding.displayEt.text.clear()
        n = operationsHistory.lastIndex + 1
        viewModel.addToHistory(operationsHistory)
        setUpRecyclerView()
        view.hideKeyboard()

    }

    private fun enableAllOperationBtns() {
        binding.multiplyBrn.isEnabled = true
        binding.minusBtn.isEnabled = true
        binding.divideBtn.isEnabled = true
        binding.plusBtn.isEnabled = true
    }


    private fun setUpRecyclerView() {
        historyAdapter = MyAdapter(operationsHistory)
        binding.historyRv.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
            adapter = historyAdapter
            scrollToPosition(historyAdapter.itemCount - 1)
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


}

