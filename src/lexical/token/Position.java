package lexical.token;

public class Position implements IPosition {

    private final int lineNumber;
    private final int columnNumber;

    public Position(int lineNumber, int columnNumber) {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int getColumnNumber() {
        return this.columnNumber;
    }

    public String toString() {
        return "(line " + lineNumber + ", column " + columnNumber + ")";
    }
}