package lexical;

public interface IToken {
    Position getPosition();
    String getValue();
    TokenType getType();
}
