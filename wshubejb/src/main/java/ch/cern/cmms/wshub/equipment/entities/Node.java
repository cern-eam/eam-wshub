package ch.cern.cmms.wshub.equipment.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Node {
	
	private String code;
	private String desc;
	private String type;
	
	public Node(String code, String desc, String type) {
		super();
		this.code = code;
		this.desc = desc;
		this.type = type;
	}
	
	public Node() {
		code = null;
		desc = null;
		type = null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	




}
