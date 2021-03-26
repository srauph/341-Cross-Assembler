import lexical.token.Position;
import lexical.token.Token;
import lexical.token.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestToken {

    Token t1 = new Token(new Position(1,2),"halt", TokenType.MNEMONIC);
    Token t2 = new Token(new Position(2,4),"<i3>", TokenType.OPERAND);
    Token t3 = new Token(new Position(3,6),"add", TokenType.INSTRUCTION);
    Token t4 = new Token(new Position(4,8),"CString", TokenType.DIRECTIVE);
    Token t5 = new Token(new Position(5,10),"; OK, number <u5> [0..31].", TokenType.COMMENT);

    @Test
    void Should_Create_Halt_1_2_EqualTo_Mnemonic() {
        String result = t1.getValue()+"("+t1.getPosition().getLineNumber()+","+t1.getPosition().getColumnNumber()+")="+TokenType.MNEMONIC;
        Assertions.assertEquals("halt(1,2)=MNEMONIC",result);
    }

    @Test
    void Should_Create_TestOperand_2_4_EqualTo_Operand() {
        String result = t2.getValue()+"("+t2.getPosition().getLineNumber()+","+t2.getPosition().getColumnNumber()+")="+TokenType.OPERAND;
        Assertions.assertEquals("<i3>(2,4)=OPERAND",result);
    }

    @Test
    void Should_Create_Add_3_6_EqualTo_Instruction() {
        String result = t3.getValue()+"("+t3.getPosition().getLineNumber()+","+t3.getPosition().getColumnNumber()+")="+TokenType.INSTRUCTION;
        Assertions.assertEquals("add(3,6)=INSTRUCTION",result);
    }

    @Test
    void Should_Create_CString_4_8_EqualTo_Directive() {
        String result = t4.getValue()+"("+t4.getPosition().getLineNumber()+","+t4.getPosition().getColumnNumber()+")="+TokenType.DIRECTIVE;
        Assertions.assertEquals("CString(4,8)=DIRECTIVE",result);
    }

    @Test
    void Should_Create_TestComment_5_10_EqualTo_Comment() {
        String result = t5.getValue() + "(" + t5.getPosition().getLineNumber() + "," + t5.getPosition().getColumnNumber() + ")=" + TokenType.COMMENT;
        Assertions.assertEquals("; OK, number <u5> [0..31].(5,10)=COMMENT", result);
    }
}
