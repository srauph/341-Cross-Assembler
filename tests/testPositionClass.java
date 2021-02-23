import lexical.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testPositionClass {

    Position p1 = new Position(1,2);
    Position p2 = new Position(2,4);
    Position p3 = new Position(3,6);
    Position p4 = new Position(65535,255);
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

}
