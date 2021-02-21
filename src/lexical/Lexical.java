package lexical;

import utils.StringUtils;
import utils.SymbolTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Lexical {

    private FileInputStream fis = null;
    private SymbolTable<String, Integer> keywords;

    public Lexical(String inputFile) {
        try {
            fis = new FileInputStream(new File(inputFile));
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
        boolean isLastCharALetter = false;
        Token tok = new Token();
        try {
            int c;
            while ((c = fis.read()) != -1) {
                if ((char) c == '\n') { // end of line
                    return new Token("EOL", TokenType.EOL);
                }
                if (StringUtils.isWhiteSpace((char) c) && isLastCharALetter) {
                    break;
                } else {
                    isLastCharALetter = false;
                }
                if (String.valueOf((char) c).matches(alphabet)) {
                    sb.append((char) c);
                    isLastCharALetter = true;
                    tok.setType(TokenType.MNEMONIC);
                }
            }
            if (sb.toString().equals("")) {
                return new Token("EOF", TokenType.EOF);
            } else {
                tok.setValue(sb.toString());
                return tok;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Token("EOF", TokenType.EOF);
    }

    public SymbolTable<String, Integer> getKeywords() {
        return keywords;
    }
}
