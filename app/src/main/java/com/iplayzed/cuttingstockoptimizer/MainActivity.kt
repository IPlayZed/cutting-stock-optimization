package com.iplayzed.cuttingstockoptimizer

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.ojalgo.optimisation.Expression
import org.ojalgo.optimisation.ExpressionsBasedModel
import org.ojalgo.optimisation.Variable


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wizard_step_one)

        val stoneBlockWidthTextView: TextView = findViewById(R.id.stoneBlockWidth)
        val blockLength1TextView: TextView = findViewById(R.id.blockLength1)
        val blockLength2TextView: TextView = findViewById(R.id.blockLength2)
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

    val X_W__W_W = model.addVariable("X_W__W_W").weight(1).lower(0).integer() //X444
    val X_W__W_H1 = model.addVariable("X_W__W_H1").weight(1).lower(0).integer()  //X446
    val X_W__W_H2 = model.addVariable("X_W__W_H2").weight(1).lower(0).integer() //X448
    val X_W__H1_H1 = model.addVariable("X_W__H1_H1").weight(1).lower(0).integer() //X466
    val X_W__H1_H2 = model.addVariable("X_W__H1_H2").weight(1).lower(0).integer() //X468
    val X_W__H2_H2 = model.addVariable("X_W__H2_H2").weight(1).lower(0).integer() //X488
    val X_H1__W_W = model.addVariable("X_H1__W_W").weight(1).lower(0).integer() //X644
    val X_H1__W_H1 = model.addVariable("X_H1__W_H1").weight(1).lower(0).integer() //X646
    val X_H1__W_H2 = model.addVariable("X_H1__W_H2").weight(1).lower(0).integer() //X648
    val X_H1__H1_H1 = model.addVariable("X_H1__H1_H1").weight(1).lower(0).integer() //X666
    val X_H1__H1_H2 = model.addVariable("X_H1__H1_H2").weight(1).lower(0).integer() //X668
    val X_H1__H2_H2 = model.addVariable("X_H1__H2_H2").weight(1).lower(0).integer() //X688
    val X_H2__W_W = model.addVariable("X_H2__W_W").weight(1).lower(0).integer() //X844
    val X_H2__W_H1 = model.addVariable("X_H2__W_H1").weight(1).lower(0).integer() //X846
    val X_H2__W_H2 = model.addVariable("X_H2__W_H2").weight(1).lower(0).integer() //X848
    val X_H2__H1_H1 = model.addVariable("X_H2__H1_H1").weight(1).lower(0).integer() //X866
    val X_H2__H1_H2 = model.addVariable("X_H2__H1_H2").weight(1).lower(0).integer() //X868
    val X_H2__H2_H2 = model.addVariable("X_H2__H2_H2").weight(1).lower(0).integer() //X888

    val Y_W__W_W = model.addVariable("Y_W__W_W").binary().weight(1) //Y444
    val Y_W__W_H1 = model.addVariable("Y_W__W_H1").binary().weight(1) //Y446
    val Y_W__W_H2 = model.addVariable("Y_W__W_H2").binary().weight(1) //Y448
    val Y_W__H1_H1 = model.addVariable("Y_W__H1_H1").binary().weight(1) //Y466
    val Y_W__H1_H2 = model.addVariable("Y_W__H1_H2").binary().weight(1) //Y468
    val Y_W__H2_H2 = model.addVariable("Y_W__H2_H2").binary().weight(1) //Y488
    val Y_H1__W_W = model.addVariable("Y_H1__W_W").binary().weight(1) //Y644
    val Y_H1__W_H1 = model.addVariable("Y_H1__W_H1").binary().weight(1) //Y646
    val Y_H1__W_H2 = model.addVariable("Y_H1__W_H2").binary().weight(1) //Y648
    val Y_H1__H1_H1 = model.addVariable("Y_H1__H1_H1").binary().weight(1) //Y666
    val Y_H1__H1_H2 = model.addVariable("Y_H1__H1_H2").binary().weight(1) //Y668
    val Y_H1__H2_H2 = model.addVariable("Y_H1__H2_H2").binary().weight(1) //Y688
    val Y_H2__W_W = model.addVariable("Y_H2__W_W").binary().weight(1) //Y844
    val Y_H2__W_H1 = model.addVariable("Y_H2__W_H1").binary().weight(1) //Y846
    val Y_H2__W_H2 = model.addVariable("Y_H2__W_H2").binary().weight(1) //Y848
    val Y_H2__H1_H1 = model.addVariable("Y_H2__H1_H1").binary().weight(1) //Y866
    val Y_H2__H1_H2 = model.addVariable("Y_H2__H1_H2").binary().weight(1) //Y868
    val Y_H2__H2_H2 = model.addVariable("Y_H2__H2_H2").binary().weight(1) //Y888

    // Constraints
    model.addExpression("A__W_W") //A44
        .lower(cell11)
        .set(X_W__W_W, 2) //X444
        .set(X_W__W_H1, 1) //X446
        .set(X_W__W_H2, 1) //X448
        .weight(1)
    model.addExpression("A__W_H1") //A46
        .lower(cell12)
        .set(X_W__W_H1, 1) //X446
        .set(X_W__H1_H1, 2) //X466
        .set(X_W__H1_H2, 1) //X468
        .set(X_H1__W_W, 2) //X644
        .set(X_H1__W_H1, 1) //X646
        .set(X_H1__W_H2, 1) //X648
        .weight(1)
    model.addExpression("A__W_H2") //A48
        .lower(cell13)
        .set(X_W__W_H2, 1) //X448
        .set(X_W__H1_H2, 1) //X468
        .set(X_W__H2_H2, 2) //X488
        .set(X_H2__W_W, 2) //X844
        .set(X_H2__W_H1, 1) //X846
        .set(X_H2__W_H2, 1) //X848
        .weight(1)
    model.addExpression("A__H1_H1") //A66
        .lower(cell22)
        .set(X_H1__W_H1, 1) //X646
        .set(X_H1__H1_H1, 2) //X666
        .set(X_H1__H1_H2, 1) //X668
        .weight(1)
    model.addExpression("A__H1_H2") //A68
        .lower(cell23)
        .set(X_H1__W_H2, 1) //X648
        .set(X_H1__H1_H2, 1) //X668
        .set(X_H1__H2_H2, 2) //X688
        .set(X_H2__W_H1, 1) //X846
        .set(X_H2__H1_H1, 2) //X866
        .set(X_H2__H1_H2, 1) //X868
        .weight(1)
    model.addExpression("A__H2_H2") //A88
        .lower(cell33)
        .set(X_H2__W_H2, 1) //X848
        .set(X_H2__H1_H2, 1) //X868
        .set(X_H2__H2_H2, 2) //X888
        .weight(1)

    model.addExpression().weight(1).set(X_W__W_W, 1).set(Y_W__W_W, -100).upper(0)
    model.addExpression().weight(1).set(X_W__W_H1, 1).set(Y_W__W_H1, -100).upper(0)
    model.addExpression().weight(1).set(X_W__W_H2, 1).set(Y_W__W_H2, -100).upper(0)
    model.addExpression().weight(1).set(X_W__H1_H1, 1).set(Y_W__H1_H1, -100).upper(0)
    model.addExpression().weight(1).set(X_W__H1_H2, 1).set(Y_W__H1_H2, -100).upper(0)
    model.addExpression().weight(1).set(X_W__H2_H2, 1).set(Y_W__H2_H2, -100).upper(0)
    model.addExpression().weight(1).set(X_H1__W_W, 1).set(Y_H1__W_W, -100).upper(0)
    model.addExpression().weight(1).set(X_H1__W_H1, 1).set(Y_H1__W_H1, -100).upper(0)
    model.addExpression().weight(1).set(X_H1__W_H2, 1).set(Y_H1__W_H2, -100).upper(0)
    model.addExpression().weight(1).set(X_H1__H1_H1, 1).set(Y_H1__H1_H1, -100).upper(0)
    model.addExpression().weight(1).set(X_H1__H1_H2, 1).set(Y_H1__H1_H2, -100).upper(0)
    model.addExpression().weight(1).set(X_H1__H2_H2, 1).set(Y_H1__H2_H2, -100).upper(0)
    model.addExpression().weight(1).set(X_H2__W_W, 1).set(Y_H2__W_W, -100).upper(0)
    model.addExpression().weight(1).set(X_H2__W_H1, 1).set(Y_H2__W_H1, -100).upper(0)
    model.addExpression().weight(1).set(X_H2__W_H2, 1).set(Y_H2__W_H2, -100).upper(0)
    model.addExpression().weight(1).set(X_H2__H1_H1, 1).set(Y_H2__H1_H1, -100).upper(0)
    model.addExpression().weight(1).set(X_H2__H1_H2, 1).set(Y_H2__H1_H2, -100).upper(0)
    model.addExpression().weight(1).set(X_H2__H2_H2, 1).set(Y_H2__H2_H2, -100).upper(0)

    val result = model.maximise()
    return result.value
}
