package parser;

import errorhandling.ErrorMsg;
import errorhandling.IErrorReporter;
import lexical.token.Label;

import java.util.ArrayList;

public class LabelValidator implements ILabelValidator {

    private final ArrayList<Label> operandLabel = new ArrayList<>();
    private final ArrayList<String> instructionLabel = new ArrayList<>();

    /**
     * Adds an operand label to the operandLabel array.
     *
     * @param label
     */
    public void addOperandLabel(Label label) {
        this.operandLabel.add(label);
    }


    /**
     * Adds an instruction label to the instructionLabel array.
     * If the instruction is already defined the error reporter will record the error.
     *
     * @param label
     * @param errorReporter
     */
    public void instructionLabelErrorReporting(Label label, IErrorReporter errorReporter) {
        if (instructionLabel.contains(label.getLabel())) {
            ErrorMsg errorMsg = new ErrorMsg(label.getLabel() + " label already defined.", label.getPosition());
            errorReporter.record(errorMsg);
        } else {
            this.instructionLabel.add(label.getLabel());
        }
    }

    /**
     * Checks if an operand label is defined by an instruction label and if not records an error.
     *
     * @param errorReporter
     */
    public void operandLabelErrorReporting(IErrorReporter errorReporter) {
        for (Label operandLabel : operandLabel) {
            if (operandLabel != null && !instructionLabel.contains(operandLabel.getLabel())) {
                ErrorMsg errorMsg = new ErrorMsg(operandLabel.getLabel() + " label not found (or defined).", operandLabel.getPosition());
                errorReporter.record(errorMsg);
            }
        }
    }
}
