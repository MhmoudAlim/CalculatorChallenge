package com.mahmoudalim.calculatorchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mahmoudalim.calculatorchallenge.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun multiply(view: View) {}
    fun divide(view: View) {}
    fun minus(view: View) {}
    fun plus(view: View) {}
    fun undo(view: View) {}
    fun redo(view: View) {}
    fun equal(view: View) {}
}