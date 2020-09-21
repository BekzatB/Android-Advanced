package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.calculator.presenter.CalculatorContract
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity(), CalculatorContract.View {
    private val presenter: CalculatorContract.Presenter by inject { parametersOf(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListener()
    }

    private fun setListener() {
        button0.setOnClickListener {
            presenter.addValue(button0.text.toString())
        }
        button1.setOnClickListener {
            presenter.addValue(button1.text.toString())
        }
        button2.setOnClickListener {
            presenter.addValue(button2.text.toString())
        }
        button3.setOnClickListener {
            presenter.addValue(button3.text.toString())
        }
        button4.setOnClickListener {
            presenter.addValue(button4.text.toString())
        }
        button5.setOnClickListener {
            presenter.addValue(button5.text.toString())
        }
        button6.setOnClickListener {
            presenter.addValue(button6.text.toString())
        }
        button7.setOnClickListener {
            presenter.addValue(button7.text.toString())
        }
        button8.setOnClickListener {
            presenter.addValue(button8.text.toString())
        }
        button9.setOnClickListener {
            presenter.addValue(button9.text.toString())
        }
        buttonPlus.setOnClickListener {
            presenter.setOperation(buttonPlus.text.toString())
        }
        buttonMinus.setOnClickListener {
            presenter.setOperation(buttonMinus.text.toString())
        }
        buttonMultiple.setOnClickListener {
            presenter.setOperation(buttonMultiple.text.toString())
        }
        buttonDelete.setOnClickListener {
            presenter.setOperation(buttonDelete.text.toString())
        }
        buttonEqual.setOnClickListener {
            presenter.calculate()
        }
        buttonDot.setOnClickListener {
            presenter.addValue(buttonDot.text.toString())
        }
        buttonDel.setOnClickListener {
            presenter.delete()
        }
        buttonCe.setOnClickListener {
            presenter.clear()
        }
    }

    override fun setValueView(value: String) {
        displayText.text = value
    }

    override fun setError() {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
    }
}