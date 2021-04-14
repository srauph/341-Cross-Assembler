package ir;

import lexical.token.Comment;
import lexical.token.Directive;
import lexical.token.Instruction;
import lexical.token.Label;

public class LineStatement {
    private Label label = null;
    //Either instruction OR directive, cant have both for 1 line
    private Instruction instruction;
    private Directive directive;
    private Comment comment;

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

    @Override
    public String toString() {
        return "LineStatement{" +
                "label=" + label +
                ", instruction=" + instruction +
                ", directive=" + directive +
                ", comment=" + comment +
                '}';
    }
}
