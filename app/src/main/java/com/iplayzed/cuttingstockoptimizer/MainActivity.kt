package com.iplayzed.cuttingstockoptimizer

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.ortools.Loader
import com.google.ortools.linearsolver.MPSolver
import com.google.ortools.linearsolver.MPSolver.ResultStatus


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

    val TAG = "performOptimization"
    // These variables represent how much of a given combination is needed.
    // W represents the width of the stone block.
    // H1 represents the first possible height, H2 the second possible height.
    // For instance X_W__W_W represents how much width x width and width x width sized
    // blocks are created.

    // Comments are for concrete 40w,60/80h.

    Loader.loadNativeLibraries()
    val solver = MPSolver(
        "StoneCuttingOptimization",
        MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING
    )

    val X_W__W_W = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_W__W_W"
    ) //X444

    val X_W__W_H1 = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_W__W_H1"
    ) //X446

    val X_W__W_H2 = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_W__W_H2"
    ) //X448

    val X_W__H1_H1 = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_W__H1_H1"
    ) //X466

    val X_W__H1_H2 = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_W__H1_H2"
    ) //X468

    val X_W__H2_H2 = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_W__H2_H2"
    ) //X488

    val X_H1__W_W = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_H1__W_W"
    ) //X644

    val X_H1__W_H1 = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_H1__W_H1"
    ) //X646

    val X_H1__W_H2 = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_H1__W_H2"
    ) //X648

    val X_H1__H1_H1 = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_H1__H1_H1"
    ) //X666

    val X_H1__H1_H2 = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_H1__H1_H2"
    ) //X668

    val X_H1__H2_H2 = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_H1__H2_H2"
    ) //X688

    val X_H2__W_W = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_H2__W_W"
    ) //X844

    val X_H2__W_H1 = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_H2__W_H1"
    ) //X846

    val X_H2__W_H2 = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_H2__W_H2"
    ) //X848

    val X_H2__H1_H1 = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_H2__H1_H1"
    ) //X866

    val X_H2__H1_H2 = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_H2__H1_H2"
    ) //X868

    val X_H2__H2_H2 = solver.makeIntVar(
        0.0,
        Int.MAX_VALUE.toDouble(), "X_H2__H2_H2"
    ) //X888


    val Y_W__W_W = solver.makeBoolVar("Y_W__W_W") //Y444

    val Y_W__W_H1 = solver.makeBoolVar("Y_W__W_H1") //Y446

    val Y_W__W_H2 = solver.makeBoolVar("Y_W__W_H2") //Y448

    val Y_W__H1_H1 = solver.makeBoolVar("Y_W__H1_H1") //Y466

    val Y_W__H1_H2 = solver.makeBoolVar("Y_W__H1_H2") //Y468

    val Y_W__H2_H2 = solver.makeBoolVar("Y_W__H2_H2") //Y488

    val Y_H1__W_W = solver.makeBoolVar("Y_H1__W_W") //Y644

    val Y_H1__W_H1 = solver.makeBoolVar("Y_H1__W_H1") //Y646

    val Y_H1__W_H2 = solver.makeBoolVar("Y_H1__W_H2") //Y648

    val Y_H1__H1_H1 = solver.makeBoolVar("Y_H1__H1_H1") //Y666

    val Y_H1__H1_H2 = solver.makeBoolVar("Y_H1__H1_H2") //Y668

    val Y_H1__H2_H2 = solver.makeBoolVar("Y_H1__H2_H2") //Y688

    val Y_H2__W_W = solver.makeBoolVar("Y_H2__W_W") //Y844

    val Y_H2__W_H1 = solver.makeBoolVar("Y_H2__W_H1") //Y846

    val Y_H2__W_H2 = solver.makeBoolVar("Y_H2__W_H2") //Y848

    val Y_H2__H1_H1 = solver.makeBoolVar("Y_H2__H1_H1") //Y866

    val Y_H2__H1_H2 = solver.makeBoolVar("Y_H2__H1_H2") //Y868

    val Y_H2__H2_H2 = solver.makeBoolVar("Y_H2__H2_H2") //Y888


    println("Number of variables = " + solver.numVariables())

    val A__W_W = solver.makeConstraint(
        cell11.toDouble(),
        Int.MAX_VALUE.toDouble(), "A__W_W"
    ) //A44

    A__W_W.setCoefficient(X_W__W_W, 2.0) //X444

    A__W_W.setCoefficient(X_W__W_H1, 1.0) //X446

    A__W_W.setCoefficient(X_W__W_H2, 1.0) //X448


    val A__W_H1 = solver.makeConstraint(
        cell12.toDouble(),
        Int.MAX_VALUE.toDouble(), "A__W_H1"
    ) //A46

    A__W_H1.setCoefficient(X_W__W_H1, 1.0) //X446

    A__W_H1.setCoefficient(X_W__H1_H1, 2.0) //X466

    A__W_H1.setCoefficient(X_W__H1_H2, 1.0) //X468

    A__W_H1.setCoefficient(X_H1__W_W, 2.0) //X644

    A__W_H1.setCoefficient(X_H1__W_H1, 1.0) //X646

    A__W_H1.setCoefficient(X_H1__W_H2, 1.0) //X648


    val A__W_H2 = solver.makeConstraint(
        cell13.toDouble(),
        Int.MAX_VALUE.toDouble(), "A__W_H2"
    ) //A48

    A__W_H2.setCoefficient(X_W__W_H2, 1.0) //X448

    A__W_H2.setCoefficient(X_W__H1_H2, 1.0) //X468

    A__W_H2.setCoefficient(X_W__H2_H2, 2.0) //X488

    A__W_H2.setCoefficient(X_H2__W_W, 2.0) //X844

    A__W_H2.setCoefficient(X_H2__W_H1, 1.0) //X846

    A__W_H2.setCoefficient(X_H2__W_H2, 1.0) //X848


    val A__H1_H1 = solver.makeConstraint(
        cell22.toDouble(),
        Int.MAX_VALUE.toDouble(), "A__H1_H1"
    ) //A66

    A__H1_H1.setCoefficient(X_H1__W_H1, 1.0) //X646

    A__H1_H1.setCoefficient(X_H1__H1_H1, 2.0) //X666

    A__H1_H1.setCoefficient(X_H1__H1_H2, 1.0) //X668


    val A__H1_H2 = solver.makeConstraint(
        cell23.toDouble(),
        Int.MAX_VALUE.toDouble(), "A__H1_H2"
    ) //A68

    A__H1_H2.setCoefficient(X_H1__W_H2, 1.0) //X648

    A__H1_H2.setCoefficient(X_H1__H1_H2, 1.0) //X668

    A__H1_H2.setCoefficient(X_H1__H2_H2, 2.0) //X688

    A__H1_H2.setCoefficient(X_H2__W_H1, 1.0) //X846

    A__H1_H2.setCoefficient(X_H2__H1_H1, 2.0) //X866

    A__H1_H2.setCoefficient(X_H2__H1_H2, 1.0) //X868


    val A__H2_H2 = solver.makeConstraint(
        cell33.toDouble(),
        Int.MAX_VALUE.toDouble(), "A__H2_H2"
    ) //A88

    A__H2_H2.setCoefficient(X_H2__W_H2, 1.0) //X848

    A__H2_H2.setCoefficient(X_H2__H1_H2, 1.0) //X868

    A__H2_H2.setCoefficient(X_H2__H2_H2, 2.0) //X888


    val link1 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link1.setCoefficient(X_W__W_W, 1.0)
    link1.setCoefficient(Y_W__W_W, -100.0)

    val link2 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link2.setCoefficient(X_W__W_H1, 1.0)
    link2.setCoefficient(Y_W__W_H1, -100.0)

    val link3 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link3.setCoefficient(X_W__W_H2, 1.0)
    link3.setCoefficient(Y_W__W_H2, -100.0)

    val link4 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link4.setCoefficient(X_W__H1_H1, 1.0)
    link4.setCoefficient(Y_W__H1_H1, -100.0)

    val link5 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link5.setCoefficient(X_W__H1_H2, 1.0)
    link5.setCoefficient(Y_W__H1_H2, -100.0)

    val link6 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link6.setCoefficient(X_W__H2_H2, 1.0)
    link6.setCoefficient(Y_W__H2_H2, -100.0)

    val link7 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link7.setCoefficient(X_H1__W_W, 1.0)
    link7.setCoefficient(Y_H1__W_W, -100.0)

    val link8 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link8.setCoefficient(X_H1__W_H1, 1.0)
    link8.setCoefficient(Y_H1__W_H1, -100.0)

    val link9 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link9.setCoefficient(X_H1__W_H2, 1.0)
    link9.setCoefficient(Y_H1__W_H2, -100.0)

    val link10 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link10.setCoefficient(X_H1__H1_H1, 1.0)
    link10.setCoefficient(Y_H1__H1_H1, -100.0)

    val link11 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link11.setCoefficient(X_H1__H1_H2, 1.0)
    link11.setCoefficient(Y_H1__H1_H2, -100.0)

    val link12 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link12.setCoefficient(X_H1__H2_H2, 1.0)
    link12.setCoefficient(Y_H1__H2_H2, -100.0)

    val link13 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link13.setCoefficient(X_H2__W_W, 1.0)
    link13.setCoefficient(Y_H2__W_W, -100.0)

    val link14 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link14.setCoefficient(X_H2__W_H1, 1.0)
    link14.setCoefficient(Y_H2__W_H1, -100.0)

    val link15 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link15.setCoefficient(X_H2__W_H2, 1.0)
    link15.setCoefficient(Y_H2__W_H2, -100.0)

    val link16 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link16.setCoefficient(X_H2__H1_H1, 1.0)
    link16.setCoefficient(Y_H2__H1_H1, -100.0)

    val link17 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link17.setCoefficient(X_H2__H1_H2, 1.0)
    link17.setCoefficient(Y_H2__H1_H2, -100.0)

    val link18 = solver.makeConstraint(Int.MIN_VALUE.toDouble(), 0.0)
    link18.setCoefficient(X_H2__H2_H2, 1.0)
    link18.setCoefficient(Y_H2__H2_H2, -100.0)

    println("Number of constraints = " + solver.numConstraints())

    val objective = solver.objective()

    objective.setCoefficient(X_W__W_W, 1.0)
    objective.setCoefficient(X_W__W_H1, 1.0)
    objective.setCoefficient(X_W__W_H2, 1.0)
    objective.setCoefficient(X_W__H1_H1, 1.0)
    objective.setCoefficient(X_W__H1_H2, 1.0)
    objective.setCoefficient(X_W__H2_H2, 1.0)
    objective.setCoefficient(X_H1__W_W, 1.0)
    objective.setCoefficient(X_H1__W_H1, 1.0)
    objective.setCoefficient(X_H1__W_H2, 1.0)
    objective.setCoefficient(X_H1__H1_H1, 1.0)
    objective.setCoefficient(X_H1__H1_H2, 1.0)
    objective.setCoefficient(X_H1__H1_H2, 1.0)
    objective.setCoefficient(X_H2__W_W, 1.0)
    objective.setCoefficient(X_H2__W_H1, 1.0)
    objective.setCoefficient(X_H2__W_H2, 1.0)
    objective.setCoefficient(X_H2__H1_H1, 1.0)
    objective.setCoefficient(X_H2__H1_H2, 1.0)
    objective.setCoefficient(X_H2__H2_H2, 1.0)

    objective.setCoefficient(Y_W__W_W, 1.0)
    objective.setCoefficient(Y_W__W_H1, 1.0)
    objective.setCoefficient(Y_W__W_H2, 1.0)
    objective.setCoefficient(Y_W__H1_H1, 1.0)
    objective.setCoefficient(Y_W__H1_H2, 1.0)
    objective.setCoefficient(Y_W__H2_H2, 1.0)
    objective.setCoefficient(Y_H1__W_W, 1.0)
    objective.setCoefficient(Y_H1__W_H1, 1.0)
    objective.setCoefficient(Y_H1__W_H2, 1.0)
    objective.setCoefficient(Y_H1__H1_H1, 1.0)
    objective.setCoefficient(Y_H1__H1_H2, 1.0)
    objective.setCoefficient(Y_H1__H1_H2, 1.0)
    objective.setCoefficient(Y_H2__W_W, 1.0)
    objective.setCoefficient(Y_H2__W_H1, 1.0)
    objective.setCoefficient(Y_H2__W_H2, 1.0)
    objective.setCoefficient(Y_H2__H1_H1, 1.0)
    objective.setCoefficient(Y_H2__H1_H2, 1.0)
    objective.setCoefficient(Y_H2__H2_H2, 1.0)

    // Objective direction

    // Objective direction
    objective.setMinimization()

    // Solve the model

    // Solve the model
    val resultStatus = solver.solve()

    // Check that the problem has an optimal solution

    // Check that the problem has an optimal solution
    if (resultStatus == ResultStatus.OPTIMAL) {
        Log.d(TAG, "Solution found!")
        Log.d(TAG, "----- Configurations used -----")
        // Output solution values
        Log.d(TAG, "X_W__W_W/X444: " + X_W__W_W.solutionValue())
        Log.d(TAG, "X_W__W_H1/X446: " + X_W__W_H1.solutionValue())
        Log.d(TAG, "X_W__W_H2/X448: " + X_W__W_H2.solutionValue())
        Log.d(TAG, "X_W__H1_H1/X466: " + X_W__H1_H1.solutionValue())
        Log.d(TAG, "X_W__H1_H2/X468: " + X_W__H1_H2.solutionValue())
        Log.d(TAG, "X_W__H2_H2/X488: " + X_W__H2_H2.solutionValue())
        Log.d(TAG, "X_H1__W_W/X644: " + X_H1__W_W.solutionValue())
        Log.d(TAG, "X_H1__W_H1/X646: " + X_H1__W_H1.solutionValue())
        Log.d(TAG, "X_H1__W_H2/X648: " + X_H1__W_H2.solutionValue())
        Log.d(TAG, "X_H1__H1_H1/X666: " + X_H1__H1_H1.solutionValue())
        Log.d(TAG, "X_H1__H1_H2/X668: " + X_H1__H1_H2.solutionValue())
        Log.d(TAG, "X_H1__H2_H2/X688: " + X_H1__H2_H2.solutionValue())
        Log.d(TAG, "X_H2__W_W/X844: " + X_H2__W_W.solutionValue())
        Log.d(TAG, "X_H2__W_H1/X846: " + X_H2__W_H1.solutionValue())
        Log.d(TAG, "X_H2__W_H2/X848: " + X_H2__W_H2.solutionValue())
        Log.d(TAG, "X_H2__H1_H1/X866: " + X_H2__H1_H1.solutionValue())
        Log.d(TAG, "X_H2__H1_H2/X868: " + X_H2__H1_H2.solutionValue())
        Log.d(TAG, "X_H2__H2_H2/X888: " + X_H2__H2_H2.solutionValue())

        Log.d(TAG, "----- Changes used -----")
        Log.d(TAG, "Y_W__W_W/Y444: " + Y_W__W_W.solutionValue())
        Log.d(TAG, "Y_W__W_H1/Y446: " + Y_W__W_H1.solutionValue())
        Log.d(TAG, "Y_W__W_H2/Y448: " + Y_W__W_H2.solutionValue())
        Log.d(TAG, "Y_W__H1_H1/Y466: " + Y_W__H1_H1.solutionValue())
        Log.d(TAG, "Y_W__H1_H2/Y468: " + Y_W__H1_H2.solutionValue())
        Log.d(TAG, "Y_W__H2_H2/Y488: " + Y_W__H2_H2.solutionValue())
        Log.d(TAG, "Y_H1__W_W/Y644: " + Y_H1__W_W.solutionValue())
        Log.d(TAG, "Y_H1__W_H1/Y646: " + Y_H1__W_H1.solutionValue())
        Log.d(TAG, "Y_H1__W_H2/Y648: " + Y_H1__W_H2.solutionValue())
        Log.d(TAG, "Y_H1__H1_H1/Y666: " + Y_H1__H1_H1.solutionValue())
        Log.d(TAG, "Y_H1__H1_H2/Y668: " + Y_H1__H1_H2.solutionValue())
        Log.d(TAG, "Y_H1__H2_H2/Y688: " + Y_H1__H2_H2.solutionValue())
        Log.d(TAG, "Y_H2__W_W/Y844: " + Y_H2__W_W.solutionValue())
        Log.d(TAG, "Y_H2__W_H1/Y846: " + Y_H2__W_H1.solutionValue())
        Log.d(TAG, "Y_H2__W_H2/Y848: " + Y_H2__W_H2.solutionValue())
        Log.d(TAG, "Y_H2__H1_H1/Y866: " + Y_H2__H1_H1.solutionValue())
        Log.d(TAG, "Y_H2__H1_H2/Y868: " + Y_H2__H1_H2.solutionValue())
        Log.d(TAG, "Y_H2__H2_H2/Y888: " + Y_H2__H2_H2.solutionValue())
    } else {
        Log.d(TAG, "No solution found, result status is: $resultStatus")
    }

    Log.d(TAG, "Solution:")
    Log.d(TAG, "Objective value = " + objective.value())

    return objective.value()
}
