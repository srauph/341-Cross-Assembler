import lexical.token.Position;

public class ErrorMsg{

	String msg;
	Position pos;

	public ErrorMsg(String message, Position position){
		this.msg = message;
		this.pos = position;
	}

	public String getMessage(){
		return msg;
	}

	public Position getPosition(){
		return pos;
	}

}