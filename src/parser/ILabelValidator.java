package parser;


import errorhandling.IErrorReporter;
import lexical.token.Label;

public interface ILabelValidator {


    void addOperandLabel(Label label);

    void instructionLabelErrorReporting(Label label, IErrorReporter errorReporter);

    void operandLabelErrorReporting(IErrorReporter errorReporter);
}
