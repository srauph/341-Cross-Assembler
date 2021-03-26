import lexical.token.Instruction;
import lexical.token.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestInstruction {

    Instruction i1 = new Instruction(new Position(1,2), "testInstruction");

    @Test
    void should_return_testInstruction(){
        String result = i1.getValue();
        Assertions.assertEquals("testInstruction",result);
    }
}
