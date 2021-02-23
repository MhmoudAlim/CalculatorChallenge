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
import com.mahmoudalim.calculatorchallenge.slideInRight
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), MyAdapter.OnItemClickListener {
    /**
    MainActivity global parameters
     */
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainVM
    private lateinit var historyAdapter: MyAdapter
    private var firstOperand = 0
    private var secondOperand = 0
    private lateinit var operationSign: String
    /**
     * n is a counter for the operationsHistory list that is used to (undo/Redo) operations
     */
    private var n = 0
    private var operationsHistory = mutableListOf("0")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * initializing the ViewBinding to inflate the root view and child views when needed
         */
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /**
         *Create a ViewModel the first time the system calls an activity's onCreate() method.
         */
        viewModel = ViewModelProvider(this).get(MainVM::class.java)
        inItLayout()

        /**
         * observing result Livedata to update the UI
         * setting the resultTv TextView to the String received in the Observer
         * updating the operationsHistory List wuth the current operation
         * and updating viewModel.addToHistory method with the new list of the data history of operations
         */
        viewModel.result.observe(this, Observer {
            binding.resultTv.text = it
            firstOperand = it.toDouble().roundToInt()
            if (n != 0)
                operationsHistory.add(n, it.toString())
            viewModel.addToHistory(operationsHistory)
            n++
            historyAdapter.notifyDataSetChanged()
        })
    }

    /**
     *these booleans are to manage the state of the buttons control them in each button click
     */
    private var operationIsSelected = false
    private var oneOperationSelected = false
    private var displayIsNotEmpty = false

    /**
     * this function is to initialized in the Activity onCreate()
     */
    private fun inItLayout() {
        binding.displayEt.isEnabled = true
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
            binding.resultTv.text = operationsHistory[operationsHistory.lastIndex]
            firstOperand = operationsHistory[operationsHistory.lastIndex].toDouble().roundToInt()
            setUpRecyclerView()
        })
    }

    /**
     * this function is triggered on the multiply button onClick()
     */
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

    /**
     * this function is triggered on the divide button onClick()
     * it take a View as @param and doesn't return anything
     */
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

    /**
     * this function is triggered on the minus button onClick()
     * it take a View as @param and doesn't return anything
     */
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

    /**
     * this function is triggered on the plus button onClick()
     * it take a View as @param and doesn't return anything
     */
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

    /**
     * this function is triggered on the undo button onClick()
     * it take a View as @param and doesn't return anything
     * it trigger a helper function to close the soft keyboard so that the result is shown in the collection Views
     * it updates the operationsHistory list and rePosition the pointer in the History of operations
     * decrement the counter of the list to reposition the pointer
     * send the new list to the ViewModel.addToHistory()
     * and finally updates the current firstOperand as it's always not given by the user
     */
    fun undo(view: View) {
        view.hideKeyboard()
        if (!binding.redoBtn.isEnabled)
            binding.redoBtn.isEnabled = true
        if (n > 1) {
            binding.resultTv.text = operationsHistory[n - 2]
            operationsHistory.add(operationsHistory.lastIndex + 1, operationsHistory[n - 2])
            historyAdapter.notifyDataSetChanged()
            binding.historyRv.scrollToPosition(historyAdapter.itemCount - 1)
            viewModel.addToHistory(operationsHistory)
            Log.i("cc", operationsHistory.toString())
            firstOperand = operationsHistory[n - 2].toDouble().roundToInt()
            n--
        } else binding.undoBtn.isEnabled = false
    }

    /**
     * this function is triggered on the redo button onClick()
     * it take a View as @param and doesn't return anything
     * it trigger a helper function to close the soft keyboard so that the result is shown in the collection Views
     * it updates the operationsHistory list and rePosition the pointer in the History of operations
     * increment the counter of the list to reposition the pointer
     * send the new list to the ViewModel.addToHistory()
     * and finally updates the current firstOperand as it's always not given by the user
     */
    fun redo(view: View) {
        view.hideKeyboard()
        binding.redoBtn.isEnabled = false
        binding.undoBtn.isEnabled = true
        if (n < operationsHistory.size)
            binding.resultTv.text = operationsHistory[n]
        operationsHistory.add(operationsHistory.lastIndex + 1, operationsHistory[n])
        viewModel.addToHistory(operationsHistory)
        historyAdapter.notifyDataSetChanged()
        binding.historyRv.scrollToPosition(historyAdapter.itemCount - 1)
        firstOperand = operationsHistory[n].toDouble().roundToInt()
        n++
    }

    /**
     * this function is triggered on the equal button onClick()
     * it take a View as @param and doesn't return anything
     * send the actual values of the equations @firstOperand , @operationSign , @secondOperand
     * to the viewModel.calResult to calculate the operation and return the result to be observed
     * as this is the last operation done alwyas in a life cyclew of the useCase so it trigger the
     * afterEveryOperationButtonRestore() to restore all buttons initial states
     */
    fun equal(view: View) {
        binding.undoBtn.isEnabled = true
        viewModel.calResult(firstOperand, operationSign, secondOperand)
        n = operationsHistory.lastIndex + 1
        viewModel.addToHistory(operationsHistory)

        afterEveryOperationButtonRestore()
        setUpRecyclerView()
        view.hideKeyboard()

    }

    /**
     * this function is triggered on the equal button onClick()
     */
    private fun afterEveryOperationButtonRestore() {
        binding.displayEt.text.clear()
        binding.multiplyBrn.isEnabled = true
        binding.minusBtn.isEnabled = true
        binding.divideBtn.isEnabled = true
        binding.plusBtn.isEnabled = true
    }

    /**
     * this function is triggered on the equal button onClick()
     * setting up the recycler view with the updated history list to be viewd in the recycler Collection Views
     * and always making sure that the list position is in the last item of the HORIZONTAL layout
     */
    private fun setUpRecyclerView() {
        historyAdapter = MyAdapter(operationsHistory, this)
        binding.historyRv.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
            adapter = historyAdapter
            /**
             * a slide right animation function declared in the @ViewAnimExt kotlin file
             * it takes two @param : animTime , startOffest as a Long data type
             */
            slideInRight(100L, 100L)
            scrollToPosition(historyAdapter.itemCount - 1)
        }
    }


    /**
     * this is an override onItemClick fun() to the  OnItemClickListener interface
     * in the @MyAdapter Class to listen to the Cliks in the Recycl;lerViews and updates the
     * Result TExtView with the previous operation result
     * and resetting the current firstOperand
     */
    override fun onItemClick(position: Int) {
        if (position > 0) {
            val clickedItem = operationsHistory[position - 1]
            binding.resultTv.text = clickedItem
            firstOperand = clickedItem.toDouble().roundToInt()
        }
    }

    /**
     * a helper fun to close the current soft keyboard if opened
     */
    private fun View.hideKeyboard() {
        val keyboard = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(windowToken, 0)
    }


}
