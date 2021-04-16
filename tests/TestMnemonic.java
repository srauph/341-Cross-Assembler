import lexical.token.Mnemonic;
import lexical.token.Position;
import lexical.token.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMnemonic {

    Mnemonic m1 = new Mnemonic(new Position(1,2),"halt");
    Mnemonic m2 = new Mnemonic(new Position(2,4),"pop");
    Mnemonic m3 = new Mnemonic(new Position(3,6),"dup");

    @Test
    void Should_Create_Halt_1_2_EqualTo_Mnemonic() {
        String result = m1.getValue()+"("+m1.getPosition().getLineNumber()+","+m1.getPosition().getColumnNumber()+")="+TokenType.MNEMONIC;
        Assertions.assertEquals("halt(1,2)=MNEMONIC",result);
    }

    @Test
    void Should_Create_Pop_2_4_EqualTo_Mnemonic() {
        String result = m2.getValue()+"("+m2.getPosition().getLineNumber()+","+m2.getPosition().getColumnNumber()+")="+TokenType.MNEMONIC;
        Assertions.assertEquals("pop(2,4)=MNEMONIC",result);
    }

    @Test
    void Should_Create_Dup_3_6_EqualTo_Mnemonic() {
        String result = m3.getValue()+"("+m3.getPosition().getLineNumber()+","+m3.getPosition().getColumnNumber()+")="+TokenType.MNEMONIC;
        Assertions.assertEquals("dup(3,6)=MNEMONIC",result);
    }
}