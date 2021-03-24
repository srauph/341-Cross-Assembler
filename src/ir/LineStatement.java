package ir;

import lexical.token.Comment;
import lexical.token.Directive;
import lexical.token.Instruction;

public class LineStatement {
    //Label
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

    @Override
    public String toString() {
        return "LineStatement{" +
                "instruction=" + instruction +
                ", comment=" + comment +
                ", directive=" + directive +
                '}';
    }
}
