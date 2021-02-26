package ir;

import lexical.token.Mnemonic;

public class LineStatement {
    //Label
    private Mnemonic mnemonic;
    //Operand
    //Comment

    public LineStatement() {
    }

    public LineStatement(Mnemonic mnemonic) {
        this.mnemonic = mnemonic;
    }

    public Mnemonic getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(Mnemonic mnemonic) {
        this.mnemonic = mnemonic;
    }

    @Override
    public String toString() {
        return "ir.LineStatement{" +
                "mnemonic='" + mnemonic + '\'' +
                '}';
    }
}
