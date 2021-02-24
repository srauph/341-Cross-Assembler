package lexical.token;

public class Token implements IToken {

    private Position pos;
    private String name;
    private TokenType type;

    public Token() {
    }

    public Token(Position pos, String name, TokenType type) {
        this.pos = pos;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", value='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
