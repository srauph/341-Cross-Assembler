import lexical.token.Directive;
import lexical.token.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestDirective {

    Directive d1 = new Directive(new Position(1,2));

    @Test
    void Should_Create_TestDirective_equalTo_Directive(){
        d1.setDirective("CString");
        d1.setStringOperand("0");
        Assertions.assertEquals("Directive{directive='CString', stringOperand='" + 0 + "'}",d1.toString());
    }
}
