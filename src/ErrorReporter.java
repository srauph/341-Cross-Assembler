import java.util.LinkedList;

public class ErrorReporter implements IErrorReporter{

	LinkedList<ErrorMsg> list;
	boolean errorsReported;
	String fileName;

	public ErrorReporter(Options options){
		list = new LinkedList<ErrorMsg>();
		errorsReported = false;
		fileName = options.getFileName() + ".asm";
	}

	public void record(ErrorMsg error){
		this.list.add(error);
		this.errorsReported = true;
	}
	
	public void report(){
		if (this.errorsReported){
			int errorCount = 0;
			while (errorCount<list.size()){
				System.out.print(fileName + ": Error: Line: " + list.get(errorCount).getPosition().getLineNumber() + ": " + list.get(errorCount).getMessage() + "\n");
				errorCount++;
			}
			System.out.print("\n" + errorCount + "errors.");
			System.exit(1);
		}
	}

	public boolean errorsReported(){
		return errorsReported;
	}

}