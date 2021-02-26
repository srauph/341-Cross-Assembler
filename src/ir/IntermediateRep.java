package ir;

import java.util.Collection;
import java.util.LinkedList;

public class IntermediateRep implements IIntermediateRep {

    private final LinkedList<LineStatement> intermediateRep;

    public IntermediateRep() {
        this.intermediateRep = new LinkedList<>();
    }

    public void add(LineStatement ls) {
        this.intermediateRep.add(ls);
    }

    public void addAll(Collection<LineStatement> ls) {
        this.intermediateRep.addAll(ls);
    }

    public void copyIR(IntermediateRep ir) {
        this.intermediateRep.addAll(ir.intermediateRep);
    }

    public LinkedList<LineStatement> getStatements() {
        return intermediateRep;
    }
}
