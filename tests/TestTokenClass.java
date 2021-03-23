import lexical.token.Position;
import lexical.token.Token;
import lexical.token.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTokenClass {

    Token t1 = new Token (new Position(1,2),"halt", TokenType.MNEMONIC);
    Token t2 = new Token(new Position(2,4),"pop", TokenType.MNEMONIC);
    Token t3 = new Token(new Position(3,6),"dup", TokenType.MNEMONIC);
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
