package parser;

import errorhandling.ErrorMsg;
import errorhandling.IErrorReporter;
import ir.IntermediateRep;
import ir.LineStatement;
import lexical.LexicalScanner;
import lexical.token.*;
import utils.SymbolTable;

public class Parser implements IParser {
    //Sequence of line statements
    private IntermediateRep ir = new IntermediateRep();
    private LexicalScanner lexicalScanner = null;
    private SymbolTable<String, Mnemonic> keywords = null;
    private Token nextToken;
    private final IErrorReporter errorReporter;
    private boolean isTesting = false;
    private LineStatement ls = new LineStatement();
    private boolean verbose = false;

    public Parser(LexicalScanner lexicalScanner, SymbolTable<String, Mnemonic> keywords, boolean verbose, IErrorReporter errorRep) {
        this(lexicalScanner, keywords, verbose, errorRep, false);
    }

    public Parser(LexicalScanner lexicalScanner, SymbolTable<String, Mnemonic> keywords, boolean verbose, IErrorReporter errorRep, boolean isTesting) {
        this.isTesting = isTesting;
        this.lexicalScanner = lexicalScanner;
        this.keywords = keywords;
        this.getNextToken();
        this.errorReporter = errorRep;
        this.verbose = verbose;
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
        if (verbose){
            System.out.println("Parsing tokens.\n");
        }
        while (true) {
            TokenType type = nextToken.getType();
            Position position = nextToken.getPosition();
            String value = nextToken.getValue();
            Instruction inst = ls.getInstruction();
            switch (type) {
                //If token is EOL, LS is finished, add it to IR and start a new one.
                //Depending on how the input file is made, if does not end with an EOL, it will end with an EOF
                case EOF:
                case EOL:
                    //Error Reporting
                    //errorReporting(ls);

                    ir.add(ls);
                    ls = new LineStatement();
                    if (nextToken.getType() == TokenType.EOF) {
                        if (verbose)
                            System.out.println("Tokens Sucessfully parsed.\n");
                        return;
                    }
                    break;
                case LABEL:
                    Label lb = new Label(position, value);
                    //Create label for mnemonic
                    if (inst != null && inst.getMnemonic() != null) {
                        inst.setOperand(new Operand(position, value));
                        inst.getOperand().setLabel(lb);
                    } else { // else it is an instruction label
                        ls.setLabel(lb);
                    }
                    break;
                case DIRECTIVE:
                    Directive dr = new Directive(position);
                    dr.setDirective(value);
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
                    if (mnemonic != null) {
                        ls.setInstruction(new Instruction(position, value)); // Set instruction
                        //Set newly created instruction's mnemonic
                        ls.getInstruction().setMnemonic(new Mnemonic(position, value));
                        ls.getInstruction().getMnemonic().setMode(mnemonic.getMode());
                        // Shu: Added this line
                        ls.getInstruction().getMnemonic().setOpCode(keywords.get(value).getOpCode());
                    } else {
                        ErrorMsg errorMsg = new ErrorMsg("Invalid mnemonic.", position);
                        this.errorReporter.record(errorMsg);
                    }
                    break;
                case OPERAND:
                    //System.out.println("[Debug] - " + nextToken);
                    // Shu: Code to figure out how much to ass to the opcode to make it match the table the prof gave.
                    // Shu: TLDR: base from keywords + operand + offset to account for bit shifts = opcode
                    Mnemonic mne = ls.getInstruction().getMnemonic();
                    int opc = Integer.parseInt(value);
                    try {
                        switch (mne.getValue().split("\\.")[1]) {
                            case "i3":
                                if (opc < 0) {
                                    opc += 8;
                                }
                                break;
                            case "u5":
                                if (opc < 16) {
                                    opc += 16;
                                } else {
                                    opc -= 16;
                                }
                                break;
                        }
                    } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    }
                    opc += mne.getOpCode();
                    ls.getInstruction().getMnemonic().setOpCode(opc); //Set mnemonic's opcode
                    Operand operand = new Operand(position, value);
                    operand.setOperand(Integer.parseInt(value));
                    ls.getInstruction().setOperand(operand); //set instruction's opcode
                    break;
                default:
                    ErrorMsg unknown_token = new ErrorMsg("Unknown token", nextToken.getPosition());
                    errorReporter.record(unknown_token);
            }
            if (isTesting) {
                return;
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
                    String msg = checkInvalidOperand(ls);
                    if (!msg.equals("")) {
                        errorMsg.setMessage(msg);
                    }
                }
            }
            if (!errorMsg.getMessage().isEmpty()) {
                errorMsg.setPosition(token.getPosition());
                this.errorReporter.record(errorMsg);
            }
        }
    }

    private String checkInvalidOperand(LineStatement ls) {
        String errorMessage = "";
        String suffix = getSuffix(ls.getInstruction().getValue());
        int opCode = Integer.parseInt(ls.getInstruction().getOperand().getValue());
        String mnemonic = ls.getInstruction().getMnemonic().getValue();

        if (suffix != null) {
            switch (suffix) {
                case "u5":
                    if (opCode < 0 || opCode > 31) {
                        errorMessage = "The immediate instruction \'" + mnemonic +
                                "\' must have a 5-bit unsigned operand number ranging from 0 to 31.";
                    }
                    break;
                case "u3":
                    if (opCode < 0 || opCode > 7) {
                        errorMessage = "The immediate instruction \'" + mnemonic +
                                "\' must have a 3-bit unsigned operand number ranging from 0 to 7.";
                    }
                    break;
                case "i3":
                    if (opCode < -4 || opCode > 3) {
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
     *
     * @param value
     * @return
     */
    private String getSuffix(String value) {
        String[] opCode = value.split("\\.");
        if (opCode.length == 0) {
            return "";
        } else return opCode[opCode.length - 1];
    }

    private void getNextToken() {
        if (!isTesting) {
            this.nextToken = lexicalScanner.getNextToken();
        }
    }

    public void setNextToken(Token nextToken) {
        this.nextToken = nextToken;
    }

    public IntermediateRep getIr() {
        return ir;
    }

    public boolean isTesting() {
        return isTesting;
    }

    public void setTesting(boolean isTesting) {
        this.isTesting = isTesting;
    }

    public void setIr(IntermediateRep ir) {
        this.ir = ir;
    }
}
