package lexical.token;

public enum TokenType {
    LABEL("Label"),
    MNEMONIC("Mnemonic"),
    INSTRUCTION("Instruction"),
    OPERAND("Operand"),
    DIRECTIVE("Directive"),
    STRING_OPERAND("StringOperand"),
    COMMENT("Comment"),
    EOL("EOL"),
    EOF("EOF"),
    UNKNOWN("UNKNOWN"),
    ;
    private final String type;

    TokenType(String type) {
        this.type = type;
    }
}