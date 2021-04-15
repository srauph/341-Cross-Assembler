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

    public Parser(LexicalScanner lexicalScanner, SymbolTable<String, Mnemonic> keywords, IErrorReporter errorRep) {
        this(lexicalScanner, keywords, errorRep, false);
    }

    public Parser(LexicalScanner lexicalScanner, SymbolTable<String, Mnemonic> keywords, IErrorReporter errorRep, boolean isTesting) {
        this.isTesting = isTesting;
        this.lexicalScanner = lexicalScanner;
        this.keywords = keywords;
        this.getNextToken();
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
                    /**
                     * (certain) : i put error reporting for when an instruction doesnt have an operand here because i couldnt figure
                     * out how to do it from "case:MNEMONIC" as i would need to do lexicalScanner.getNextToken and that messes
                     * things up. lmk if u know a different way
                     */
                    // Error reporting for when an instruction (immediate or relative) does not have an operand
                    instructionErrorReporting(ls, nextToken);

                    ir.add(ls);
                    ls = new LineStatement();
                    if (nextToken.getType() == TokenType.EOF) {
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


                    // Operand error reporting
                    operandErrorReporting(ls, nextToken);

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

    private void instructionErrorReporting(LineStatement ls, Token nextToken) {
        // If there is no instruction, then we assume it's a line with only a comment and ignore it
        if((ls.getInstruction() != null) && (keywords.get(ls.getInstruction().getMnemonic().getValue()) != null)) {
            if (!keywords.get(ls.getInstruction().getMnemonic().getValue()).getMode().equals("inherent") && ls.getInstruction().getOperand() == null) {
                ErrorMsg errorMsg = new ErrorMsg("Instruction requires an operand.", nextToken.getPosition());
                errorReporter.record(errorMsg);
            }
        }
    }

    private void operandErrorReporting(LineStatement ls, Token nextToken){
        String message = checkInvalidOperand(ls, nextToken.getValue());
        if(!message.equals("")){
            ErrorMsg errorMsg = new ErrorMsg(message, nextToken.getPosition());
            errorReporter.record(errorMsg);
        }

        // Checking if inherent instruction has an operand
        else if(keywords.get(ls.getInstruction().getMnemonic().getValue()).getMode().equals("inherent") && nextToken.getValue() != null){
            ErrorMsg errorMsg = new ErrorMsg("Inherent instruction must not have an operand", nextToken.getPosition());
            errorReporter.record(errorMsg);
        }
    }

    public IntermediateRep getIR() {
        return this.ir;
    }

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
                    if (opCode < -128 || opCode > 128) {
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
