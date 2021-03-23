package lexical.token;

public interface IToken {
    Position getPosition();

    String getValue();

    TokenType getType();
}
