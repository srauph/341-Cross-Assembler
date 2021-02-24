package lexical.token;

public interface IToken {
    Position getPosition();
    String getName();
    TokenType getType();
}
