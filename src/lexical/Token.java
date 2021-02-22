package lexical;

public class Token {

    private Position pos;
    private String value;
    private TokenType type;

    public Token() {
    }

    public Token(Position pos, String value, TokenType type) {
        this.pos = pos;
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public Position getPosition() {
        return pos;
    }

    public void setPosition(Position pos) { this.pos = pos; }

    @Override
    public String toString() {
        return "Token{" +
                "pos=" + pos +
                ", value='" + value + '\'' +
                ", type=" + type +
                '}';
    }
}
