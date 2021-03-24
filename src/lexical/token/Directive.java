package lexical.token;

public class Directive extends Token {
    private String directive;
    private String stringOperand;

    public Directive(Position pos, String name) {
        super(pos, name, TokenType.DIRECTIVE);
    }

    public String getDirective() {
        return directive;
    }

    public void setDirective(String directive) {
        this.directive = directive;
    }

    public String getStringOperand() {
        return stringOperand;
    }

    public void setStringOperand(String stringOperand) {
        this.stringOperand = stringOperand;
    }

    @Override
    public String toString() {
        return "Directive{" +
                "directive='" + directive + '\'' +
                ", stringOperand='" + stringOperand + '\'' +
                '}';
    }
}
