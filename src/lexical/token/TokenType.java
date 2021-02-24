package lexical.token;

public enum TokenType {
    LABEL("Label"),
    MNEMONIC("Mnemonic"),
    INSTRUCTION("Instruction"),
    OPERAND("Operand"),
    DIRECTIVE("Directive"),
    COMMENT("Comment"),
    EOL("EOL"),
    EOF("EOF"),
    ;
    private final String type;

    TokenType(String type) {
        this.type = type;
    }
}