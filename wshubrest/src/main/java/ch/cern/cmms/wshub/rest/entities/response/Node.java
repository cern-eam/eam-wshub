package ch.cern.cmms.wshub.rest.entities.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Node {
	
	public Node(String id, String label, String title, String type) {
		super();
		this.id = id;
		this.label = label;
		this.title = title;
		this.type = type;
	}
	
	public Node() {
		id = null;
		label = null;
		title = null;
	}
	private String id;
	private String label;
	private String title;
	private String type;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


}
