package errorReporting;

import lexical.token.Position;

public class ErrorMsg {

    String msg;
    Position pos;

    public ErrorMsg() {
    	this.msg = "";
    	this.pos = null;
    }

    public ErrorMsg(String message, Position position) {
        this.msg = message;
        this.pos = position;
    }
	public String getMessage() {
		return msg;
	}

	public void setMessage(String msg) {
		this.msg = msg;
	}

	public Position getPosition() {
		return pos;
	}

	public void setPosition(Position pos) {
		this.pos = pos;
	}
}