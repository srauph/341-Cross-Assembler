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
     *
     *
     * Each LineStatement is composed of three optional parts (a label,
     * an instruction/directive and a comment) followed by an end-of-line (EOL).
     *
     * LineStatement = [ Label ] [ Instruction | Directive ] [ Comment ] EOL .
     */
    public void parseTokens() {
        LineStatement ls = new LineStatement();
        // System.out.println("[Debug] Parsing tokens...");
        while (nextToken.getType() != TokenType.EOF) {
//            System.out.println("[Debug]" + this.nextToken);
            // System.out.println("[Debug] Parsing Token " + nextToken.toString());

            //TODO Check TokenType and create the new objects Instruction, Comment, etc
            /*
             example: if  tokentype == mnemonic, then start creating the Instruction object
                      if it is a comment, make comment object etc
                      Feel free to change this function if it makes it easier for you
             */
            TokenType type = nextToken.getType();
            Position position = nextToken.getPosition();
            String value = nextToken.getValue();

            switch(type) {

                case EOL:               //If token is EOL, LS is finished, add it to IR and start a new one.
                    System.out.println("[Debug] - " + ls);
                    //Error Reporting
                    if (ls.getInstruction()!=null){//if no instruction then we assume its a line with only a comment and ignore it
                        if (keywords.get(ls.getInstruction().getMnemonic().getValue())==null){
                            ErrorMsg e1 = new ErrorMsg("Invalid mnemonic or directive.",nextToken.getPosition());
                            errorReporter.record(e1);
                        } else if (keywords.get(ls.getInstruction().getMnemonic().getValue())!=null){
                            if (!keywords.get(ls.getInstruction().getMnemonic().getValue()).getMode().equals("inherent") && ls.getInstruction().getOperand() == null){
                                ErrorMsg e2 = new ErrorMsg("Instruction requires an operand.",nextToken.getPosition());
                                errorReporter.record(e2);
                            }else if (keywords.get(ls.getInstruction().getMnemonic().getValue()).getMode().equals("inherent") && ls.getInstruction().getOperand() != null){
                                ErrorMsg e3 = new ErrorMsg("Inherent instruction must not have an operand.",nextToken.getPosition());
                                errorReporter.record(e3);
                            }
                        }
                    }
                    ir.add(ls);
                    ls = new LineStatement();
                    break;

                case COMMENT:           // If token is a comment
                    ls.setComment(new Comment(position, value));
                    break;

                case MNEMONIC:          //If token is a mnemonic
                    ls.setInstruction(new Instruction(position, value)); // Set instruction
                    //Set newly created instruction's mnemonic
                    ls.getInstruction().setMnemonic(new Mnemonic(position, value));
                    break;

                case OPERAND:
                    //System.out.println("[Debug] - " + nextToken);
                    ls.getInstruction().getMnemonic().setOpCode(Integer.parseInt(value)); //Set mnemonic's opcode
                    Operand operand = new Operand(position, value);
                    operand.setOperand(Integer.parseInt(value));
                    ls.getInstruction().setOperand(operand); //set instruction's opcode
                    break;

            }

            //Get the next token to process
            getNextToken();
        }
//        System.out.println("[Debug]" + this.nextToken);
        //System.out.println("[Debug] Done parsing tokens...");
        //System.out.println(Arrays.toString(intermediateRep.toArray()));
    }

    public IntermediateRep getIR() {
        return this.ir;
    }

    private void getNextToken() {
        this.nextToken = lexicalScanner.getNextToken();
    }
}
