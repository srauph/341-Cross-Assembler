package ir;

import lexical.token.Comment;
import lexical.token.Instruction;

public class LineStatement {
    //Label
    private Instruction instruction;
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

    @Override
    public String toString() {
        return "LineStatement{" +
                "instruction=" + instruction +
                ", comment=" + comment +
                '}';
    }
}
