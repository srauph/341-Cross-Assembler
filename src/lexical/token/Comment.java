package lexical.token;

public class Comment extends Token {
    private final String comment;

    public Comment(Position pos, String comment) {
        super(pos, "", TokenType.COMMENT);
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Comment{" + this.getComment() + "}";
    }
}
