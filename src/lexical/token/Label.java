package lexical.token;

public class Label extends Token {
    private String label;

    public Label(Position pos, String label) {
        super(pos, "", TokenType.LABEL);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Label{" +
                "label='" + label + '\'' +
                '}';
    }
}