package com.ahpp.calculadora

import android.os.Bundle
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.ahpp.calculadora.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    var canAddOperation: Boolean = false
    var canAddDot: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            ChangeSystemBarsTheme(!isSystemInDarkTheme())

            Locale.setDefault(Locale("en", "US"))

            // Disable the keyboard on display EditText
            binding.input.showSoftInputOnFocus = false

            // Focus by default
            binding.input.requestFocus()

            //ISSO AQUI DA ERRO DE VEZ ENQUANDO NAO SEI PQ
            binding.btLimpar.setOnLongClickListener {
                binding.input.setText("")
                binding.resultDisplay.text = ""
                true
            }

            //numeros
            binding.bt0.setOnClickListener{ AcrescentarExpressao("0", true)
                canAddOperation = true}
            binding.bt1.setOnClickListener{AcrescentarExpressao("1", true)
                canAddOperation = true}
            binding.bt2.setOnClickListener{AcrescentarExpressao("2", true)
                canAddOperation = true}
            binding.bt3.setOnClickListener{AcrescentarExpressao("3", true)
                canAddOperation = true}
            binding.bt4.setOnClickListener{AcrescentarExpressao("4", true)
                canAddOperation = true}
            binding.bt5.setOnClickListener{AcrescentarExpressao("5", true)
                canAddOperation = true}
            binding.bt6.setOnClickListener{AcrescentarExpressao("6", true)
                canAddOperation = true}
            binding.bt7.setOnClickListener{AcrescentarExpressao("7", true)
                canAddOperation = true}
            binding.bt8.setOnClickListener{AcrescentarExpressao("8", true)
                canAddOperation = true}
            binding.bt9.setOnClickListener{AcrescentarExpressao("9", true)
                canAddOperation = true}
            binding.btVirg.setOnClickListener{AcrescentarExpressao(".", true)
                canAddOperation = false}

            //operadores
            binding.btSom.setOnClickListener {
                if (canAddOperation){
                    AcrescentarExpressao("+", false)
                    canAddOperation = false
                    canAddDot= true
                } else {
                    avisoExpressao()
                }
            }
            binding.btSub.setOnClickListener {
                if (canAddOperation){
                    AcrescentarExpressao("-", false)
                    canAddOperation = false
                    canAddDot= true
                } else {
                    avisoExpressao()
                }
            }
            binding.btDiv.setOnClickListener {
                if (canAddOperation){
                    AcrescentarExpressao("/", false)
                    canAddOperation = false
                    canAddDot= true
                } else {
                    avisoExpressao()
                }
            }
            binding.btMult.setOnClickListener {
                if (canAddOperation){
                    AcrescentarExpressao("*", false)
                    canAddOperation = false
                    canAddDot= true
                } else {
                    avisoExpressao()
                }
            }

            binding.btPow.setOnClickListener {
                if (canAddOperation){
                    AcrescentarExpressao("^", false)
                    canAddOperation = false
                    canAddDot= true
                } else {
                    avisoExpressao()
                }
            }

            binding.btClearAll.setOnClickListener {
                binding.input.setText("")
                binding.resultDisplay.text = ""
                canAddOperation = false
                canAddDot= true
            }

            binding.btLimpar.setOnClickListener {
                var string = binding.input.text.toString()
                if (string.isNotBlank()) {
                    if (string.last() == '.')
                        canAddDot = true
                    string = string.substring(0, string.length - 1)
                    binding.input.setText(string)

                    canAddOperation = if (string.isNotBlank()){
                        !(string.last() == '*' || string.last() == '-' || string.last() == '+' ||  string.last() == '/')
                    } else
                        false
                }
                binding.resultDisplay.text = ""

                //move o cursor para o final quando apaga algo
                binding.input.setSelection(binding.input.text.length)
            }

            binding.btResult.setOnClickListener {
                try {
                    val expressao = ExpressionBuilder(binding.input.text.toString()).build()

                    val resultado = expressao.evaluate()
                    val longResult = resultado.toLong()

                    if (resultado == longResult.toDouble()){
                        //resultado int
                        binding.resultDisplay.text = longResult.toString()
                    } else {
                        //resultado double
                        val result = String.format("%.9f", resultado).toDouble()
                        binding.resultDisplay.text = result.toString()

                    }
                }catch (e: Exception){
                    avisoExpressao()
                }
            }
        }
    }

    @Composable
    private fun ChangeSystemBarsTheme(lightTheme: Boolean) {
        val mycollor = Color.Transparent

        val quasePreta = Color(0xFF000000)
        val quaseBranca = Color(0xFFEFEFEF)

        if (lightTheme) {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.light(Color.White.toArgb(), mycollor.toArgb()),
                navigationBarStyle = SystemBarStyle.light(mycollor.toArgb(), mycollor.toArgb()))
        } else {
            enableEdgeToEdge(
                //muda a cor do fundo para cinza aqui, no modo noturno
                statusBarStyle = SystemBarStyle.dark(mycollor.toArgb()),
                navigationBarStyle = SystemBarStyle.dark(mycollor.toArgb()))

            binding.input.setBackgroundColor(quaseBranca.toArgb())
            binding.resultDisplay.setBackgroundColor(quaseBranca.toArgb())

            binding.inputHorizontalScrollView.setBackgroundColor(quaseBranca.toArgb())
            binding.resultDisplayHorizontalScrollView.setBackgroundColor(quaseBranca.toArgb())

            binding.constrLayout.setBackgroundColor(quasePreta.toArgb())
        }
    }

    fun AcrescentarExpressao(string: String, limparDados: Boolean){
        if (binding.resultDisplay.text.isNotEmpty()){
            binding.input.setText("")
        }

        if (limparDados){
            binding.resultDisplay.text = ""
            if (string == "." && canAddDot){
                binding.input.append(string)
                canAddDot = false
            } else if (!canAddDot && string == ".") {
                Toast.makeText(this, "Ponto já adicionado",Toast.LENGTH_SHORT).show()
            } else {
                binding.input.append(string)
            }
        } else {
            binding.input.append(binding.resultDisplay.text)
            binding.input.append(string)
            binding.resultDisplay.text = ""
        }
    }

    fun avisoExpressao(){
        Toast.makeText(this, "Digite uma expressão válida!",Toast.LENGTH_SHORT).show()
    }

}