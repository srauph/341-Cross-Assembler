package errorhandling;

import options.Options;

import java.util.LinkedList;

public class ErrorReporter implements IErrorReporter {

    private final LinkedList<ErrorMsg> list;
    private boolean hasErrors;
    private final String fileName;

    public ErrorReporter(Options options) {
        this.list = new LinkedList<>();
        this.hasErrors = false;
        this.fileName = options.getFileName() + ".asm";
    }

    public ErrorReporter(String name) {
        this.list = new LinkedList<>();
        this.hasErrors = false;
        this.fileName = name + ".asm";
    }

    public void record(ErrorMsg error) {
        this.list.add(error);
        this.hasErrors = true;
    }

    public void checkReports() {
        if (this.hasErrors) {
            int errorCount = 0;
            while (errorCount < list.size()) {
                System.out.print(fileName + ": Error: Line: " + list.get(errorCount).getPosition().getLineNumber() + ": " + list.get(errorCount).getMessage() + "\n");
                errorCount++;
            }
            System.out.print(errorCount + " errors.\n");
            System.exit(1);
        }
    }

    public boolean hasErrors() {
        return hasErrors;
    }
}