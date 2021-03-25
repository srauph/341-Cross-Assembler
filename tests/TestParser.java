import errorhandling.ErrorReporter;
import lexical.LexicalScanner;
import lexical.token.Position;
import lexical.token.Token;
import lexical.token.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.Parser;

public class TestParser {
    LexicalScanner scanner = new LexicalScanner();
    ErrorReporter errorReporter = new ErrorReporter("TestParser");

    @Test
    void Should_Pass_If_Output_Is_Same_As_Expected() {

        Token[] tokenArray = {t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24};
        Parser parse = new Parser(scanner, scanner.getKeywords(), errorReporter, true);
        for (Token tok : tokenArray) {
            if (tok != null) {
                parse.setNextToken(tok);
                parse.parseTokens();
            }
        }
        String expectedOutPut = "IntermediateRep{intermediateRep=[LineStatement{instruction=null, comment=Comment{; TestImmediate.asm - Test immediate instructions.}, directive=null}, LineStatement{instruction=null, comment=null, directive=null}, LineStatement{instruction=Instruction{mnemonic=Mnemonic{opCode=128, name=enter.u5}, operand=Operand{operand=0}}, comment=Comment{; OK, number <u5> [0..31].}, directive=null}, LineStatement{instruction=Instruction{mnemonic=Mnemonic{opCode=145, name=ldc.i3}, operand=Operand{operand=1}}, comment=Comment{; OK, number <i3> [-4..3].}, directive=null}, LineStatement{instruction=Instruction{mnemonic=Mnemonic{opCode=158, name=addv.u3}, operand=Operand{operand=6}}, comment=Comment{; OK, number <u3> [0..7].}, directive=null}, LineStatement{instruction=Instruction{mnemonic=Mnemonic{opCode=163, name=ldv.u3}, operand=Operand{operand=3}}, comment=Comment{; OK, number <u3> [0..7].}, directive=null}, LineStatement{instruction=Instruction{mnemonic=Mnemonic{opCode=173, name=stv.u3}, operand=Operand{operand=5}}, comment=Comment{; OK, number <u3> [0..7].}, directive=null}, LineStatement{instruction=null, comment=null, directive=null}]}";
        Assertions.assertEquals(parse.getIR().toString(), expectedOutPut);
    }

    Token t1 = new Token(new Position(1, 1), "; TestImmediate.asm - Test immediate instructions.", TokenType.COMMENT);
    Token t2 = new Token(new Position(1, 2), "EOL", TokenType.EOL);
    Token t3 = new Token(new Position(2, 1), "EOL", TokenType.EOL);

    Token t4 = new Token(new Position(3, 1), "enter.u5", TokenType.MNEMONIC);
    Token t5 = new Token(new Position(3, 2), "0", TokenType.OPERAND);
    Token t6 = new Token(new Position(3, 3), "; OK, number <u5> [0..31].", TokenType.COMMENT);
    Token t7 = new Token(new Position(3, 4), "EOL", TokenType.EOL);


    Token t8 = new Token(new Position(4, 1), "ldc.i3", TokenType.MNEMONIC);
    Token t9 = new Token(new Position(4, 2), "1", TokenType.OPERAND);
    Token t10 = new Token(new Position(4, 3), "; OK, number <i3> [-4..3].", TokenType.COMMENT);
    Token t11 = new Token(new Position(4, 4), "EOL", TokenType.EOL);

    Token t12 = new Token(new Position(5, 1), "addv.u3", TokenType.MNEMONIC);
    Token t13 = new Token(new Position(5, 2), "6", TokenType.OPERAND);
    Token t14 = new Token(new Position(5, 3), "; OK, number <u3> [0..7].", TokenType.COMMENT);
    Token t15 = new Token(new Position(5, 4), "EOL", TokenType.EOL);


    Token t16 = new Token(new Position(6, 1), "ldv.u3", TokenType.MNEMONIC);
    Token t17 = new Token(new Position(6, 2), "3", TokenType.OPERAND);
    Token t18 = new Token(new Position(6, 3), "; OK, number <u3> [0..7].", TokenType.COMMENT);
    Token t19 = new Token(new Position(6, 4), "EOL", TokenType.EOL);

    Token t20 = new Token(new Position(7, 1), "stv.u3", TokenType.MNEMONIC);
    Token t21 = new Token(new Position(7, 2), "5", TokenType.OPERAND);
    Token t22 = new Token(new Position(7, 3), "; OK, number <u3> [0..7].", TokenType.COMMENT);
    Token t23 = new Token(new Position(7, 4), "EOL", TokenType.EOL);
    Token t24 = new Token(new Position(8, 1), "EOF", TokenType.EOF);

}

