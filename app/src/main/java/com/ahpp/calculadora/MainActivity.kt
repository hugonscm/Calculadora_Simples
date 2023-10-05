package com.ahpp.calculadora

import android.os.Bundle
import android.util.Log
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
            ChangeSystemBarsTheme(!isSystemInDarkTheme())
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            Locale.setDefault(Locale("en", "US"))

            //numeros
            binding.btZero.setOnClickListener{ AcrescentarExpressao("0", true)
                canAddOperation = true}
            binding.btUm.setOnClickListener{AcrescentarExpressao("1", true)
                canAddOperation = true}
            binding.btDois.setOnClickListener{AcrescentarExpressao("2", true)
                canAddOperation = true}
            binding.btTres.setOnClickListener{AcrescentarExpressao("3", true)
                canAddOperation = true}
            binding.btQuatro.setOnClickListener{AcrescentarExpressao("4", true)
                canAddOperation = true}
            binding.btCinco.setOnClickListener{AcrescentarExpressao("5", true)
                canAddOperation = true}
            binding.btSeis.setOnClickListener{AcrescentarExpressao("6", true)
                canAddOperation = true}
            binding.btSete.setOnClickListener{AcrescentarExpressao("7", true)
                canAddOperation = true}
            binding.btOito.setOnClickListener{AcrescentarExpressao("8", true)
                canAddOperation = true}
            binding.btNove.setOnClickListener{AcrescentarExpressao("9", true)
                canAddOperation = true}
            binding.btPonto.setOnClickListener{AcrescentarExpressao(".", true)
                canAddOperation = false}

            //operadores
            binding.soma.setOnClickListener {
                if (canAddOperation){
                    AcrescentarExpressao("+", false)
                    canAddOperation = false
                    canAddDot= true
                } else {
                    avisoExpressao()
                }
            }
            binding.subtracao.setOnClickListener {
                if (canAddOperation){
                    AcrescentarExpressao("-", false)
                    canAddOperation = false
                    canAddDot= true
                } else {
                    avisoExpressao()
                }
            }
            binding.divisao.setOnClickListener {
                if (canAddOperation){
                    AcrescentarExpressao("/", false)
                    canAddOperation = false
                    canAddDot= true
                } else {
                    avisoExpressao()
                }
            }
            binding.multiplicacao.setOnClickListener {
                if (canAddOperation){
                    AcrescentarExpressao("*", false)
                    canAddOperation = false
                    canAddDot= true
                } else {
                    avisoExpressao()
                }
            }
            binding.limpar.setOnClickListener {
                binding.expressao.text = ""
                binding.resultado.text = ""
                canAddOperation = false
                canAddDot= true
            }

            binding.btDeletar.setOnClickListener {
                var string = binding.expressao.text.toString()
                if (string.isNotBlank()) {
                    if (string.last() == '.')
                        canAddDot = true
                    string = string.substring(0, string.length - 1)
                    binding.expressao.text = string

                    canAddOperation = if (string.isNotBlank()){
                        !(string.last() == '*' || string.last() == '-' || string.last() == '+' ||  string.last() == '/')
                    } else
                        false
                }
                binding.resultado.text = ""
            }

            binding.btResultado.setOnClickListener {
                try {
                    val expressao = ExpressionBuilder(binding.expressao.text.toString()).build()

                    val resultado = expressao.evaluate()
                    val longResult = resultado.toLong()

                    if (resultado == longResult.toDouble()){
                        //resultado int
                        binding.resultado.text = longResult.toString()
                    } else {
                        //resultado double
                        val result = String.format("%.9f", resultado).toDouble()
                        binding.resultado.text = result.toString()
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
        if (lightTheme) {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.light(Color.White.toArgb(), mycollor.toArgb()),
                navigationBarStyle = SystemBarStyle.light(mycollor.toArgb(), mycollor.toArgb()))
        } else {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.dark(Color.Black.toArgb()),
                navigationBarStyle = SystemBarStyle.dark(Color.Black.toArgb()))
        }
    }

    fun AcrescentarExpressao(string: String, limparDados: Boolean){
        if (binding.resultado.text.isNotEmpty()){
            binding.expressao.text = ""
        }

        if (limparDados){
            binding.resultado.text = ""
            if (string == "." && canAddDot){
                binding.expressao.append(string)
                canAddDot = false
            } else if (!canAddDot && string == ".") {
                Toast.makeText(this, "Ponto já adicionado",Toast.LENGTH_SHORT).show()
            } else {
                binding.expressao.append(string)
            }
        } else {
            binding.expressao.append(binding.resultado.text)
            binding.expressao.append(string)
            binding.resultado.text = ""
        }
    }

    fun avisoExpressao(){
        Toast.makeText(this, "Digite uma expressão válida!",Toast.LENGTH_SHORT).show()
    }

}