package ch.cern.cmms.wshub.rest.entities.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Graph {

	public Node[] nodes;
	public Edge[] edges;
	public GraphLinkType[] linkTypes;
	public int maxDepth;
	public int complete;
}
