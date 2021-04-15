package parser;


import errorhandling.IErrorReporter;
import lexical.token.Label;

public interface ILabelValidator {


    void addOperandLabel(Label label);

    void addInstructionLabel(Label label, IErrorReporter errorReporter);

    void checkIfDefined(IErrorReporter errorReporter);
}
