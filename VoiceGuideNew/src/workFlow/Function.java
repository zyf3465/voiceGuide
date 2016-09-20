package workFlow;

import java.util.List;
import java.util.Map;

public class Function {

	private String id; //id
	private String name;
	
	private Map fieldMap ;//表单映射和值
	private Map nodeList ;//节点列表
	
	
	public Function(String id, String name, Map fieldMap, Map nodeList) {
		super();
		this.id = id;
		this.name = name;
		this.fieldMap = fieldMap;
		this.nodeList = nodeList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map fieldMap) {
		this.fieldMap = fieldMap;
	}

	public Map getNodeList() {
		return nodeList;
	}

	public void setNodeList(Map nodeList) {
		this.nodeList = nodeList;
	}
	
}
