

import java.io.*;
import java.util.ArrayList;
import java.util.List;



public class Task {
	public Task(String type) {
		taskType = type;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String taskType;
	public List<TaskNode> nodes = new ArrayList();
	public List<TaskNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<TaskNode> nodes) {
		this.nodes = nodes;
	}
}
