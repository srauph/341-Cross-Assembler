package lexical.token;

/**
 * An Instruction is composed of an opcode Mnemonic and an optional Operand
 */
public class Instruction extends Token {


    private Mnemonic mnemonic;
    //  private Operand operand; //TODO: Implement this

    public Instruction(Position pos, String name) {
        super(pos, name, TokenType.INSTRUCTION);
    }

    public Mnemonic getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(Mnemonic mnemonic) {
        this.mnemonic = mnemonic;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "mnemonic=" + mnemonic +
                '}';
    }
}
