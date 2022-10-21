package ch.cern.cmms.wshub.rest.entities.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Edge {

	public Edge() {
		super();
	}
	
	public Edge(String from, String to, String arrows, String linkType) {
		super();
		this.from = from;
		this.to = to;
		this.arrows = arrows;
		this.linkType = linkType;
	}
	
	private String from;
	private String to;
	private String arrows;
	private String linkType;
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}

	public String getArrows() {
		return arrows;
	}

	public void setArrows(String arrows) {
		this.arrows = arrows;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}


}
