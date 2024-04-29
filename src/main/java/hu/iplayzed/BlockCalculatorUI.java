package hu.iplayzed;

import javax.swing.*;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class BlockCalculatorUI extends JFrame {

    private JTextField blockWidthField;
    private JTextField firstLengthField;
    private JTextField secondLengthField;
    private JButton generateTableButton;
    private JButton calculateOptimumButton;
    private JTextArea resultArea;

    public BlockCalculatorUI() {
        // Initialize components
        blockWidthField = new JTextField(10);
        firstLengthField = new JTextField(10);
        secondLengthField = new JTextField(10);
        generateTableButton = new JButton("GENERATE TABLE");
        calculateOptimumButton = new JButton("CALCULATE OPTIMUM");
        resultArea = new JTextArea("Optimal days will appear here", 5, 20);

        // Layout setup
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.add(new JLabel("Enter block width in cm"));
        this.add(blockWidthField);
        this.add(new JLabel("Enter first possible length in cm"));
        this.add(firstLengthField);
        this.add(new JLabel("Enter second possible length in cm"));
        this.add(secondLengthField);
        this.add(generateTableButton);
        this.add(calculateOptimumButton);
        this.add(new JScrollPane(resultArea));

        // Action Listeners for buttons
        setupActionListeners();

        // Finalize frame
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    private void setupActionListeners() {
        generateTableButton.addActionListener(e -> generateTable());
        calculateOptimumButton.addActionListener(e -> calculateOptimum());
    }

    private void generateTable() {
        // Logic to generate table
    }

    @SuppressWarnings("DuplicatedCode")
    private void calculateOptimum() {
        Loader.loadNativeLibraries();
        MPSolver solver = new MPSolver(
                "StoneCuttingOptimization",
                MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);

        MPVariable X_W__W_W = solver.makeIntVar(0, Integer.MAX_VALUE, "X_W__W_W"); //X444
        MPVariable X_W__W_H1 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_W__W_H1"); //X446
        MPVariable X_W__W_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_W__W_H2"); //X448
        MPVariable X_W__H1_H1 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_W__H1_H1"); //X466
        MPVariable X_W__H1_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_W__H1_H2"); //X468
        MPVariable X_W__H2_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_W__H2_H2"); //X488
        MPVariable X_H1__W_W = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H1__W_W"); //X644
        MPVariable X_H1__W_H1 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H1__W_H1"); //X646
        MPVariable X_H1__W_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H1__W_H2"); //X648
        MPVariable X_H1__H1_H1 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H1__H1_H1"); //X666
        MPVariable X_H1__H1_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H1__H1_H2"); //X668
        MPVariable X_H1__H2_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H1__H2_H2"); //X688
        MPVariable X_H2__W_W = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H2__W_W"); //X844
        MPVariable X_H2__W_H1 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H2__W_H1"); //X846
        MPVariable X_H2__W_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H2__W_H2"); //X848
        MPVariable X_H2__H1_H1 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H2__H1_H1"); //X866
        MPVariable X_H2__H1_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H2__H1_H2"); //X868
        MPVariable X_H2__H2_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H2__H2_H2"); //X888

        MPVariable Y_W__W_W = solver.makeBoolVar("Y_W__W_W"); //Y444
        MPVariable Y_W__W_H1 = solver.makeBoolVar("Y_W__W_H1"); //Y446
        MPVariable Y_W__W_H2 = solver.makeBoolVar("Y_W__W_H2"); //Y448
        MPVariable Y_W__H1_H1 = solver.makeBoolVar("Y_W__H1_H1"); //Y466
        MPVariable Y_W__H1_H2 = solver.makeBoolVar("Y_W__H1_H2"); //Y468
        MPVariable Y_W__H2_H2 = solver.makeBoolVar("Y_W__H2_H2"); //Y488
        MPVariable Y_H1__W_W = solver.makeBoolVar("Y_H1__W_W"); //Y644
        MPVariable Y_H1__W_H1 = solver.makeBoolVar("Y_H1__W_H1"); //Y646
        MPVariable Y_H1__W_H2 = solver.makeBoolVar("Y_H1__W_H2"); //Y648
        MPVariable Y_H1__H1_H1 = solver.makeBoolVar("Y_H1__H1_H1"); //Y666
        MPVariable Y_H1__H1_H2 = solver.makeBoolVar("Y_H1__H1_H2"); //Y668
        MPVariable Y_H1__H2_H2 = solver.makeBoolVar("Y_H1__H2_H2"); //Y688
        MPVariable Y_H2__W_W = solver.makeBoolVar("Y_H2__W_W"); //Y844
        MPVariable Y_H2__W_H1 = solver.makeBoolVar("Y_H2__W_H1"); //Y846
        MPVariable Y_H2__W_H2 = solver.makeBoolVar("Y_H2__W_H2"); //Y848
        MPVariable Y_H2__H1_H1 = solver.makeBoolVar("Y_H2__H1_H1"); //Y866
        MPVariable Y_H2__H1_H2 = solver.makeBoolVar("Y_H2__H1_H2"); //Y868
        MPVariable Y_H2__H2_H2 = solver.makeBoolVar("Y_H2__H2_H2"); //Y888

        System.out.println("Number of variables = " + solver.numVariables());

        MPConstraint A__W_W = solver.makeConstraint(10, Integer.MAX_VALUE, "A__W_W"); //A44
        A__W_W.setCoefficient(X_W__W_W, 2); //X444
        A__W_W.setCoefficient(X_W__W_H1, 1); //X446
        A__W_W.setCoefficient(X_W__W_H2, 1); //X448

        MPConstraint A__W_H1 = solver.makeConstraint(10, Integer.MAX_VALUE, "A__W_H1"); //A46
        A__W_H1.setCoefficient(X_W__W_H1, 1); //X446
        A__W_H1.setCoefficient(X_W__H1_H1, 2); //X466
        A__W_H1.setCoefficient(X_W__H1_H2, 1); //X468
        A__W_H1.setCoefficient(X_H1__W_W, 2); //X644
        A__W_H1.setCoefficient(X_H1__W_H1, 1); //X646
        A__W_H1.setCoefficient(X_H1__W_H2, 1); //X648

        MPConstraint A__W_H2 = solver.makeConstraint(0, Integer.MAX_VALUE, "A__W_H2"); //A48
        A__W_H2.setCoefficient(X_W__W_H2, 1); //X448
        A__W_H2.setCoefficient(X_W__H1_H2, 1); //X468
        A__W_H2.setCoefficient(X_W__H2_H2, 2); //X488
        A__W_H2.setCoefficient(X_H2__W_W, 2); //X844
        A__W_H2.setCoefficient(X_H2__W_H1, 1); //X846
        A__W_H2.setCoefficient(X_H2__W_H2, 1); //X848

        MPConstraint A__H1_H1 = solver.makeConstraint(5, Integer.MAX_VALUE, "A__H1_H1"); //A66
        A__H1_H1.setCoefficient(X_H1__W_H1, 1); //X646
        A__H1_H1.setCoefficient(X_H1__H1_H1, 2); //X666
        A__H1_H1.setCoefficient(X_H1__H1_H2, 1); //X668

        MPConstraint A__H1_H2 = solver.makeConstraint(10, Integer.MAX_VALUE, "A__H1_H2"); //A68
        A__H1_H2.setCoefficient(X_H1__W_H2, 1); //X648
        A__H1_H2.setCoefficient(X_H1__H1_H2, 1); //X668
        A__H1_H2.setCoefficient(X_H1__H2_H2, 2); //X688
        A__H1_H2.setCoefficient(X_H2__W_H1, 1); //X846
        A__H1_H2.setCoefficient(X_H2__H1_H1, 2); //X866
        A__H1_H2.setCoefficient(X_H2__H1_H2, 1); //X868

        MPConstraint A__H2_H2 = solver.makeConstraint(20, Integer.MAX_VALUE, "A__H2_H2"); //A88
        A__H2_H2.setCoefficient(X_H2__W_H2, 1); //X848
        A__H2_H2.setCoefficient(X_H2__H1_H2, 1); //X868
        A__H2_H2.setCoefficient(X_H2__H2_H2, 2); //X888

        MPConstraint link1 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link1.setCoefficient(X_W__W_W, 1);
        link1.setCoefficient(Y_W__W_W, -100);

        MPConstraint link2 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link2.setCoefficient(X_W__W_H1, 1);
        link2.setCoefficient(Y_W__W_H1, -100);

        MPConstraint link3 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link3.setCoefficient(X_W__W_H2, 1);
        link3.setCoefficient(Y_W__W_H2, -100);

        MPConstraint link4 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link4.setCoefficient(X_W__H1_H1, 1);
        link4.setCoefficient(Y_W__H1_H1, -100);

        MPConstraint link5 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link5.setCoefficient(X_W__H1_H2, 1);
        link5.setCoefficient(Y_W__H1_H2, -100);

        MPConstraint link6 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link6.setCoefficient(X_W__H2_H2, 1);
        link6.setCoefficient(Y_W__H2_H2, -100);

        MPConstraint link7 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link7.setCoefficient(X_H1__W_W, 1);
        link7.setCoefficient(Y_H1__W_W, -100);

        MPConstraint link8 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link8.setCoefficient(X_H1__W_H1, 1);
        link8.setCoefficient(Y_H1__W_H1, -100);

        MPConstraint link9 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link9.setCoefficient(X_H1__W_H2, 1);
        link9.setCoefficient(Y_H1__W_H2, -100);

        MPConstraint link10 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link10.setCoefficient(X_H1__H1_H1, 1);
        link10.setCoefficient(Y_H1__H1_H1, -100);

        MPConstraint link11 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link11.setCoefficient(X_H1__H1_H2, 1);
        link11.setCoefficient(Y_H1__H1_H2, -100);

        MPConstraint link12 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link12.setCoefficient(X_H1__H2_H2, 1);
        link12.setCoefficient(Y_H1__H2_H2, -100);

        MPConstraint link13 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link13.setCoefficient(X_H2__W_W, 1);
        link13.setCoefficient(Y_H2__W_W, -100);

        MPConstraint link14 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link14.setCoefficient(X_H2__W_H1, 1);
        link14.setCoefficient(Y_H2__W_H1, -100);

        MPConstraint link15 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link15.setCoefficient(X_H2__W_H2, 1);
        link15.setCoefficient(Y_H2__W_H2, -100);

        MPConstraint link16 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link16.setCoefficient(X_H2__H1_H1, 1);
        link16.setCoefficient(Y_H2__H1_H1, -100);

        MPConstraint link17 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link17.setCoefficient(X_H2__H1_H2, 1);
        link17.setCoefficient(Y_H2__H1_H2, -100);

        MPConstraint link18 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link18.setCoefficient(X_H2__H2_H2, 1);
        link18.setCoefficient(Y_H2__H2_H2, -100);

        System.out.println("Number of constraints = " + solver.numConstraints());

        MPObjective objective = solver.objective();

        objective.setCoefficient(X_W__W_W, 1);
        objective.setCoefficient(X_W__W_H1, 1);
        objective.setCoefficient(X_W__W_H2, 1);
        objective.setCoefficient(X_W__H1_H1, 1);
        objective.setCoefficient(X_W__H1_H2, 1);
        objective.setCoefficient(X_W__H2_H2, 1);
        objective.setCoefficient(X_H1__W_W, 1);
        objective.setCoefficient(X_H1__W_H1, 1);
        objective.setCoefficient(X_H1__W_H2, 1);
        objective.setCoefficient(X_H1__H1_H1, 1);
        objective.setCoefficient(X_H1__H1_H2, 1);
        objective.setCoefficient(X_H1__H1_H2, 1);
        objective.setCoefficient(X_H2__W_W, 1);
        objective.setCoefficient(X_H2__W_H1, 1);
        objective.setCoefficient(X_H2__W_H2, 1);
        objective.setCoefficient(X_H2__H1_H1, 1);
        objective.setCoefficient(X_H2__H1_H2, 1);
        objective.setCoefficient(X_H2__H2_H2, 1);

        objective.setCoefficient(Y_W__W_W, 1);
        objective.setCoefficient(Y_W__W_H1, 1);
        objective.setCoefficient(Y_W__W_H2, 1);
        objective.setCoefficient(Y_W__H1_H1, 1);
        objective.setCoefficient(Y_W__H1_H2, 1);
        objective.setCoefficient(Y_W__H2_H2, 1);
        objective.setCoefficient(Y_H1__W_W, 1);
        objective.setCoefficient(Y_H1__W_H1, 1);
        objective.setCoefficient(Y_H1__W_H2, 1);
        objective.setCoefficient(Y_H1__H1_H1, 1);
        objective.setCoefficient(Y_H1__H1_H2, 1);
        objective.setCoefficient(Y_H1__H1_H2, 1);
        objective.setCoefficient(Y_H2__W_W, 1);
        objective.setCoefficient(Y_H2__W_H1, 1);
        objective.setCoefficient(Y_H2__W_H2, 1);
        objective.setCoefficient(Y_H2__H1_H1, 1);
        objective.setCoefficient(Y_H2__H1_H2, 1);
        objective.setCoefficient(Y_H2__H2_H2, 1);

        // Objective direction
        objective.setMinimization();

        // Solve the model
        MPSolver.ResultStatus resultStatus = solver.solve();

        // Check that the problem has an optimal solution
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("Solution found!");
            System.out.println();
            // Output solution values
            System.out.println("X_W__W_W/X444: " + X_W__W_W.solutionValue());
            System.out.println("X_W__W_H1/X446: " + X_W__W_H1.solutionValue());
            System.out.println("X_W__W_H2/X448: " + X_W__W_H2.solutionValue());
            System.out.println("X_W__H1_H1/X466: " + X_W__H1_H1.solutionValue());
            System.out.println("X_W__H1_H2/X468: " + X_W__H1_H2.solutionValue());
            System.out.println("X_W__H2_H2/X488: " + X_W__H2_H2.solutionValue());
            System.out.println("X_H1__W_W/X644: " + X_H1__W_W.solutionValue());
            System.out.println("X_H1__W_H1/X646: " + X_H1__W_H1.solutionValue());
            System.out.println("X_H1__W_H2/X648: " + X_H1__W_H2.solutionValue());
            System.out.println("X_H1__H1_H1/X666: " + X_H1__H1_H1.solutionValue());
            System.out.println("X_H1__H1_H2/X668: " + X_H1__H1_H2.solutionValue());
            System.out.println("X_H1__H2_H2/X688: " + X_H1__H2_H2.solutionValue());
            System.out.println("X_H2__W_W/X844: " + X_H2__W_W.solutionValue());
            System.out.println("X_H2__W_H1/X846: " + X_H2__W_H1.solutionValue());
            System.out.println("X_H2__W_H2/X848: " + X_H2__W_H2.solutionValue());
            System.out.println("X_H2__H1_H1/X866: " + X_H2__H1_H1.solutionValue());
            System.out.println("X_H2__H1_H2/X868: " + X_H2__H1_H2.solutionValue());
            System.out.println("X_H2__H2_H2/X888: " + X_H2__H2_H2.solutionValue());
            System.out.println();

            System.out.println("Y_W__W_W/Y444: " + Y_W__W_W.solutionValue());
            System.out.println("Y_W__W_H1/Y446: " + Y_W__W_H1.solutionValue());
            System.out.println("Y_W__W_H2/Y448: " + Y_W__W_H2.solutionValue());
            System.out.println("Y_W__H1_H1/Y466: " + Y_W__H1_H1.solutionValue());
            System.out.println("Y_W__H1_H2/Y468: " + Y_W__H1_H2.solutionValue());
            System.out.println("Y_W__H2_H2/Y488: " + Y_W__H2_H2.solutionValue());
            System.out.println("Y_H1__W_W/Y644: " + Y_H1__W_W.solutionValue());
            System.out.println("Y_H1__W_H1/Y646: " + Y_H1__W_H1.solutionValue());
            System.out.println("Y_H1__W_H2/Y648: " + Y_H1__W_H2.solutionValue());
            System.out.println("Y_H1__H1_H1/Y666: " + Y_H1__H1_H1.solutionValue());
            System.out.println("Y_H1__H1_H2/Y668: " + Y_H1__H1_H2.solutionValue());
            System.out.println("Y_H1__H2_H2/Y688: " + Y_H1__H2_H2.solutionValue());
            System.out.println("Y_H2__W_W/Y844: " + Y_H2__W_W.solutionValue());
            System.out.println("Y_H2__W_H1/Y846: " + Y_H2__W_H1.solutionValue());
            System.out.println("Y_H2__W_H2/Y848: " + Y_H2__W_H2.solutionValue());
            System.out.println("Y_H2__H1_H1/Y866: " + Y_H2__H1_H1.solutionValue());
            System.out.println("Y_H2__H1_H2/Y868: " + Y_H2__H1_H2.solutionValue());
            System.out.println("Y_H2__H2_H2/Y888: " + Y_H2__H2_H2.solutionValue());
            System.out.println();

        } else {
            System.out.println("No solution found, result status is: " + resultStatus);
        }

        System.out.println("Solution:");
        System.out.println("Objective value = " + objective.value());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BlockCalculatorUI::new);
    }
}

