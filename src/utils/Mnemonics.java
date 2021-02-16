package utils;

import java.util.Objects;

public enum Mnemonics {
    HALT("halt"),
    POP("pop"),
    DUP("dup"),
    EXIT("exit"),
    RET("ret"),
    NOT("not"),
    AND("and"),
    OR("or"),
    XOR("xor"),
    NEG("neg"),
    INC("inc"),
    DEC("dec"),
    ADD("add"),
    SYB("sub"),
    MUL("mul"),
    DEV("div"),
    REM("rem"),
    SHL("shl"),
    SHR("shr"),
    TEQ("teq"),
    TNE("tne"),
    TLT("tlt"),
    TGT("tgt"),
    TLE("tle"),
    TGE("tge"),

    ;

    private final String mnemonic;

    Mnemonics(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public static boolean isMnemonic(String mnemonic) {
        for (Mnemonics nm : Mnemonics.values()) {
            if (Objects.equals(nm.mnemonic, mnemonic)) {
                return true;
            }
        }
        return false;
    }
}
