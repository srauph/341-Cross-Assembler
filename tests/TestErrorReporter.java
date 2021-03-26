import errorhandling.ErrorReporter;
import lexical.LexicalScanner;
import lexical.token.Position;
import lexical.token.Token;
import lexical.token.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.Parser;

public class TestErrorReporter {
    LexicalScanner scanner = new LexicalScanner();
    ErrorReporter errorReporter = new ErrorReporter("TestErrorReporter");

    @Test
    void Should_Return_True_If_No_Errors_Detected() {

        Token[] tokenArray = {t1, t2, t3, t4, t5, t6, t7, t8, t9, t10};
        Parser parse = new Parser(scanner, scanner.getKeywords(), errorReporter, true);
        for (Token tok : tokenArray) {
            if (tok != null) {
                parse.setNextToken(tok);
                parse.parseTokens();
            }
        }
        Assertions.assertTrue(errorReporter.hasErrors());
    }

    Token t1 = new Token(new Position(1, 1), "ldc.i3", TokenType.MNEMONIC);
    Token t2 = new Token(new Position(1, 2), " An immediate instruction always requires an operand.", TokenType.COMMENT);
    Token t3 = new Token(new Position(1, 3), "EOL", TokenType.EOL);

    Token t4 = new Token(new Position(2, 1), "pop", TokenType.MNEMONIC);
    Token t5 = new Token(new Position(2, 2), "2", TokenType.OPERAND);
    Token t6 = new Token(new Position(2, 3), "; An inherent instruction has no operand.", TokenType.COMMENT);
    Token t7 = new Token(new Position(2, 4), "EOL", TokenType.EOL);

    Token t8 = new Token(new Position(3, 1), "top", TokenType.MNEMONIC);
    Token t9 = new Token(new Position(3, 2), "; Not a valid mnemonic or directive.", TokenType.COMMENT);
    Token t10 = new Token(new Position(3, 3), "EOF", TokenType.EOF);
}

