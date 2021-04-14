package lexical.token;

/**
 * An Instruction is composed of an opcode Mnemonic and an optional Operand
 */
public class Instruction extends Token {

    private Mnemonic mnemonic = null;
    private Operand operand = null;

    public Instruction(Position pos, String name) {
        super(pos, name, TokenType.INSTRUCTION);
    }

    public Mnemonic getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(Mnemonic mnemonic) {
        this.mnemonic = mnemonic;
    }

    public Operand getOperand() {
        return operand;
    }

    public void setOperand(Operand operand) {
        this.operand = operand;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "mnemonic=" + mnemonic +
                ", operand=" + operand +
                '}';
    }
}
