package ir;

import lexical.token.Comment;
import lexical.token.Directive;
import lexical.token.Instruction;
import lexical.token.Label;
import utils.StringUtils;

public class LineStatement {
    private Label label = null;
    //Either instruction OR directive, cant have both for 1 line
    private Instruction instruction;
    private Directive directive;
    private Comment comment;
    private int address;
    private int[] code = new int[0];

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

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int[] getCode() {
        return code;
    }

    public void setCode(int[] code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "LineStatement{" +
                "label=" + label +
                ", instruction=" + instruction +
                ", directive=" + directive +
                ", comment=" + comment +
                ", address=" + address +
                ", code=" + StringUtils.getHexStringFromIntArray(code) +
                '}';
    }
}
