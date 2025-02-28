package com.example.appcalculadora

import android.app.Activity
import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class MainActivity : AppCompatActivity() {

    val historyList = mutableListOf<String>()
    var operacionActual =""
    var primerNumero: Double = Double.NaN
    var segundoNumero: Double = Double.NaN
    lateinit var tvTemp: TextView
    lateinit var tvResult: TextView
    lateinit var formatoDecimal: DecimalFormat

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("history", ArrayList(historyList))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        formatoDecimal = DecimalFormat("#.#####")
        tvTemp = findViewById(R.id.tvTemp)
        tvResult = findViewById(R.id.tvResult)


        savedInstanceState?.let {
            val savedHistory = it.getStringArrayList("history")
            if (savedHistory != null) {
                historyList.addAll(savedHistory)
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val darkModeSwitch = findViewById<SwitchMaterial>(R.id.material_switch)
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }


    fun cambiarOperador(b: View) {
        val boton: Button = b as Button
        val op = if (boton.text.toString().trim() == "X") "*" else boton.text.toString().trim()


        val currentValue = tvTemp.text.toString().trim().toDoubleOrNull()

        if (currentValue != null) {
            if (primerNumero.isNaN()) {
                primerNumero = currentValue
            } else {
                calcular()  // Evalúa la operación pendiente con el número actual.
            }
            operacionActual = op
            // Se muestra el resultado parcial junto con el operador.
            tvResult.text = formatoDecimal.format(primerNumero) + operacionActual
            tvTemp.setText("")
        } else {
            tvTemp.append(op)
        }
    }



    fun insertarSin(view: View) {
        val currentText = tvTemp.text.toString()
        val insertText = "sin("
        val newText = currentText + insertText
        tvTemp.setText(newText)
    }

    fun insertarCos(view: View) {
        val currentText = tvTemp.text.toString()
        val insertText = "cos("
        val newText = currentText + insertText
        tvTemp.setText(newText)
    }

    fun insertarTan(view: View) {
        val currentText = tvTemp.text.toString()
        val insertText = "tan("
        val newText = currentText + insertText
        tvTemp.setText(newText)
    }

    fun insertarRaiz(view: View) {
        val currentText = tvTemp.text.toString()
        val insertText = "√("
        val newText = currentText + insertText
        tvTemp.setText(newText)
    }

    fun insertarAbrirParentesis(view: View) {
        tvTemp.append("(")
    }

    fun insertarCerrarParentesis(view: View) {
        tvTemp.append(")")
    }


    fun seleccionarNumero(b: View){
        val boton:Button = b as Button
        if (tvTemp.text.toString()=="0"){
            tvTemp.text=""
        }
        tvTemp.text = tvTemp.text.toString() + boton.text.toString()
    }

    fun calcular(){
        try {
            val input = tvTemp.text.toString().trim()

            // Evaluación de funciones avanzadas (sin, cos, tan, √) – se mantiene igual.
            if (input.startsWith("sin(") && input.endsWith(")")) {
                val inner = input.substring("sin(".length, input.length - 1)
                val value = inner.toDoubleOrNull()
                if (value != null) {
                    // Convertir de grados a radianes, si se desea trabajar en grados
                    primerNumero = sin(Math.toRadians(value))
                    tvResult.text = formatoDecimal.format(primerNumero)
                } else {
                    Toast.makeText(this, "Entrada inválida en sin()", Toast.LENGTH_SHORT).show()
                }
                tvTemp.setText("")
                operacionActual = ""
                return
            }
            if (input.startsWith("cos(") && input.endsWith(")")) {
                val inner = input.substring("cos(".length, input.length - 1)
                val value = inner.toDoubleOrNull()
                if (value != null) {
                    primerNumero = cos(Math.toRadians(value))
                    tvResult.text = formatoDecimal.format(primerNumero)
                } else {
                    Toast.makeText(this, "Entrada inválida en cos()", Toast.LENGTH_SHORT).show()
                }
                tvTemp.setText("")
                operacionActual = ""
                return
            }
            if (input.startsWith("tan(") && input.endsWith(")")) {
                val inner = input.substring("tan(".length, input.length - 1)
                val value = inner.toDoubleOrNull()
                if (value != null) {
                    primerNumero = tan(Math.toRadians(value))
                    tvResult.text = formatoDecimal.format(primerNumero)
                } else {
                    Toast.makeText(this, "Entrada inválida en tan()", Toast.LENGTH_SHORT).show()
                }
                tvTemp.setText("")
                operacionActual = ""
                return
            }
            if (input.startsWith("√(") && input.endsWith(")")) {
                val inner = input.substring("√(".length, input.length - 1)
                val value = inner.toDoubleOrNull()
                if (value != null) {
                    if (value < 0) {
                        Toast.makeText(this, "No se puede calcular la raíz de un número negativo", Toast.LENGTH_SHORT).show()
                        return
                    }
                    primerNumero = sqrt(value)
                    tvResult.text = formatoDecimal.format(primerNumero)
                } else {
                    Toast.makeText(this, "Entrada inválida en √()", Toast.LENGTH_SHORT).show()
                }
                tvTemp.setText("")
                operacionActual = ""
                return
            }

            if (primerNumero.toString() != "NaN") {
                val secondValue = tvTemp.text.toString().toDoubleOrNull()
                if (secondValue == null) {
                    Toast.makeText(this, "Operación incompleta", Toast.LENGTH_SHORT).show()
                    return
                }
                segundoNumero = secondValue
                tvTemp.setText("")
                when (operacionActual) {
                    "+" -> primerNumero += segundoNumero
                    "-" -> primerNumero -= segundoNumero
                    "*" -> primerNumero *= segundoNumero
                    "/" -> primerNumero /= segundoNumero
                    "%" -> primerNumero %= segundoNumero
                }
            } else {

                val numberValue = tvTemp.text.toString().toDoubleOrNull()
                if (numberValue == null) {
                    Toast.makeText(this, "Entrada inválida", Toast.LENGTH_SHORT).show()
                    return
                }
                primerNumero = numberValue
            }
        } catch (e: Exception) {

        }
    }


    fun igual(b: View) {
        calcularExpresion()
        val resultado = formatoDecimal.format(primerNumero)
        historyList.add("Resultado: $resultado")
        Log.d("MainActivity", "Historial actualizado: $historyList")
    }

    fun borrar(b: View){
        val boton:Button = b as Button
        if (boton.text.toString().trim()== "C"){
            if (tvTemp.text.toString().isNotEmpty()){
                var datosActuales:CharSequence = tvTemp.text as CharSequence
                tvTemp.text = datosActuales.subSequence(0,datosActuales.length-1)
            }else {
                primerNumero = Double.NaN
                segundoNumero = Double.NaN
                tvTemp.text =""
                tvResult.text =""

            }

        }else if(boton.text.toString().trim()=="CA"){
            primerNumero = Double.NaN
            segundoNumero = Double.NaN
            tvTemp.text =""
            tvResult.text =""
        }

    }

    companion object {
        const val HISTORY_REQUEST_CODE = 1
    }

    fun mostrarHistorial(b: View) {
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putStringArrayListExtra("history", ArrayList(historyList))
        startActivityForResult(intent, HISTORY_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HISTORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra("selectedOperation")?.let { selectedOperation ->
                // Extrae la parte numérica después de "Resultado:".
                val resultValue = selectedOperation.substringAfter(":").trim()
                primerNumero = resultValue.toDoubleOrNull() ?: Double.NaN
                tvTemp.text = resultValue
            }
        }
    }



    fun calcularExpresion() {
        try {
            var exprString = if (tvResult.text.toString().isNotEmpty()) {
                tvResult.text.toString() + tvTemp.text.toString()
            } else {
                tvTemp.text.toString().trim()
            }
            if (exprString.isEmpty()) {
                Toast.makeText(this, "No hay expresión para evaluar", Toast.LENGTH_SHORT).show()
                return
            }
            // Reemplaza el símbolo de raíz "√(" por "sqrt(" para que exp4j lo reconozca.
            exprString = exprString.replace("√(", "sqrt(")

            val expression = ExpressionBuilder(exprString).build()
            val result = expression.evaluate()

            primerNumero = result
            tvResult.text = formatoDecimal.format(result)
            tvTemp.setText("")
            operacionActual = ""
        } catch (e: Exception) {
            Toast.makeText(this, "Error en la expresión", Toast.LENGTH_SHORT).show()
        }
    }



}