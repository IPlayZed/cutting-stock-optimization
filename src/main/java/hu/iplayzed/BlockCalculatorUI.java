package hu.iplayzed;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class BlockCalculatorUI extends JFrame {

    private final JTextField firstLengthField;
    private final JTextField secondLengthField;
    private final JButton generateTableButton;
    private final JButton calculateOptimumButton;
    private final JTextArea resultArea;
    private final JTextField blockWidthField;
    private final DefaultTableModel tableModel;
    private final JTextField transitionTime;
    private final JTextField mValue;

    public BlockCalculatorUI() {
        // Initialize components
        super("Optimization Parameters");

        blockWidthField = new JTextField(10);
        firstLengthField = new JTextField(10);
        secondLengthField = new JTextField(10);
        transitionTime = new JTextField(10);
        mValue = new JTextField(10);

        generateTableButton = new JButton("GENERATE TABLE");

        String[] columnNames = {"Mirror", "Width", "Height1", "Height2"};
        final String NODATA = "        X";
        Object[][] data = {
                {NODATA, "", "", ""},
                {"", "", "", ""},
                {"", NODATA, "", ""},
                {"", NODATA, NODATA, ""},
        };
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make cells non-editable except the USER_INPUT fields
                return (row > 0 && column > 0) && !getValueAt(row, column).equals("X");
            }
        };
        JTable table = new JTable(tableModel);

        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(80);
        }

        // Disable auto resizing
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set the table's preferred size
        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        calculateOptimumButton = new JButton("CALCULATE OPTIMUM");

        resultArea = new JTextArea("", 40, 50);

        // Layout setup
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.add(new JLabel("Enter block width in cm"));
        this.add(blockWidthField);
        this.add(new JLabel("Enter first possible length in cm"));
        this.add(firstLengthField);
        this.add(new JLabel("Enter second possible length in cm"));
        this.add(secondLengthField);
        this.add(generateTableButton);
        this.add(scrollPane);
        this.add(new JLabel("Enter transition time in days"));
        this.add(transitionTime);
        this.add(new JLabel("Enter M value for constraint."));
        this.add(mValue);
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
        //noinspection unused
        calculateOptimumButton.addActionListener(event -> calculateOptimum(transitionTime.getText(),
                mValue.getText()));
        generateTableButton.addActionListener(this::generateTable);
    }

    private void generateTable(ActionEvent e) {
        tableModel.setValueAt(blockWidthField.getText() + " cm", 1, 0);
        tableModel.setValueAt(firstLengthField.getText() + " cm", 2, 0);
        tableModel.setValueAt(secondLengthField.getText() + " cm", 3, 0);

        tableModel.setValueAt(blockWidthField.getText() + " cm", 0, 1);
        tableModel.setValueAt(firstLengthField.getText() + " cm", 0, 2);
        tableModel.setValueAt(secondLengthField.getText() + " cm", 0, 3);
    }

    @SuppressWarnings("DuplicatedCode")
    private void calculateOptimum(String transitionTime, String mValue) {
        int cell11, cell12, cell13, cell22, cell23, cell33;
        try {
            cell11 = Integer.parseInt((String) tableModel.getValueAt(1,1));
            cell12 = Integer.parseInt((String) tableModel.getValueAt(1,2));
            cell13 = Integer.parseInt((String) tableModel.getValueAt(1,3));
            cell22 = Integer.parseInt((String) tableModel.getValueAt(2,2));
            cell23 = Integer.parseInt((String) tableModel.getValueAt(2,3));
            cell33 = Integer.parseInt((String) tableModel.getValueAt(3,3));
        }
        catch (NumberFormatException exception)
        {
            resultArea.setText("""

                    Make sure to press enter after writing the value into the table for it to \
                    register.
                    """);
            return;
        }

        int _transitionTime = Integer.parseInt(transitionTime);
        int _mValue = Integer.parseInt(mValue);

        if (cell11 < 0 || cell12 < 0 || cell13 < 0 || cell22 < 0 || cell23 < 0 || cell33 < 0) {
            resultArea.setText("\nBlock quantities can't be negative!");
            return;
        }
        if (_transitionTime <= 0) {
            resultArea.setText("\nTransition time must be greater than zero.\n");
            return;
        }
        if (_mValue < 100) {
            resultArea.setText("\nM should be at least 100 for acceptable operation.\n");
            return;
        }
        _mValue *= -1;

        Loader.loadNativeLibraries();
        MPSolver solver = new MPSolver(
                "StoneCuttingOptimization",
                MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);

        List<MPVariable> xVariables = new ArrayList<>();
        MPVariable X_W__W_W = solver.makeIntVar(0, Integer.MAX_VALUE, "X_W__W_W"); //X444
        xVariables.add(X_W__W_W);
        MPVariable X_W__W_H1 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_W__W_H1"); //X446
        xVariables.add(X_W__W_H1);
        MPVariable X_W__W_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_W__W_H2"); //X448
        xVariables.add(X_W__W_H2);
        MPVariable X_W__H1_H1 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_W__H1_H1"); //X466
        xVariables.add(X_W__H1_H1);
        MPVariable X_W__H1_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_W__H1_H2"); //X468
        xVariables.add(X_W__H1_H2);
        MPVariable X_W__H2_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_W__H2_H2"); //X488
        xVariables.add(X_W__H2_H2);
        MPVariable X_H1__W_W = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H1__W_W"); //X644
        xVariables.add(X_H1__W_W);
        MPVariable X_H1__W_H1 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H1__W_H1"); //X646
        xVariables.add(X_H1__W_H1);
        MPVariable X_H1__W_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H1__W_H2"); //X648
        xVariables.add(X_H1__W_H2);
        MPVariable X_H1__H1_H1 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H1__H1_H1"); //X666
        xVariables.add(X_H1__H1_H1);
        MPVariable X_H1__H1_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H1__H1_H2"); //X668
        xVariables.add(X_H1__H1_H2);
        MPVariable X_H1__H2_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H1__H2_H2"); //X688
        xVariables.add(X_H1__H2_H2);
        MPVariable X_H2__W_W = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H2__W_W"); //X844
        xVariables.add(X_H2__W_W);
        MPVariable X_H2__W_H1 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H2__W_H1"); //X846
        xVariables.add(X_H2__W_H1);
        MPVariable X_H2__W_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H2__W_H2"); //X848
        xVariables.add(X_H2__W_H2);
        MPVariable X_H2__H1_H1 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H2__H1_H1"); //X866
        xVariables.add(X_H2__H1_H1);
        MPVariable X_H2__H1_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H2__H1_H2"); //X868
        xVariables.add(X_H2__H1_H2);
        MPVariable X_H2__H2_H2 = solver.makeIntVar(0, Integer.MAX_VALUE, "X_H2__H2_H2"); //X888
        xVariables.add(X_H2__H2_H2);

        List<MPVariable> yVariables = new ArrayList<>();
        MPVariable Y_W__W_W = solver.makeBoolVar("Y_W__W_W"); //Y444
        yVariables.add(Y_W__W_W);
        MPVariable Y_W__W_H1 = solver.makeBoolVar("Y_W__W_H1"); //Y446
        yVariables.add(Y_W__W_H1);
        MPVariable Y_W__W_H2 = solver.makeBoolVar("Y_W__W_H2"); //Y448
        yVariables.add(Y_W__W_H2);
        MPVariable Y_W__H1_H1 = solver.makeBoolVar("Y_W__H1_H1"); //Y466
        yVariables.add(Y_W__H1_H1);
        MPVariable Y_W__H1_H2 = solver.makeBoolVar("Y_W__H1_H2"); //Y468
        yVariables.add(Y_W__H1_H2);
        MPVariable Y_W__H2_H2 = solver.makeBoolVar("Y_W__H2_H2"); //Y488
        yVariables.add(Y_W__H2_H2);
        MPVariable Y_H1__W_W = solver.makeBoolVar("Y_H1__W_W"); //Y644
        yVariables.add(Y_H1__W_W);
        MPVariable Y_H1__W_H1 = solver.makeBoolVar("Y_H1__W_H1"); //Y646
        yVariables.add(Y_H1__W_H1);
        MPVariable Y_H1__W_H2 = solver.makeBoolVar("Y_H1__W_H2"); //Y648
        yVariables.add(Y_H1__W_H2);
        MPVariable Y_H1__H1_H1 = solver.makeBoolVar("Y_H1__H1_H1"); //Y666
        yVariables.add(Y_H1__H1_H1);
        MPVariable Y_H1__H1_H2 = solver.makeBoolVar("Y_H1__H1_H2"); //Y668
        yVariables.add(Y_H1__H1_H2);
        MPVariable Y_H1__H2_H2 = solver.makeBoolVar("Y_H1__H2_H2"); //Y688
        yVariables.add(Y_H1__H2_H2);
        MPVariable Y_H2__W_W = solver.makeBoolVar("Y_H2__W_W"); //Y844
        yVariables.add(Y_H2__W_W);
        MPVariable Y_H2__W_H1 = solver.makeBoolVar("Y_H2__W_H1"); //Y846
        yVariables.add(Y_H2__W_H1);
        MPVariable Y_H2__W_H2 = solver.makeBoolVar("Y_H2__W_H2"); //Y848
        yVariables.add(Y_H2__W_H2);
        MPVariable Y_H2__H1_H1 = solver.makeBoolVar("Y_H2__H1_H1"); //Y866
        yVariables.add(Y_H2__H1_H1);
        MPVariable Y_H2__H1_H2 = solver.makeBoolVar("Y_H2__H1_H2"); //Y868
        yVariables.add(Y_H2__H1_H2);
        MPVariable Y_H2__H2_H2 = solver.makeBoolVar("Y_H2__H2_H2"); //Y888
        yVariables.add(Y_H2__H2_H2);

        MPConstraint A__W_W = solver.makeConstraint(cell11, Integer.MAX_VALUE, "A__W_W"); //A44
        A__W_W.setCoefficient(X_W__W_W, 2); //X444
        A__W_W.setCoefficient(X_W__W_H1, 1); //X446
        A__W_W.setCoefficient(X_W__W_H2, 1); //X448

        MPConstraint A__W_H1 = solver.makeConstraint(cell12, Integer.MAX_VALUE, "A__W_H1"); //A46
        A__W_H1.setCoefficient(X_W__W_H1, 1); //X446
        A__W_H1.setCoefficient(X_W__H1_H1, 2); //X466
        A__W_H1.setCoefficient(X_W__H1_H2, 1); //X468
        A__W_H1.setCoefficient(X_H1__W_W, 2); //X644
        A__W_H1.setCoefficient(X_H1__W_H1, 1); //X646
        A__W_H1.setCoefficient(X_H1__W_H2, 1); //X648

        MPConstraint A__W_H2 = solver.makeConstraint(cell13, Integer.MAX_VALUE, "A__W_H2"); //A48
        A__W_H2.setCoefficient(X_W__W_H2, 1); //X448
        A__W_H2.setCoefficient(X_W__H1_H2, 1); //X468
        A__W_H2.setCoefficient(X_W__H2_H2, 2); //X488
        A__W_H2.setCoefficient(X_H2__W_W, 2); //X844
        A__W_H2.setCoefficient(X_H2__W_H1, 1); //X846
        A__W_H2.setCoefficient(X_H2__W_H2, 1); //X848

        MPConstraint A__H1_H1 = solver.makeConstraint(cell22, Integer.MAX_VALUE, "A__H1_H1"); //A66
        A__H1_H1.setCoefficient(X_H1__W_H1, 1); //X646
        A__H1_H1.setCoefficient(X_H1__H1_H1, 2); //X666
        A__H1_H1.setCoefficient(X_H1__H1_H2, 1); //X668

        MPConstraint A__H1_H2 = solver.makeConstraint(cell23, Integer.MAX_VALUE, "A__H1_H2"); //A68
        A__H1_H2.setCoefficient(X_H1__W_H2, 1); //X648
        A__H1_H2.setCoefficient(X_H1__H1_H2, 1); //X668
        A__H1_H2.setCoefficient(X_H1__H2_H2, 2); //X688
        A__H1_H2.setCoefficient(X_H2__W_H1, 1); //X846
        A__H1_H2.setCoefficient(X_H2__H1_H1, 2); //X866
        A__H1_H2.setCoefficient(X_H2__H1_H2, 1); //X868

        MPConstraint A__H2_H2 = solver.makeConstraint(cell33, Integer.MAX_VALUE, "A__H2_H2"); //A88
        A__H2_H2.setCoefficient(X_H2__W_H2, 1); //X848
        A__H2_H2.setCoefficient(X_H2__H1_H2, 1); //X868
        A__H2_H2.setCoefficient(X_H2__H2_H2, 2); //X888

        MPConstraint link1 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link1.setCoefficient(X_W__W_W, 1);
        link1.setCoefficient(Y_W__W_W, _mValue);

        MPConstraint link2 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link2.setCoefficient(X_W__W_H1, 1);
        link2.setCoefficient(Y_W__W_H1, _mValue);

        MPConstraint link3 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link3.setCoefficient(X_W__W_H2, 1);
        link3.setCoefficient(Y_W__W_H2, _mValue);

        MPConstraint link4 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link4.setCoefficient(X_W__H1_H1, 1);
        link4.setCoefficient(Y_W__H1_H1, _mValue);

        MPConstraint link5 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link5.setCoefficient(X_W__H1_H2, 1);
        link5.setCoefficient(Y_W__H1_H2, _mValue);

        MPConstraint link6 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link6.setCoefficient(X_W__H2_H2, 1);
        link6.setCoefficient(Y_W__H2_H2, _mValue);

        MPConstraint link7 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link7.setCoefficient(X_H1__W_W, 1);
        link7.setCoefficient(Y_H1__W_W, _mValue);

        MPConstraint link8 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link8.setCoefficient(X_H1__W_H1, 1);
        link8.setCoefficient(Y_H1__W_H1, _mValue);

        MPConstraint link9 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link9.setCoefficient(X_H1__W_H2, 1);
        link9.setCoefficient(Y_H1__W_H2, _mValue);

        MPConstraint link10 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link10.setCoefficient(X_H1__H1_H1, 1);
        link10.setCoefficient(Y_H1__H1_H1, _mValue);

        MPConstraint link11 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link11.setCoefficient(X_H1__H1_H2, 1);
        link11.setCoefficient(Y_H1__H1_H2, _mValue);

        MPConstraint link12 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link12.setCoefficient(X_H1__H2_H2, 1);
        link12.setCoefficient(Y_H1__H2_H2, _mValue);

        MPConstraint link13 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link13.setCoefficient(X_H2__W_W, 1);
        link13.setCoefficient(Y_H2__W_W, _mValue);

        MPConstraint link14 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link14.setCoefficient(X_H2__W_H1, 1);
        link14.setCoefficient(Y_H2__W_H1, _mValue);

        MPConstraint link15 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link15.setCoefficient(X_H2__W_H2, 1);
        link15.setCoefficient(Y_H2__W_H2, _mValue);

        MPConstraint link16 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link16.setCoefficient(X_H2__H1_H1, 1);
        link16.setCoefficient(Y_H2__H1_H1, _mValue);

        MPConstraint link17 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link17.setCoefficient(X_H2__H1_H2, 1);
        link17.setCoefficient(Y_H2__H1_H2, _mValue);

        MPConstraint link18 = solver.makeConstraint(Integer.MIN_VALUE, 0);
        link18.setCoefficient(X_H2__H2_H2, 1);
        link18.setCoefficient(Y_H2__H2_H2, _mValue);

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

        objective.setCoefficient(Y_W__W_W, _transitionTime);
        objective.setCoefficient(Y_W__W_H1, _transitionTime);
        objective.setCoefficient(Y_W__W_H2, _transitionTime);
        objective.setCoefficient(Y_W__H1_H1, _transitionTime);
        objective.setCoefficient(Y_W__H1_H2, _transitionTime);
        objective.setCoefficient(Y_W__H2_H2, _transitionTime);
        objective.setCoefficient(Y_H1__W_W, _transitionTime);
        objective.setCoefficient(Y_H1__W_H1, _transitionTime);
        objective.setCoefficient(Y_H1__W_H2, _transitionTime);
        objective.setCoefficient(Y_H1__H1_H1, _transitionTime);
        objective.setCoefficient(Y_H1__H1_H2, _transitionTime);
        objective.setCoefficient(Y_H1__H1_H2, _transitionTime);
        objective.setCoefficient(Y_H2__W_W, _transitionTime);
        objective.setCoefficient(Y_H2__W_H1, _transitionTime);
        objective.setCoefficient(Y_H2__W_H2, _transitionTime);
        objective.setCoefficient(Y_H2__H1_H1, _transitionTime);
        objective.setCoefficient(Y_H2__H1_H2, _transitionTime);
        objective.setCoefficient(Y_H2__H2_H2, _transitionTime);

        // Objective direction
        objective.setMinimization();

        // Solve the model
        MPSolver.ResultStatus resultStatus = solver.solve();
        resultArea.setText("");
        resultArea.append("\n============ NEW RUN ============\n");
        resultArea.append("Number of variables = " + solver.numVariables() + "\n");
        resultArea.append("Number of constraints = " + solver.numConstraints() + "\n");
        // Check that the problem has an optimal solution
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            // Output solution values
            StringBuilder xResults = new StringBuilder();
            xResults.append("Solution found! Here are the non-zero build configurations:\n");
            int val = 0;
            for (MPVariable variable : xVariables) {
                if (variable.solutionValue() != 0) {
                    val += (int) variable.solutionValue();
                    xResults.append(variable.name()).append(": ").append(Math.round(variable.solutionValue()))
                            .append("\n");
                }
            }
            StringBuilder yResults = new StringBuilder();
            resultArea.append(String.valueOf(yResults));
            yResults.append("Here are the transitions:\n");
            for (MPVariable variable : yVariables) {
                if (variable.solutionValue() != 0) {
                    val += _transitionTime;
                    yResults.append(variable.name()).append("\n");
                }
            }
            resultArea.append(String.valueOf(xResults));
            resultArea.append(String.valueOf(yResults));
            resultArea.append("\n Total days needed at minimum is: " + val + "\n");
        } else {
            resultArea.setText("\nNo solution found, result status is: " + resultStatus + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BlockCalculatorUI ui = new BlockCalculatorUI();
            ui.pack(); // Resize frame to fit the table's preferred size
            ui.setLocationRelativeTo(null); // Center the window on the screen
            ui.setVisible(true);
        });
    }
}

