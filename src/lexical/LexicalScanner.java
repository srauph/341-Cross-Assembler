package lexical;

import errorhandling.ErrorMsg;
import errorhandling.ErrorReporter;
import errorhandling.IErrorReporter;
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

public class LexicalScanner implements ILexicalScanner {

    private int lineNumber = 1;
    private int columnNumber = 0;

    private FileInputStream fis = null;
    private SymbolTable<String, Mnemonic> keywords;
    private IErrorReporter errorReporter;

    public LexicalScanner() {
        keywords = new SymbolTable<>();
        initKeywordTable();
    }

    public LexicalScanner(String inputFile) {
        try {
            File file = new File(inputFile);
            file.setWritable(true);
            fis = new FileInputStream(file);
            keywords = new SymbolTable<>();
            initKeywordTable();
            errorReporter = new ErrorReporter(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Initializes the keyword table to allow verification of valid mnemonics and directions (in the future).
     */
    public void initKeywordTable() {
        keywords.put("halt", new Mnemonic("halt", 0x00, "inherent"));
        keywords.put("pop", new Mnemonic("pop", 0x01, "inherent"));
        keywords.put("dup", new Mnemonic("dup", 0x02, "inherent"));
        keywords.put("exit", new Mnemonic("exit", 0x03, "inherent"));
        keywords.put("ret", new Mnemonic("ret", 0x04, "inherent"));
        keywords.put("not", new Mnemonic("not", 0x0C, "inherent"));
        keywords.put("and", new Mnemonic("and", 0x0D, "inherent"));
        keywords.put("or", new Mnemonic("or", 0x0E, "inherent"));
        keywords.put("xor", new Mnemonic("xor", 0x0F, "inherent"));
        keywords.put("neg", new Mnemonic("neg", 0x10, "inherent"));
        keywords.put("inc", new Mnemonic("inc", 0x11, "inherent"));
        keywords.put("dec", new Mnemonic("dec", 0x12, "inherent"));
        keywords.put("add", new Mnemonic("add", 0x13, "inherent"));
        keywords.put("sub", new Mnemonic("sub", 0x14, "inherent"));
        keywords.put("mul", new Mnemonic("mul", 0x15, "inherent"));
        keywords.put("div", new Mnemonic("div", 0x16, "inherent"));
        keywords.put("rem", new Mnemonic("rem", 0x17, "inherent"));
        keywords.put("shl", new Mnemonic("shl", 0x18, "inherent"));
        keywords.put("shr", new Mnemonic("shr", 0x19, "inherent"));
        keywords.put("teq", new Mnemonic("teq", 0x1A, "inherent"));
        keywords.put("tne", new Mnemonic("tne", 0x1B, "inherent"));
        keywords.put("tlt", new Mnemonic("tlt", 0x1C, "inherent"));
        keywords.put("tgt", new Mnemonic("tgt", 0x1D, "inherent"));
        keywords.put("tle", new Mnemonic("tle", 0x1E, "inherent"));
        keywords.put("tge", new Mnemonic("tge", 0x1F, "inherent"));

        keywords.put(".cstring", new Mnemonic(".cstring", "directive"));

        keywords.put("enter.u5", new Mnemonic("enter.u5", 0x70, "immediate"));
        keywords.put("ldc.i3", new Mnemonic("ldc.i3", 0x90, "immediate"));
        keywords.put("addv.u3", new Mnemonic("addv.u3", 0x98, "immediate"));
        keywords.put("ldv.u3", new Mnemonic("ldv.u3", 0xA0, "immediate"));
        keywords.put("stv.u3", new Mnemonic("stv.u3", 0xA8, "immediate"));

        keywords.put("br.i8", new Mnemonic("br.i8", 0xE0, "relative"));
        keywords.put("brf.i8", new Mnemonic("brf.i8", 0xE3, "relative"));
        keywords.put("ldc.i8", new Mnemonic("ldc.i8", 0xD9, "relative"));
        keywords.put("ldv.u8", new Mnemonic("ldv.u8", 0xB1, "relative"));
        keywords.put("stv.u8", new Mnemonic("stv.u8", 0xB2, "relative"));
        keywords.put("lda.i16", new Mnemonic("lda.i16", 0xD5, "relative"));
    }

    /**
     * Returns the next token. Supports Mnemonic of inherent/immediate types,
     * instruction, comment, EOL and EOF.
     *
     * @return the next token
     */
    public Token getNextToken() {
        StringBuilder sb = new StringBuilder();
        String alphabet = "[a-zA-Z]*$";
        String numbers = "[0-9]*$";
        int c = readChar();

        //skip ignored characters until we reach a valid character
        while (StringUtils.isIgnoredCharacter(c)) {
            c = readChar();
        }
        //label
        if (Character.isUpperCase(c)) {
            return readLabel(c, sb);
        }

        //Directive
        if (StringUtils.isPeriod((char) c)) {
            return readDirective(c, sb);
        }

        //Mnemonic inherent/immediate
        if (String.valueOf((char) c).matches(alphabet)) {
            return readAddressing(c, sb);
        }

        //instruction
        if (StringUtils.isMinusSign(c) || String.valueOf((char) c).matches(numbers)) {
            return readOperand(c, sb);
        }

        //String for directive
        if (StringUtils.isQuote((char) c)) {
            return readStringOperand(c, sb);
        }

        //comment
        if (StringUtils.isSemicolon(c)) {
            return readComment(c, sb);
        }

        //Check if next valid character is an EOL
        if (StringUtils.isEOL(c)) {
            Position pos = new Position(lineNumber, ++columnNumber);
            this.columnNumber = 0;
            ++lineNumber;//Increment after
            return new Token(pos, "EOL", TokenType.EOL);
        }

        //Check EOF
        if (StringUtils.isEOF(c)) {
            errorReporter.checkReports();
            //Check if any errors for scanner exist and if so, print and terminate?
            return new Token(new Position(lineNumber, 0), "EOF", TokenType.EOF);
        }

        return readUnknown(c, sb);
    }

    /**
     * Reads string for directive.
     *
     * @param c
     * @return unknown token
     */
    private Token readStringOperand(int c, StringBuilder sb) {
        Position pos = new Position(lineNumber, ++columnNumber);
        while (!StringUtils.isSpace(c)) {
            validateCharacter(c, pos);
            //continue reading each character
            sb.append((char) c);
            c = readChar();
        }
        return new Token(pos, sb.toString(), TokenType.STRING_OPERAND);
    }

    /**
     * Reads labels and returns the token.
     *
     * @param c
     * @param sb
     * @return
     */
    private Token readLabel(int c, StringBuilder sb) {
        Position pos = new Position(lineNumber, ++columnNumber);
        while (!StringUtils.isIgnoredCharacter(c) && !StringUtils.isEOF(c)) {
            validateCharacter(c, pos);
            //continue reading each character
            sb.append((char) c);
            c = readChar();
        }
        return new Token(pos, sb.toString(), TokenType.LABEL);
    }


    /**
     * Reads directive.
     *
     * @param c
     * @return unknown token
     */
    private Token readDirective(int c, StringBuilder sb) {
        Position pos = new Position(lineNumber, ++columnNumber);
        while (!StringUtils.isSpace(c)) {
            validateCharacter(c, pos);
            //continue reading each character
            sb.append((char) c);
            c = readChar();
        }
        return new Token(pos, sb.toString(), TokenType.DIRECTIVE);
    }

    /**
     * Reads the .asm file for the next unknown token.
     *
     * @param c
     * @return unknown token
     */
    private Token readUnknown(int c, StringBuilder sb) {
        while (!StringUtils.isSpace(c)) {
            //continue reading each character
            sb.append((char) c);
            c = readChar();
        }
        return new Token(new Position(lineNumber, ++columnNumber), sb.toString(), TokenType.UNKNOWN);
    }

    /**
     * Returns the next available character.
     *
     * @return next character
     */
    private char readChar() {
        try {
            return (char) fis.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return '\0';
    }

    /**
     * Reads the .asm file for the next available mnemonic. It can be either immediate/inherent addressing.
     *
     * @param c
     * @return mnemonic token
     */
    private Token readAddressing(int c, StringBuilder sb) {
        Position pos = new Position(lineNumber, ++columnNumber);
        while (!StringUtils.isIgnoredCharacter(c) && !StringUtils.isEOF(c)) {
            validateCharacter(c, pos);

            //continue reading each character
            sb.append((char) c);
            c = readChar();
        }
        return new Token(pos, sb.toString(), TokenType.MNEMONIC);
    }

    /**
     * Returns the token of the operand (?). Supports negative values.
     * I am under the impression the operand is the number after the mnemonic and before the comment.
     *
     * @param c
     * @param sb
     * @return instruction token
     */
    private Token readOperand(int c, StringBuilder sb) {
        Position pos = new Position(lineNumber, ++columnNumber);
        while (!StringUtils.isIgnoredCharacter(c) && !StringUtils.isEOF(c)) {
            validateCharacter(c, pos);
            //continue reading each character
            sb.append((char) c);
            c = readChar();
        }
        return new Token(pos, sb.toString(), TokenType.OPERAND);
    }

    /**
     * Returns the comment token.
     *
     * @param c
     * @param sb
     * @return comment token
     */
    private Token readComment(int c, StringBuilder sb) {
        while (!StringUtils.isEOL(c) && c != '\r' && !StringUtils.isEOF(c)) {
            //continue reading each character
            sb.append((char) c);
            c = readChar();
        }
        return new Token(new Position(lineNumber, ++columnNumber), sb.toString(), TokenType.COMMENT);
    }

    /**
     * Validates characters during scanner process if it is valid.
     * Should it not, an error message will be stored.
     *
     * @param c
     */
    public void validateCharacter(int c, Position pos) {
        ErrorMsg errorMsg = new ErrorMsg();

        if (StringUtils.isEOF(c)) {
            errorMsg.setMessage("eof in string");
        }

        if (StringUtils.isEOL(c)) {
            errorMsg.setMessage("eol in string");
        }

        if (!errorMsg.getMessage().isEmpty()) { // if an error is found report it
            errorMsg.setPosition(pos);
            this.errorReporter.record(errorMsg);
        }
    }

    public SymbolTable<String, Mnemonic> getKeywords() {
        return keywords;
    }
}
