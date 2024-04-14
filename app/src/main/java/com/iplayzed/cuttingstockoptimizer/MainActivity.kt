package com.iplayzed.cuttingstockoptimizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.iplayzed.cuttingstockoptimizer.ui.theme.CuttingStockOptimizerTheme
import org.ojalgo.optimisation.ExpressionsBasedModel
import org.ojalgo.optimisation.Variable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CuttingStockOptimizerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CuttingStockOptimizerTheme {
        Greeting("Android")
    }
}

private fun performOptimization(){
    val model = ExpressionsBasedModel()

    // Felhasználó által megadott adatok
    val products = listOf("40x40", "40x60", "60x60", "60x80", "80x80")
    val requirements = mapOf("40x40" to 10, "40x60" to 20, "60x60" to 5, "60x80" to 10, "80x80" to 20)
    val variables = mutableMapOf<String, Variable>()
    val binaryVariables = mutableMapOf<String, Variable>()

    // Változók létrehozása
    for (product in products) {
        val x = Variable.make("x_$product").lower(0).integer(true).weight(1)
        val y = Variable.make("y_$product").binary().weight(1)
        model.addVariable(x)
        model.addVariable(y)
        variables[product] = x
        binaryVariables[product] = y
    }

    // Korlátok beállítása
    for ((product, requirement) in requirements) {
        val expression = model.addExpression("Require_$product").lower(requirement.toDouble())
        expression.set(variables[product], 1)
        // Kapcsolat az x és y változók között
        val linkExpression = model.addExpression("Link_$product").upper(0)
        linkExpression.set(variables[product], 1)
        linkExpression.set(binaryVariables[product], -100)
    }

    // Célfüggvény: Minimális gyártási és átállási napok
    val result = model.minimise()
}