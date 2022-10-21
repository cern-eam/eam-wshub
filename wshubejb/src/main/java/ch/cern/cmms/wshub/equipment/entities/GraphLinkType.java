package ch.cern.cmms.wshub.equipment.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GraphLinkType {

	public GraphLinkType() {};

	public GraphLinkType(String linkCode, String linkDesc, String linkColor) {
		super();
		this.linkCode = linkCode;
		this.linkDesc = linkDesc;
		this.linkColor = linkColor;
	}

	private String linkCode;
	private String linkDesc;
	private String linkColor;

	public String getLinkCode() {
		return linkCode;
	}
	public void setLinkCode(String linkCode) {
		this.linkCode = linkCode;
	}
	public String getLinkDesc() {
		return linkDesc;
	}
	public void setLinkDesc(String linkDesc) {
		this.linkDesc = linkDesc;
	}
	public String getLinkColor() {
		return linkColor;
	}
	public void setLinkColor(String linkColor) {
		this.linkColor = linkColor;
	}

}
