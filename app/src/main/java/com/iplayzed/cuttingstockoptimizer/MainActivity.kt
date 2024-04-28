package com.iplayzed.cuttingstockoptimizer

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.ojalgo.optimisation.ExpressionsBasedModel
import org.ojalgo.optimisation.Variable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wizard_step_one)

        val stoneBlockWidthTextView: TextView = findViewById(R.id.stoneBlockWidth)
        val blockLength1TextView: TextView = findViewById(R.id.blockLength1)
        val blockLength2TextView: TextView = findViewById(R.id.blockLength2)
        val transitionTimeTextView: TextView = findViewById(R.id.transitionTime)
        val generateTableButton: Button = findViewById(R.id.generateTable)

        val widthHorizontalTextView: TextView = findViewById(R.id.widthHorizontal)
        val heightHorizontal1TextView: TextView = findViewById(R.id.heightHorizontal1)
        val heightHorizontal2TextView: TextView = findViewById(R.id.heightHorizontal2)

        val widthVerticalTextView: TextView = findViewById(R.id.widthVertical)
        val heightVertical1TextView: TextView = findViewById(R.id.heightVertical1)
        val heightVertical2TextView: TextView = findViewById(R.id.heightVertical2)


        generateTableButton.setOnClickListener {
            val stoneBlockWidth = stoneBlockWidthTextView.getText().toString().toInt()
            val blockLength1 = blockLength1TextView.getText().toString().toInt()
            val blockLength2 = blockLength2TextView.getText().toString().toInt()
            //val transitionTime = transitionTimeTextView.getText().toString().toInt()

            widthHorizontalTextView.text = stoneBlockWidth.toString()
            widthVerticalTextView.text = stoneBlockWidth.toString()

            heightHorizontal1TextView.text = blockLength1.toString()
            heightVertical1TextView.text = blockLength1.toString()

            heightHorizontal2TextView.text = blockLength2.toString()
            heightVertical2TextView.text = blockLength2.toString()
        }

        val cell11TextView: TextView = findViewById(R.id.cell11)
        val cell12TextView: TextView = findViewById(R.id.cell12)
        val cell13TextView: TextView = findViewById(R.id.cell13)
        val cell22TextView: TextView = findViewById(R.id.cell22)
        val cell23TextView: TextView = findViewById(R.id.cell23)
        val cell33TextView: TextView = findViewById(R.id.cell33)

        val resultDaysTextView: TextView = findViewById(R.id.resultDays)
        val calculateOptimumButton: Button = findViewById(R.id.calculateOptimum)

        calculateOptimumButton.setOnClickListener {
            val cell11: Int = cell11TextView.getText().toString().toInt()
            val cell12: Int = cell12TextView.getText().toString().toInt()
            val cell13: Int = cell13TextView.getText().toString().toInt()
            val cell22: Int = cell22TextView.getText().toString().toInt()
            val cell23: Int = cell23TextView.getText().toString().toInt()
            val cell33: Int = cell33TextView.getText().toString().toInt()

            val result = performOptimization(cell11, cell12, cell13, cell22, cell23, cell33)
            resultDaysTextView.text = result.toString()
        }
    }
}

