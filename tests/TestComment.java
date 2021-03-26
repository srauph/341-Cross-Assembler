import lexical.token.Comment;
import lexical.token.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestComment {

    Comment c1 = new Comment(new Position(1,2),"test comment");

    @Test
    void Should_Create_Comment_equalTo_test_comment(){
        String result = c1.toString();
        Assertions.assertEquals("Comment{test comment}",result);
    }
}
