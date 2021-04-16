import lexical.token.Operand;
import lexical.token.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestOperand {

    Operand o1 = new Operand(new Position(1, 2), "<i3>");

    @Test
    void Should_Create_TestOperand_equalTo_Operand() {
        o1.setOperand(0);
        Assertions.assertEquals("Operand{operand=0, label='null'}", o1.toString());
    }
}
