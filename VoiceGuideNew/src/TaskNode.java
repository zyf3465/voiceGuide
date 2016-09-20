
public class TaskNode {
	private int id = -1;
	private String type = "";
	private int next_id = -1;
	private Processor processor = null;
	
	public TaskNode(int id, String type, int next_id, Processor processor) {
		this.id = id;
		this.type = type;
		this.next_id = next_id;
		this.processor = processor;
	}
	
	public TaskNode(int id, int next_id, Processor processor) {
		this.id = id;
		this.next_id = next_id;
		this.processor = processor;
	}
	
	public TaskNode(int id, Processor processor) {
		this.id = id;
		this.processor = processor;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getNext_id() {
		return next_id;
	}
	public void setNext_id(int next_id) {
		this.next_id = next_id;
	}
	public Processor getProcessor() {
		return processor;
	}
	public void setProcessor(Processor processor) {
		this.processor = processor;
	}
}
