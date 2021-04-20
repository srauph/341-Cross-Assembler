package ir;

import lexical.token.Comment;
import lexical.token.Directive;
import lexical.token.Instruction;
import lexical.token.Label;

import java.util.Arrays;

public class LineStatement {
    private Label label = null;
    //Either instruction OR directive, cant have both for 1 line
    private Instruction instruction;
    private Directive directive;
    private Comment comment;
    private int[] code = new int[0];
    private int address;
    private int lineNumber;
    private boolean resolved = false;

    public LineStatement() {
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Directive getDirective() {
        return directive;
    }

    public void setDirective(Directive directive) {
        this.directive = directive;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public int getAddressSize() {
        if (getInstruction() == null && getDirective() == null) {
            return 0;
        }
        int size = 0;
        // change mode
        String mode = directive != null ? "directive" : instruction.getMnemonic().getMode();
        switch (mode) {
            case "immediate":
            case "inherent":
                size = 1;
                break;
            case "relative":
                String bitsS = getInstruction().getMnemonic().getValue().split("\\.")[1].replaceAll("[iu]", "");
                int bits = Integer.parseInt(bitsS);
                size = (bits > 8 ? 2 : 1) + 1;
                break;
            case "directive":
                int operandLength = getDirective().getStringOperand().length();// 8 bit each = 1 byte
                // mne = 1 byte + operandLength
                size = 1 + (operandLength);
                break;
        }
        return size;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getAddress() {
        return address;
    }

    public int[] getCode() {
        return code;
    }

    public void setCode(int[] code) {
        this.code = code;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLinePosition(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    @Override
    public String toString() {
        return "LineStatement{" +
                "label=" + label +
                ", instruction=" + instruction +
                ", directive=" + directive +
                ", comment=" + comment +
                ", code=" + Arrays.toString(code) +
                '}';
    }
}
