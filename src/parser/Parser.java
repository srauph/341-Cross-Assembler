package parser;

import errorReporting.ErrorMsg;
import errorReporting.IErrorReporter;
import ir.IntermediateRep;
import ir.LineStatement;
import lexical.LexicalScanner;
import lexical.token.*;
import utils.SymbolTable;

public class Parser implements IParser {
    //Sequence of line statements
    private final IntermediateRep ir = new IntermediateRep();
    // private final LinkedList<ir.LineStatement> intermediateRep = new LinkedList<>();
    private final LexicalScanner lexicalScanner;
    private final SymbolTable<String, Mnemonic> keywords;
    private Token nextToken;
    private IErrorReporter errorReporter;

    public Parser(LexicalScanner lexicalScanner, SymbolTable<String, Mnemonic> keywords, IErrorReporter errorRep) {
        this.lexicalScanner = lexicalScanner;
        this.keywords = keywords;
        this.nextToken = lexicalScanner.getNextToken();
        this.errorReporter = errorRep;
    }

    /**
     * Generates line statements based upon tokens from the lexical analyzer.
     * Currently supports only Mnemomics.
     * <p>
     * <p>
     * Each LineStatement is composed of three optional parts (a label,
     * an instruction/directive and a comment) followed by an end-of-line (EOL).
     * <p>
     * LineStatement = [ Label ] [ Instruction | Directive ] [ Comment ] EOL .
     */
    public void parseTokens() {
        LineStatement ls = new LineStatement();
        while (nextToken == null || nextToken.getType() != TokenType.EOF) {
            if (nextToken == null) {
                getNextToken();
                continue;
            }
            TokenType type = nextToken.getType();
            Position position = nextToken.getPosition();
            String value = nextToken.getValue();

            switch (type) {
                case EOL:               //If token is EOL, LS is finished, add it to IR and start a new one.
                    if (ls.getInstruction() != null) {
                        if (ls.getInstruction().getOperand() == null) {
                            ls.getInstruction().getMnemonic().setMode("inherent");
                        }
                    }

                    //Error Reporting
                    errorReporting(ls);

                    ir.add(ls);
                    ls = new LineStatement();
                    break;
                case DIRECTIVE:
                    Directive dr = new Directive(position, value);
                    dr.setStringOperand(value);
                    ls.setDirective(dr);
                    break;
                case STRING_OPERAND:
                    if (ls.getDirective() != null) {
                        ls.getDirective().setStringOperand(value);
                    }
                    break;
                case COMMENT:           // If token is a comment
                    ls.setComment(new Comment(position, value));
                    break;
                case MNEMONIC:          //If token is a mnemonic
                    Mnemonic mnemonic = keywords.get(value);
                    if(mnemonic != null) {
                        ls.setInstruction(new Instruction(position, value)); // Set instruction
                        //Set newly created instruction's mnemonic
                        ls.getInstruction().setMnemonic(new Mnemonic(position, value));
                        ls.getInstruction().getMnemonic().setMode(mnemonic.getMode());
                    } else {
                        ErrorMsg errorMsg = new ErrorMsg("Invalid mnemonic or directive.", position);
                        this.errorReporter.record(errorMsg);
                    }
                    break;
                case OPERAND:
                    //System.out.println("[Debug] - " + nextToken);
                    ls.getInstruction().getMnemonic().setOpCode(Integer.parseInt(value)); //Set mnemonic's opcode
                    Operand operand = new Operand(position, value);
                    operand.setOperand(Integer.parseInt(value));
                    ls.getInstruction().setOperand(operand); //set instruction's opcode
                    ls.getInstruction().getMnemonic().setMode("immediate");
                    break;
                default:
                    ErrorMsg unknown_token = new ErrorMsg("Unknown token", nextToken.getPosition());
                    errorReporter.record(unknown_token);
            }
            //Get the next token to process
            getNextToken();
        }
    }

    public IntermediateRep getIR() {
        return this.ir;
    }

    /**
     * Analyzes line statements and report error if one is found.
     *
     * @param ls
     */
    private void errorReporting(LineStatement ls) {
        ErrorMsg errorMsg = new ErrorMsg();
        Token token = nextToken;

        if (ls.getInstruction() != null) {//if no instruction then we assume its a line with only a comment and ignore it
           if (keywords.get(ls.getInstruction().getMnemonic().getValue()) != null) {
                if (keywords.get(ls.getInstruction().getMnemonic().getValue()).getMode().equals("immediate") && ls.getInstruction().getOperand() == null) { //If instruction in not inherent (immediate or relative) but does not have an operand
                    errorMsg.setMessage("Instruction requires an operand.");
                } else if (keywords.get(ls.getInstruction().getMnemonic().getValue()).getMode().equals("inherent") && ls.getInstruction().getOperand() != null) { //If instruction is inherent but contains an operand
                    errorMsg.setMessage("Inherent instruction must not have an operand.");
                } else {
                    String msg = isValidOperand(ls);
                    if(!msg.equals("")) {
                        errorMsg.setMessage(isValidOperand(ls));
                    }
                }
            }
            if (!errorMsg.getMessage().isEmpty()) {
                errorMsg.setPosition(token.getPosition());
                this.errorReporter.record(errorMsg);
            }
        }
    }

    private String isValidOperand(LineStatement ls) {
        String errorMessage = "";
        String suffix = getSuffix(ls.getInstruction().getValue());
        int opCode = Integer.parseInt(ls.getInstruction().getOperand().getValue());
        String mnemonic = ls.getInstruction().getMnemonic().getValue();

        if(suffix != null) {
            switch (suffix) {
                case "u5":
                    if (opCode < 0 || opCode > 31) {
                        errorMessage = "The immediate instruction \'" + mnemonic +
                                "\' must have a 5-bit unsigned operand number ranging from 0 to 31.";
                    }
                    break;
                case "u3":
                    if (opCode < 0 || opCode > 7){
                        errorMessage = "The immediate instruction \'" + mnemonic +
                                "\' must have a 3-bit unsigned operand number ranging from 0 to 7.";
                    }
                    break;
                case "i3":
                    if (opCode < -4 || opCode > 3){
                        errorMessage = "The immediate instruction \'" + mnemonic +
                                "\' must have a 3-bit unsigned operand number ranging from -4 to 3.";
                    }
                    break;
            }
        }
        return errorMessage;
    }


    /**
     * Returns a string corresponding to the suffix of passed Mnemonic. Suffix
     * express the size and range of fields withing operation codes and operands.
     * @param value
     * @return
     */
    private String getSuffix(String value) {
        String[] opCode = value.split("\\.");
        if(opCode.length == 0) {
            return "";
        } else return opCode[opCode.length - 1];
    }

    private void getNextToken() {
        this.nextToken = lexicalScanner.getNextToken();
    }
}
