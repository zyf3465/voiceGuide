package workFlow;

public class Node {

	private String id;  
	private String nextNodeId;
	private String conditionRegex;
	
	private String actionType ;
	private String question;
	
	public  static String ASK = "ASK";
	public  static String STORE = "STORE";
	public  static String REDIRECT = "REDIRECT";

	public Node(String id, String nextNodeId, String conditionRegex,
			String actionType,String question) {
		super();
		this.id = id;
		this.nextNodeId = nextNodeId;
		this.conditionRegex = conditionRegex;
		this.actionType = actionType;
		this.question = question;
	}

	public String getId() {
		return id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNextNodeId() {
		return nextNodeId;
	}

	public void setNextNodeId(String nextNodeId) {
		this.nextNodeId = nextNodeId;
	}

	public String getConditionRegex() {
		return conditionRegex;
	}

	public void setConditionRegex(String conditionRegex) {
		this.conditionRegex = conditionRegex;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	


}
