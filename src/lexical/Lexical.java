package lexical;

import lexical.token.Mnemonic;
import lexical.token.Position;
import lexical.token.Token;
import lexical.token.TokenType;
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
    private SymbolTable<String, Token> keywords;

    public Lexical(String inputFile) {
        try {
            File file = new File(inputFile);
            file.setWritable(false);
            fis = new FileInputStream(file);
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
        keywords.put("halt", new Mnemonic("halt", 0x00));
        keywords.put("pop", new Mnemonic("pop", 0x01));
        keywords.put("dup", new Mnemonic("dup", 0x02));
        keywords.put("exit", new Mnemonic("exit", 0x03));
        keywords.put("ret", new Mnemonic("ret", 0x04));
        keywords.put("not", new Mnemonic("not", 0x0C));
        keywords.put("and", new Mnemonic("and", 0x0D));
        keywords.put("or", new Mnemonic("or", 0x0E));
        keywords.put("xor", new Mnemonic("xor", 0x0F));
        keywords.put("neg", new Mnemonic("neg", 0x10));
        keywords.put("inc", new Mnemonic("inc", 0x11));
        keywords.put("dec", new Mnemonic("dec", 0x12));
        keywords.put("add", new Mnemonic("add", 0x13));
        keywords.put("sub", new Mnemonic("sub", 0x14));
        keywords.put("mul", new Mnemonic("mul", 0x15));
        keywords.put("div", new Mnemonic("div", 0x16));
        keywords.put("rem", new Mnemonic("rem", 0x17));
        keywords.put("shl", new Mnemonic("shl", 0x18));
        keywords.put("shr", new Mnemonic("shr", 0x19));
        keywords.put("teq", new Mnemonic("teq", 0x1A));
        keywords.put("tne", new Mnemonic("tne", 0x1B));
        keywords.put("tlt", new Mnemonic("tlt", 0x1C));
        keywords.put("tgt", new Mnemonic("tgt", 0x1D));
        keywords.put("tle", new Mnemonic("tle", 0x1E));
        keywords.put("tge", new Mnemonic("tge", 0x1F));
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
        Mnemonic mne = null;
        try {
            int c;
            //Read each character
            while ((c = fis.read()) != -1) {
                currentColumn++;
                //If the character is a space, continue to next character
                if (StringUtils.isSpace(c)) { // space
                    columnNumber++;
                    continue;
                }
                //If the character is a letter, we can assume it is a mnemonic (for now)
                if (String.valueOf((char) c).matches(alphabet)) { // mnemonic
                    //Append the character to start building the string
                    sb.append((char) c);
                    //If we're just starting to create the mnemonic, create the object
                    if (mne == null) {
                        mne = new Mnemonic();
                    }
                }
                // If the current character is an EOL character, return an EOL token
                if (StringUtils.isEOL(c)) {
                    int EOLColumnNumber = currentColumn - 1;
                    columnNumber = 0;
                    currentColumn = 0;
                    lineNumber++;
                    return new Token(new Position(lineNumber - 1, EOLColumnNumber), "EOL", TokenType.EOL);
                }
                //Verify the string being built if it os a valid mne and break if so
                //This functionality will change when having to support other types of tokens
                if (keywords.containsKey(sb.toString())) {
                    break;
                }
            }
            //Outside the loop, if the string is empty, return an EOF, the file is done being read
            if (sb.toString().equals("")) {
                fis.close();
                return new Token(new Position(lineNumber, currentColumn), "EOF", TokenType.EOF);
            } else {
                //Broken out of the while loop from finding a valid mnemonic
                if (mne != null) {
                    //Set its position, name and return it.
                    mne.setPosition(new Position(lineNumber, columnNumber));
                    mne.setName(sb.toString());
                    columnNumber = currentColumn;
                    return mne;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Token(new Position(lineNumber, columnNumber + 1), "EOF", TokenType.EOF);
    }

    public SymbolTable<String, Token> getKeywords() {
        return keywords;
    }
}
