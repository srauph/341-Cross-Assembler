import java.util.LinkedList;

public class ErrorReporter implements IErrorReporter{

	LinkedList<ErrorMsg> list;
	boolean errorsReported;

	public ErrorReporter(){
		list = new LinkedList<ErrorMsg>();
		errorsReported = false;
	}

	public void record(ErrorMsg error){
		this.list.add(error);
		this.errorsReported = true;
	}
	
	public void report(){
		if (this.errorsReported){
			int i = 0;
			while (i<list.size()){
				System.out.print("Error at line " + list.get(i).getPosition().getLineNumber() + ": " + list.get(i).getMessage() + "\n");
				i++;
			}
			System.out.print("\nErrors: " + i + "\n");
			System.exit(1);
		}
	}

	public boolean errorsReported(){
		return errorsReported;
	}

}