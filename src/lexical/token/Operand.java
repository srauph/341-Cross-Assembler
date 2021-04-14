package lexical.token;

public class Operand extends Token {
    private int operand;
    private Label label;

    public Operand(Position pos, String name) {
        super(pos, name, TokenType.OPERAND);
    }

    public int getOperand() {
        return operand;
    }

    public void setOperand(int operand) {
        this.operand = operand;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Operand{" +
                "operand=" + operand +
                ", label='" + label + '\'' +
                '}';
    }
}
