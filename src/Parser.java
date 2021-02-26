import ir.IntermediateRep;
import ir.LineStatement;
import lexical.LexicalScanner;
import lexical.token.Mnemonic;
import lexical.token.Token;
import lexical.token.TokenType;
import utils.SymbolTable;

public class Parser implements IParser {
    //Sequence of line statements
    private final IntermediateRep ir = new IntermediateRep();
    // private final LinkedList<ir.LineStatement> intermediateRep = new LinkedList<>();
    private final LexicalScanner lexicalScanner;
    private final SymbolTable<String, Token> keywords;
    private Token nextToken;

    public Parser(LexicalScanner lexicalScanner, SymbolTable<String, Token> keywords) {
        this.lexicalScanner = lexicalScanner;
        this.keywords = keywords;
        this.nextToken = lexicalScanner.getNextToken();
    }

    /**
     * Generates line statements based upon tokens from the lexical analyzer.
     * Currently supports only Mnemomics.
     */
    public void parseTokens() {
        LineStatement ls = new LineStatement();
        // System.out.println("[Debug] Parsing tokens...");
        while (nextToken.getType() != TokenType.EOF) {
//            System.out.println("[Debug]" + this.nextToken);
            // System.out.println("[Debug] Parsing Token " + nextToken.toString());
            /*
              Once the token is an EOL, the line statement is finished
              add it to the linkedlist and create a new line statement object for the next tokens
              to be read
             */
            if (nextToken.getType() == TokenType.EOL) {
                ir.add(ls);
                ls = new LineStatement();
            }
            // checks if the token is a valid mnemonic keyword
            else if (keywords.containsKey(nextToken.getName())) {
                ls.setMnemonic((Mnemonic) nextToken);
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
