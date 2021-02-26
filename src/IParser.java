import ir.IntermediateRep;

public interface IParser {
    void parseTokens();

    IntermediateRep getIR();
}
