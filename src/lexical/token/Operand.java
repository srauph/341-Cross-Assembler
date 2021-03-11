package lexical.token;

public class Operand extends Token {
    private int operand;

    public Operand(Position pos, String name) {
        super(pos, name, TokenType.OPERAND);
    }

    public int getOperand() {
        return operand;
    }

    public void setOperand(int operand) {
        this.operand = operand;
    }

    @Override
    public String toString() {
        return "Operand{" +
                "operand=" + operand +
                '}';
    }
}
