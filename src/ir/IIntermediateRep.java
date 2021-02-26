package ir;

import java.util.Collection;
import java.util.LinkedList;

public interface IIntermediateRep {

    void add(LineStatement ls);

    void addAll(Collection<LineStatement> ls);

    void copyIR(IntermediateRep ir);

    LinkedList<LineStatement> getStatements();
}
