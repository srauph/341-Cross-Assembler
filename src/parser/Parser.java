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
    private final LabelValidator labelValidator = new LabelValidator();
    private final LexicalScanner lexicalScanner;
    private final SymbolTable<String, Mnemonic> keyword;
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
        this.keyword = keywords;
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
        if (verbose) {
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
                    // Error reporting for when an instruction (immediate or relative) does not have an operand
                    instructionErrorReporting(ls, nextToken);

                    // Relative instruction error reporting, checks if a relative instruction does not refer to a label
                    relativeInstructionErrorReporting(ls, nextToken);

                    ir.add(ls);
                    ls = new LineStatement();
                    if (nextToken.getType() == TokenType.EOF) {

                        //check label if it exists
                        operandLabelErrorReporting();

                        if (verbose) {
                            System.out.println("Tokens Successfully parsed.\n");
                        }
                        return;
                    }
                    break;
                case LABEL:
                    Label lb = new Label(position, value);

                    //Create label for mnemonic
                    if (inst != null && inst.getMnemonic() != null) {
                        inst.setOperand(new Operand(position, value));
                        inst.getOperand().setLabel(lb);
                        labelValidator.addOperandLabel(lb);
                    } else { // else it is an instruction label
                        ls.setLabel(lb);
                        instructionLabelErrorReporting(lb);
                    }
                    break;
                case DIRECTIVE:
                    Directive dr = new Directive(position);
                    dr.setDirective(value);
                    ls.setDirective(dr);
                    break;
                case STRING_OPERAND:
                    if (ls.getDirective() != null) {
                        ls.getDirective().setStringOperand(value.replaceAll("\"", ""));
                        String operand = ls.getDirective().getStringOperand();
                        int size = operand.length() + 1;
                        int[] arr = new int[size];
                        for (int i = 0; i < size - 1; i++) {
                            arr[i] = operand.charAt(i);
                        }
                        arr[size - 1] = 0;
                        ls.setCode(arr);
                        ls.setResolved(true);
                    }
                    break;
                case COMMENT:           // If token is a comment
                    ls.setComment(new Comment(position, value));
                    break;
                case MNEMONIC:          //If token is a mnemonic
                    Mnemonic mnemonic = keyword.get(value);

                    if (mnemonic != null) {
                        ls.setInstruction(new Instruction(position, value)); // Set instruction
                        //Set newly created instruction's mnemonic
                        ls.getInstruction().setMnemonic(new Mnemonic(position, value));
                        ls.getInstruction().getMnemonic().setMode(mnemonic.getMode());
                        // Shu: Added this line
                        ls.getInstruction().getMnemonic().setOpCode(keyword.get(value).getOpCode());

                        if (ls.getInstruction().getMnemonic().getMode().equals("inherent")) {
                            //machine code for inherent
                            ls.setCode(new int[]{keyword.get(value).getOpCode()});
                            ls.setResolved(true);
                        }
                    } else {
                        invalidMnemonicErrorReporting(position);
                    }
                    break;
                case OPERAND:
                    //System.out.println("[Debug] - " + nextToken);
                    // Shu: Code to figure out how much to ass to the opcode to make it match the table the prof gave.
                    // Shu: TLDR: base from keywords + operand + offset to account for bit shifts = opcode


                    // Operand error reporting
                    operandErrorReporting(ls, nextToken);

                    Operand operand = new Operand(position, value);
                    operand.setOperand(Integer.parseInt(value));
                    ls.getInstruction().setOperand(operand);

                    //Assign their machine code
                    if (ls.getInstruction() != null) {
                        if (ls.getInstruction().getMnemonic() != null) {
                            String mnemonicString = ls.getInstruction().getMnemonic().getValue();
                            if (ls.getInstruction().getMnemonic().getMode().equals("immediate")) {
                                //machine code for immediate
                                ls.setCode(new int[]{keyword.get(mnemonicString).getOpCode() + ls.getInstruction().getOperand().getOperand()});
                            } else if (ls.getInstruction().getMnemonic().getMode().equals("relative") && ls.getLabel() == null) {
                                //machine code for relative
                                ls.setCode(new int[]{keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode(),
                                        ls.getInstruction().getOperand().getOperand()});
                            }
                        }
                        ls.setResolved(true);
                    }
                    break;
                default:
                    unknownTokenErrorReporting();
            }
            if (isTesting) {
                return;
            }
            //Get the next token to process
            getNextToken();
        }
    }

    /**
     * Checks if the instruction is already defined, and if it is already defined the error reporter will record an error.
     *
     * @param lb
     */
    private void instructionLabelErrorReporting(Label lb) {
        labelValidator.instructionLabelErrorReporting(lb, errorReporter);
    }

    /**
     * Checks if an operand label is defined by an instruction label and if not records an error.
     */
    private void operandLabelErrorReporting() {
        labelValidator.operandLabelErrorReporting(errorReporter);
    }

    /**
     * Records an error to the error reporter if an unknown token is found
     */
    private void unknownTokenErrorReporting() {
        ErrorMsg unknown_token = new ErrorMsg("Unknown token", nextToken.getPosition());
        errorReporter.record(unknown_token);
    }

    /**
     * Records an error to the error reporter if an invalid mnemonic is found
     *
     * @param position
     */
    private void invalidMnemonicErrorReporting(Position position) {
        ErrorMsg errorMsg = new ErrorMsg("Invalid mnemonic.", position);
        this.errorReporter.record(errorMsg);
    }

    /**
     * Records an error to the error reporter if a relative instruction does not refer to a label (except ldc.18, ldc.i16 and ldc.i32)
     *
     * @param ls
     * @param nextToken
     */
    private void relativeInstructionErrorReporting(LineStatement ls, Token nextToken) {
        // If there is no instruction, then we assume it's a line with only a comment and ignore it
        if (ls.getInstruction() != null) {

            // Extracting mnemonic name to check if it's not ldc.18, ldc.i16 or ldc.i32
            String[] mnemonicValue = ls.getInstruction().getMnemonic().getValue().split("\\.");
            if (mnemonicValue.length > 0) {
                if ((ls.getInstruction() != null) && (keyword.get(ls.getInstruction().getMnemonic().getValue()) != null)
                        && !mnemonicValue[0].equals("ldc") && !mnemonicValue[0].equals("ldv") && !mnemonicValue[0].equals("stv")) {
                    if (keyword.get(ls.getInstruction().getMnemonic().getValue()).getMode().equals("relative") &&
                            (ls.getInstruction().getOperand().getLabel() == null)) {
                        ErrorMsg errorMsg = new ErrorMsg("Relative instruction operand must refer to a label.", nextToken.getPosition());
                        errorReporter.record(errorMsg);
                    }
                }
            }

        }
    }

    /**
     * Records an error to the error reporter if an instruction (relative or immediate) does not have an operand
     *
     * @param ls
     * @param nextToken
     */
    private void instructionErrorReporting(LineStatement ls, Token nextToken) {
        // If there is no instruction, then we assume it's a line with only a comment and ignore it
        if ((ls.getInstruction() != null) && (keyword.get(ls.getInstruction().getMnemonic().getValue()) != null)) {
            if ((keyword.get(ls.getInstruction().getMnemonic().getValue()).getMode().equals("relative") ||
                    keyword.get(ls.getInstruction().getMnemonic().getValue()).getMode().equals("immediate")) &&
                    ls.getInstruction().getOperand() == null) {
                ErrorMsg errorMsg = new ErrorMsg("Instruction requires an operand.", nextToken.getPosition());
                errorReporter.record(errorMsg);
            }
        }
    }

    /**
     * Records an error to the error reporter if a mnemonic's operand falls outside its range value or
     * if an inherent instruction does not have an operand
     *
     * @param ls
     * @param nextToken
     */
    private void operandErrorReporting(LineStatement ls, Token nextToken) {
        String message = checkInvalidOperand(ls, nextToken.getValue());
        if (!message.isEmpty()) {
            ErrorMsg errorMsg = new ErrorMsg(message, nextToken.getPosition());
            errorReporter.record(errorMsg);
        }

        // Checking if inherent instruction has an operand
        else if (keyword.get(ls.getInstruction().getMnemonic().getValue()).getMode().equals("inherent") && nextToken.getValue() != null) {
            ErrorMsg errorMsg = new ErrorMsg("Inherent instruction must not have an operand", nextToken.getPosition());
            errorReporter.record(errorMsg);
        }
    }

    public IntermediateRep getIR() {
        return this.ir;
    }

    /**
     * Returns a string corresponding to the error if the operand falls outside the corresponding mnemonic's
     * range values
     *
     * @param ls
     * @param value
     * @return
     */
    private String checkInvalidOperand(LineStatement ls, String value) {
        String errorMessage = "";
        String suffix = getSuffix(ls.getInstruction().getValue());
        long opCode = Long.parseLong(value);
        String mnemonic = ls.getInstruction().getMnemonic().getValue();

        if (suffix != null) {
            switch (suffix) {

                case "u3":
                    if (opCode < 0 || opCode > 7) {
                        errorMessage = "The instruction " + mnemonic + "'s operand number not in an u3 range [0..+7].";
                    }
                    break;
                case "i3":
                    if (opCode < -4 || opCode > 3) {
                        errorMessage = "The instruction " + mnemonic + "'s operand number not in an i3 range [-4..+3].";
                    }
                    break;
                case "u5":
                    if (opCode < 0 || opCode > 31) {
                        errorMessage = "The instruction " + mnemonic + "'s operand number not in an u5 range [0..+31].";
                    }
                    break;
                case "i5":
                    if (opCode < -16 || opCode > 15) {
                        errorMessage = "The instruction " + mnemonic + "'s operand not in an i5 range [-16..+15].";
                    }
                    break;
                case "i8":
                    if (opCode < -128 || opCode > 127) {
                        errorMessage = "The instruction " + mnemonic + "'s operand number not in an i8 range [-128..+127].";
                    }
                    break;
                case "u8":
                    if (opCode < 0 || opCode > 255) {
                        errorMessage = "The instruction " + mnemonic + "'s operand number not in an u8 range [0..+255].";
                    }
                    break;
                case "i16":
                    if (opCode < -32768 || opCode > 32767) {
                        errorMessage = "The instruction " + mnemonic + "'s operand number not in an i16 range [-32768..+32767].";
                    }
                    break;
                case "u16":
                    if (opCode < -0 || opCode > 65535) {
                        errorMessage = "The instruction " + mnemonic + "'s operand number not in an u16 range [0..+65535].";
                    }
                    break;
                case "u32":
                    if (opCode < 0 || opCode > 4294967295L) {
                        errorMessage = "The instruction " + mnemonic + "'s operand number not in an u32 range [0..+4294967295].";
                    }
                    break;
                case "i32":
                    if (opCode < -2147483648 || opCode > 2147483647L) {
                        errorMessage = "The instruction " + mnemonic + "'s operand number not in an i32 range [-2147483648..+2147483647].";
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
