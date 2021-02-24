import java.util.LinkedList;

public interface IParser {
    void parseTokens();

    LinkedList<LineStatement> getIR();
}
