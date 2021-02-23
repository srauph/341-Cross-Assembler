package lexical;

import utils.StringUtils;
import utils.SymbolTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Lexical {

    private int lineNumber = 1;
    private int columnNumber = 0;
    private int currentColumn = 0;

    private FileInputStream fis = null;
    private SymbolTable<String, Integer> keywords;

    public Lexical(String inputFile) {
        try {
            fis = new FileInputStream(inputFile);
            keywords = new SymbolTable<>();
            initKeywordTable();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Initializes the keyword table to allow verification of valid mnemonics and directions (in the future).
     */
    private void initKeywordTable() {
        keywords.put("halt", 0x00);
        keywords.put("pop", 0x01);
        keywords.put("dup", 0x02);
        keywords.put("exit", 0x03);
        keywords.put("ret", 0x04);
        keywords.put("not", 0x0C);
        keywords.put("and", 0x0D);
        keywords.put("or", 0x0E);
        keywords.put("xor", 0x0F);
        keywords.put("neg", 0x10);
        keywords.put("inc", 0x11);
        keywords.put("dec", 0x12);
        keywords.put("add", 0x13);
        keywords.put("sub", 0x14);
        keywords.put("mul", 0x15);
        keywords.put("div", 0x16);
        keywords.put("rem", 0x17);
        keywords.put("shl", 0x18);
        keywords.put("shr", 0x19);
        keywords.put("teq", 0x1A);
        keywords.put("tne", 0x1B);
        keywords.put("tlt", 0x1C);
        keywords.put("tgt", 0x1D);
        keywords.put("tle", 0x1E);
        keywords.put("tge", 0x1F);
        //Will add the rest when we get to there
    }

    /**
     * Returns the next token. Currently only supports reading mnemonics, EOL, EOF.
     * but will eventually support the rest.
     * <p>
     * For reference halt, and, nop, or, etc are utils.Mnemonics.
     *
     * @return the next token
     */
    public Token getNextToken() {
        String alphabet = "[a-zA-Z]*$";
        StringBuilder sb = new StringBuilder();
        Token tok = new Token();
        try {
            int c;
            while ((c = fis.read()) != -1) {
                currentColumn++;
                if (StringUtils.isSpace(c)) { // space
                    columnNumber++;
                    continue;
                }
                if (String.valueOf((char) c).matches(alphabet)) { // mnemonic
                    sb.append((char) c);
                    tok.setType(TokenType.MNEMONIC);
                }
                if (StringUtils.isEOL(c)) {
                    int EOLColumnNumber = currentColumn-1;
                    columnNumber = 0;
                    currentColumn = 0;
                    lineNumber++;
                    return new Token(new Position(lineNumber-1, EOLColumnNumber), "EOL", TokenType.EOL);
                }
                if (keywords.containsKey(sb.toString())) {
                    break;
                }
            }
            if (sb.toString().equals("")) {
                return new Token(new Position(lineNumber, currentColumn), "EOF", TokenType.EOF);
            } else {
                tok.setPosition(new Position(lineNumber, columnNumber));
                tok.setValue(sb.toString());
                columnNumber = currentColumn;
                return tok;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Token(new Position(lineNumber, columnNumber+1), "EOF", TokenType.EOF);
    }

    public SymbolTable<String, Integer> getKeywords() {
        return keywords;
    }
}
