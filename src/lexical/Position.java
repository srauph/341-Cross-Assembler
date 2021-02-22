package lexical;

public class Position {

    public Position(int lineNumber, int columnNumber) {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
    public int getColumnNumber() {
        return columnNumber;
    }

    public  String toString() {
        return "(line "+lineNumber+", column "+columnNumber+")";
    }
    private int lineNumber;
    private int columnNumber;
}
