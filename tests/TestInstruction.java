import lexical.token.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestInstruction {



    Token t1 = new Token (new Position(1,2),"halt", TokenType.MNEMONIC);
    Instruction i1 = new Instruction(new Position(1,2), "testInstruction");

    @Test
    void should_return_testInstruction(){
        String result = i1.getValue();
        Assertions.assertEquals("testInstruction",result);
    }


}
