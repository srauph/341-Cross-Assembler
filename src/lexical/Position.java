package lexical;// Position.java - (c) 2001-2021 by Michel de Champlain

public class Position {
    public Position(int lineNumber, int columnNumber) {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }
    public  int    getLineNumber()   { return lineNumber;   }
    public  int    getColumnNumber() { return columnNumber; }

    public  String toString() {
        return "("+getLineNumber()+","+getColumnNumber()+")";
    }
    private int lineNumber;
    private int columnNumber;
}
