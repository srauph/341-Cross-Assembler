package lexical.token;

public class Mnemonic extends Token {
    private int opCode; // value of opcode
    private int size; // size of opcode

    public Mnemonic() {
    }

    public Mnemonic(int opCode, int size, Position pos, String value) {
        super(pos, value, TokenType.MNEMONIC);
        this.opCode = opCode;
        this.size = size;
    }

    public Mnemonic(Position pos, String value) {
        super(pos, value, TokenType.MNEMONIC);
    }

    public Mnemonic(String value, int opCode) {
        super(null, value, TokenType.MNEMONIC);
        this.opCode = opCode;
    }

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Mnemonic{" +
                "opCode=" + opCode +
                ", name=" + getValue() +
                '}';
    }
}
