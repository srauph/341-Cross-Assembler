import lexical.token.Position;
import lexical.token.Token;
import lexical.token.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMnemonic {

    Position p1 = new Position(1,2);
    Position p2 = new Position(2,4);
    Position p3 = new Position(3,6);
    Position p4 = new Position(65535,255);

    Token t1 = new Token (new Position(1,2),"halt", TokenType.MNEMONIC);
    Token t2 = new Token(new Position(2,4),"pop", TokenType.MNEMONIC);
    Token t3 = new Token(new Position(3,6),"dup", TokenType.MNEMONIC);

    @Test
    void Should_Return_LineNumber_1() {
        int result = p1.getLineNumber();
        Assertions.assertEquals(1,result);
    }

    @Test
    void Should_Return_ColumnNumber_2() {
        int result = p1.getColumnNumber();
        Assertions.assertEquals(2,result);
    }

    @Test
    void Should_Return_LineNumber_2() {
        int result = p2.getLineNumber();
        Assertions.assertEquals(2,result);
    }

    @Test
    void Should_Return_ColumnNumber_4() {
        int result = p2.getColumnNumber();
        Assertions.assertEquals(4,result);
    }

    @Test
    void Should_Return_LineNumber_3() {
        int result = p3.getLineNumber();
        Assertions.assertEquals(3,result);
    }

    @Test
    void Should_Return_ColumnNumber_6() {
        int result = p3.getColumnNumber();
        Assertions.assertEquals(6,result);
    }

    @Test
    void Should_Return_LineNumber_65535() {
        int result = p4.getLineNumber();
        Assertions.assertEquals(65535,result);
    }

    @Test
    void Should_Return_ColumnNumber_255() {
        int result = p4.getColumnNumber();
        Assertions.assertEquals(255,result);
    }

    @Test
    void Should_Create_Halt_1_2_EqualTo_Mnemonic() {
        String result = t1.getValue()+"("+t1.getPosition().getLineNumber()+","+t1.getPosition().getColumnNumber()+")="+TokenType.MNEMONIC;
        Assertions.assertEquals("halt(1,2)=MNEMONIC",result);
    }

    @Test
    void Should_Create_Pop_2_4_EqualTo_Mnemonic() {
        String result = t2.getValue()+"("+t2.getPosition().getLineNumber()+","+t2.getPosition().getColumnNumber()+")="+TokenType.MNEMONIC;
        Assertions.assertEquals("pop(2,4)=MNEMONIC",result);
    }

    @Test
    void Should_Create_Dup_3_6_EqualTo_Mnemonic() {
        String result = t3.getValue()+"("+t3.getPosition().getLineNumber()+","+t3.getPosition().getColumnNumber()+")="+TokenType.MNEMONIC;
        Assertions.assertEquals("dup(3,6)=MNEMONIC",result);
    }
}