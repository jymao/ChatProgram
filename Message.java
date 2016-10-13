
public enum Message {
	CHAT(0), LOGIN(1), LOGOFF(2);
	
	private int id;
	
	Message(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
