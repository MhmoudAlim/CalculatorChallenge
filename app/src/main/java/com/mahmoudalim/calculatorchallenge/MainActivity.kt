package com.mahmoudalim.calculatorchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun multiply(view: View) {}
    fun divide(view: View) {}
    fun minus(view: View) {}
    fun plus(view: View) {}
    fun undo(view: View) {}
    fun redo(view: View) {}
    fun equal(view: View) {}
}