package lexical.token;

public class Mnemonic extends Token {
    private int opCode; // value of opcode
    private int size; // size of opcode
    private String mode; //inherent or immediate;

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

    public Mnemonic(String value, int opCode, String mode) {
        super(null, value, TokenType.MNEMONIC);
        this.opCode = opCode;
        this.mode = mode;
    }

    public Mnemonic(String value, String mode) {
        super(null, value, TokenType.MNEMONIC);
        this.mode = mode;
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "Mnemonic{" +
                "opCode=" + opCode +
                ", name=" + getValue() +
                '}';
    }
}