@Suppress("LocalVariableName")
private fun performOptimization(
    cell11: Int, cell12: Int, cell13: Int,
    cell22: Int, cell23: Int, cell33: Int
): Double {
    val model = ExpressionsBasedModel()

    // These variables represent how much of a given combination is needed.
    // W represents the width of the stone block.
    // H1 represents the first possible height, H2 the second possible height.
    // For instance X_W__W_W represents how much width x width and width x width sized
    // blocks are created.

    // Comments are for concrete 40w,60/80h.

    val X_W__W_W = model.newVariable("X_W__W_W").integer().lower(0) //X444
    val X_W__W_H1 = model.newVariable("X_W__W_H1").integer().lower(0) //X446
    val X_W__W_H2 = model.newVariable("X_W__W_H2").integer().lower(0) //X448
    val X_W__H1_H1 = model.newVariable("X_W__H1_H1").integer().lower(0) //X466
    val X_W__H1_H2 = model.newVariable("X_W__H1_H2").integer().lower(0) //X468
    val X_W__H2_H2 = model.newVariable("X_W__H2_H2").integer().lower(0) //X488
    val X_H1__W_W = model.newVariable("X_H1__W_W").integer().lower(0) //X644
    val X_H1__W_H1 = model.newVariable("X_H1__W_H1").integer().lower(0) //X646
    val X_H1__W_H2 = model.newVariable("X_H1__W_H2").integer().lower(0) //X648
    val X_H1__H1_H1 = model.newVariable("X_H1__H1_H1").integer().lower(0) //X666
    val X_H1__H1_H2 = model.newVariable("X_H1__H1_H2").integer().lower(0) //X668
    val X_H1__H2_H2 = model.newVariable("X_H1__H2_H2").integer().lower(0) //X688
    val X_H2__W_W = model.newVariable("X_H2__W_W").integer().lower(0) //X844
    val X_H2__W_H1 = model.newVariable("X_H2__W_H1").integer().lower(0) //X846
    val X_H2__W_H2 = model.newVariable("X_H2__W_H2").integer().lower(0) //X848
    val X_H2__H1_H1 = model.newVariable("X_H2__H1_H1").integer().lower(0) //X866
    val X_H2__H1_H2 = model.newVariable("X_H2__H1_H2").integer().lower(0) //X868
    val X_H2__H2_H2 = model.newVariable("X_H2__H2_H2").integer().lower(0) //X888

    model.newVariable("Y_W__W_W").binary() //Y444
    model.newVariable("Y_W__W_H1").binary() //Y446
    model.newVariable("Y_W__W_H2").binary() //Y448
    model.newVariable("Y_W__H1_H1").binary() //Y466
    model.newVariable("Y_W__H1_H2").binary() //Y468
    model.newVariable("Y_W__H2_H2").binary() //Y488
    model.newVariable("Y_H1__W_W").binary() //Y644
    model.newVariable("Y_H1__W_H1").binary() //Y646
    model.newVariable("Y_H1__W_H2").binary() //Y648
    model.newVariable("Y_H1__H1_H1").binary() //Y666
    model.newVariable("Y_H1__H1_H2").binary() //Y668
    model.newVariable("Y_H1__H2_H2").binary() //Y688
    model.newVariable("Y_H2__W_W").binary() //Y844
    model.newVariable("Y_H2__W_H1").binary() //Y846
    model.newVariable("Y_H2__W_H2").binary() //Y848
    model.newVariable("Y_H2__H1_H1").binary() //Y866
    model.newVariable("Y_H2__H1_H2").binary() //Y868
    model.newVariable("Y_H2__H2_H2").binary() //Y888

    // Constraints
    model.addExpression("A__W_W").upper(cell11) //A44
        .set(X_W__W_W, 2) //X444
        .set(X_W__W_H1, 1) //X446
        .set(X_W__W_H2, 1) //X448
    model.addExpression("A__W_H1").upper(cell12) //A46
        .set(X_W__W_H1, 1) //X446
        .set(X_W__H1_H1, 2) //X466
        .set(X_W__H1_H2, 1) //X468
        .set(X_H1__W_W, 2) //X644
        .set(X_H1__W_H1, 1) //X646
        .set(X_H1__W_H2, 1) //X648
    model.addExpression("A__W_H2").upper(cell13) //A48
        .set(X_W__W_H2, 1) //X448
        .set(X_W__H1_H2, 1) //X468
        .set(X_W__H2_H2, 2) //X488
        .set(X_H2__W_W, 2) //X844
        .set(X_H2__W_H1, 1) //X846
        .set(X_H2__W_H2, 1) //X848
    model.addExpression("A__H1_H1").upper(cell22) //A66
        .set(X_H1__W_H1, 1) //X646
        .set(X_H1__H1_H1, 2) //X666
        .set(X_H1__H1_H2, 1) //X668
    model.addExpression("A__H1_H2").upper(cell23) //A68
        .set(X_H1__W_H2, 1) //X648
        .set(X_H1__H1_H2, 1) //X668
        .set(X_H1__H2_H2, 2) //X688
        .set(X_H2__W_H1, 1) //X846
        .set(X_H2__H1_H1, 2) //X866
        .set(X_H2__H1_H2, 1) //X868
    model.addExpression("A__H2_H2").upper(cell33) //A88
        .set(X_H2__W_H2, 1) //X848
        .set(X_H2__H1_H2, 1) //X868
        .set(X_H2__H2_H2, 2) //X888

    return model.minimise().value;
}

// TODO: Use inputs dynamically.
private fun performOptimization2(): Double {
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

    return result.value
}