package lexical;

import utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Lexical {

    private FileInputStream fis = null;

    public Lexical(String fileName) {
        try {
            fis = new FileInputStream(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }

        /*
        TODO: figure out what the hell this means
        (a) Create a symbol table (for labels and mnemonics).
        (b) Enter mnemonics (keywords) in the symbol table.
        */
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
}
