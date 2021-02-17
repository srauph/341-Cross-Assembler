public class LineStatement {
    //Label
    private String mnemonic;
    //Operand
    //Comment

    public LineStatement() {
    }

    public LineStatement(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    @Override
    public String toString() {
        return "LineStatement{" +
                "mnemonic='" + mnemonic + '\'' +
                '}';
    }
}
