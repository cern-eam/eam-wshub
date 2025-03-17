package ch.cern.cmms.wshub.rest.controllers;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import ch.cern.cmms.wshub.rest.entities.response.Edge;
import ch.cern.cmms.wshub.rest.entities.response.Graph;
import ch.cern.cmms.wshub.rest.entities.response.GraphLinkType;
import ch.cern.cmms.wshub.rest.entities.response.Node;

@ManagedBean
@Path("/hierarchy")
public class EquipmentHierarchy {

	@Resource
	private DataSource datasource;

	@GET
	@Path("/get")
	@Produces("application/json")
	@Consumes("application/json")
	public Graph hierarchy(@QueryParam("parent") String parent,@QueryParam("linkTypes") String linkTypes,@QueryParam("maxDepth") int maxDepth) throws SQLException{

		if (parent == null || parent.trim().equals("")) {
			System.out.println("EXCEPTION1: No parent found");
			return null;
		}
		if (linkTypes.length() > 200) {
			System.out.println("EXCEPTION1: linkTypes must be shorter than 200 characters.");
			return null;
		}
		linkTypes = linkTypes.replaceAll("\\s+","");
		linkTypes = "('" + linkTypes.replace(",", "','") + "')";

		String v_query = "select eqplinks.*, eqpchild.obj_desc  CHILDDESC,  eqpchild.obj_mrc as CHILDMRC, eqpchild.obj_obrtype as CHILDTYPE, eqpchild.obj_status as CHILDSTATUSC, ucodeschild.uco_desc as CHILDSTATUSD  from ( "
				+ "select eqplinks.*, eqpparent.obj_desc PARENTDESC, eqpparent.obj_mrc as PARENTMRC, eqpparent.obj_obrtype as PARENTTYPE, eqpparent.obj_status as PARENTSTATUSC, ucodesparent.uco_desc as PARENTSTATUSD  from ( "
				+ "select eqplinks.* from ("
				+ "select level as hlevel, stc_parent as PARENTCODE, stc_child as CHILDCODE, 'STRUCTURE' as LINKTYPE, 'Installed In' as LINKDESC, 'BLACK' as LINKCOLOR "
				+ "from r5structures where stc_parent_org = '*' and stc_child_org = '*' "
				+ "start with stc_parent = ?  " + "connect by nocycle prior stc_child = stc_parent " + "union  "
				+ "select -1 * level as hlevel, stc_parent as PARENTCODE, stc_child as CHILDCODE, 'STRUCTURE' as LINKTYPE, 'Installed In' as LINKDESC, 'BLACK' as LINKCOLOR  "
				+ "from r5structures where stc_parent_org = '*' and stc_child_org = '*'  "
				+ "start with stc_child = ?  " + "connect by nocycle prior stc_parent = stc_child  " + "union "
				+ "select level as hlevel, eqp_srceqp AS PARENTCODE, eqp_maineqp AS CHILDCODE, EQP_DEPTYPE as LINKTYPE, EDT_DESC as LINKDESC, edt_color as LINKCOLOR  "
				+ "from u5eqpdependency, u5eqpdepentypes " + "where eqp_deptype = edt_code "
				+ "start with eqp_maineqp = ? " + "connect by nocycle prior eqp_srceqp = eqp_maineqp " + "union "
				+ "select -1 * level as hlevel, eqp_srceqp AS PARENTCODE, eqp_maineqp AS CHILDCODE, EQP_DEPTYPE as LINKTYPE, EDT_DESC as LINKDESC, edt_color as LINKCOLOR  "
				+ "from u5eqpdependency, u5eqpdepentypes " + "where eqp_deptype = edt_code "
				+ "start with eqp_srceqp = ? " + "connect by nocycle prior eqp_maineqp = eqp_srceqp "
				+ ") EQPLINKS WHERE EQPLINKS.linktype IN "+ linkTypes + " AND ABS(HLEVEL) <= " + maxDepth
				+ " AND ROWNUM <= 100"
				+ ") eqplinks, r5objects eqpparent, r5ucodes ucodesparent where obj_code = eqplinks.parentcode and uco_entity = 'OBST' and uco_code = obj_status "
				+ ") eqplinks, r5objects eqpchild, r5ucodes ucodeschild where obj_code = eqplinks.childcode and uco_entity = 'OBST' and uco_code = obj_status";

		/*String v_query = "with connections as ( " +
				"    select " +
				"        'STRUCTURE' as LINKTYPE, " +
				"        stc_parent as PARENTCODE, " +
				"        stc_child as CHILDCODE, " +
				"        'Installed In' as LINKDESC, " +
				"        'BLACK' as LINKCOLOR " +
				"    from r5structures " +
				"    UNION " +
				"    select " +
				"        EQP_DEPTYPE as LINKTYPE, " +
				"        eqp_srceqp as PARENTCODE, " +
				"        eqp_maineqp as CHILDCODE, " +
				"        edt_desc as LINKDESC, " +
				"        edt_color as LINKCOLOR " +
				"    from u5eqpdependency " +
				"        inner join u5eqpdepentypes on eqp_deptype = edt_code " +
				"    ) " +
				"select hlevel, LINKTYPE, PARENTCODE, CHILDCODE, LINKDESC, LINKCOLOR, " +
				"        p.obj_desc as PARENTDESC, " +
				"        p.obj_mrc as PARENTMRC, " +
				"        p.obj_obtype as PARENTTYPE, " +
				"        p.obj_status as PARENTSTATUSC, " +
				"        parentcodes.UCO_DESC as PARENTSTATUSD, " +
				"        c.obj_desc as CHILDDESC, " +
				"        c.obj_mrc as CHILDMRC, " +
				"        c.obj_obtype as CHILDTYPE, " +
				"        c.obj_status as CHILDSTATUSC, " +
				"        childcodes.UCO_DESC as CHILDSTATUSD " +
				"from ( " +
				"    select level as hlevel, LINKTYPE, PARENTCODE, CHILDCODE, LINKDESC, LINKCOLOR " +
				"    from connections " +
				"    start with CHILDCODE = ? " +
				"    connect by nocycle prior CHILDCODE = PARENTCODE " +
				"    UNION " +
				"    select -level as hlevel, LINKTYPE, PARENTCODE, CHILDCODE, LINKDESC, LINKCOLOR " +
				"    from connections " +
				"    start with CHILDCODE = ? " +
				"    connect by nocycle prior PARENTCODE = CHILDCODE " +
				") " +
				"    inner join r5objects c on CHILDCODE = c.obj_code " +
				"    inner join r5objects p on PARENTCODE = p.obj_code " +
				"    left join R5UCODES parentcodes ON parentcodes.UCO_ENTITY = 'OBST' AND parentcodes.UCO_CODE = p" +
				".OBJ_STATUS " +
				"    left join R5UCODES childcodes ON childcodes.UCO_ENTITY = 'OBST' AND childcodes.UCO_CODE = p" +
				".OBJ_STATUS " +
				"WHERE LINKTYPE in " + linkTypes +
				" AND ABS(HLEVEL) <= " + maxDepth + " AND ROWNUM <= 300";*/

		LinkedHashMap<String,Node> nodes = new LinkedHashMap<String,Node>();
		LinkedList<Edge> edges = new LinkedList<Edge>();

		PreparedStatement stmt = null;
		Connection v_connection = null;
		try {
			v_connection = datasource.getConnection();

			stmt = v_connection.prepareStatement(v_query);
			stmt.setString(1, parent.toUpperCase().trim());
			stmt.setString(2, parent.toUpperCase().trim());
			stmt.setString(3, parent.toUpperCase().trim());
			stmt.setString(4, parent.toUpperCase().trim());

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String parentCode = rs.getString("PARENTCODE");
				String parentType = rs.getString("PARENTTYPE");
				String childCode = rs.getString("CHILDCODE");
				String childType = rs.getString("CHILDTYPE");				
				// add Node first (if not already present)
				if (!nodes.containsKey(parentCode)) {
					Node node1 = new Node(parentCode, parentCode + " ("+parentType+")", "<b>Description: </b> " + rs.getString("PARENTDESC") + "<br><b>Status:</b> " + rs.getString("PARENTSTATUSD") + "<br><b>Department:</b> " + rs.getString("PARENTMRC"),  parentType);
					nodes.put(parentCode, node1);
				}
				if (!nodes.containsKey(childCode)) {
					Node node2 = new Node(childCode,childCode+ " ("+childType+")", "<b>Description: </b> " + rs.getString("CHILDDESC") + "<br><b>Status:</b> " + rs.getString("CHILDSTATUSD") + "<br><b>Department:</b> " + rs.getString("CHILDMRC"), childType );
					nodes.put(childCode, node2);
				}
				// add Edge
				Edge edge = new Edge(parentCode, childCode, "to", rs.getString("LINKTYPE"));
				edges.add(edge);
			}
			v_connection.close();
		} catch (Exception e) {
			System.out.println("EXCEPTION1: " + e.getMessage());
		} finally {
			if(v_connection != null) v_connection.close();
		}

		//
		LinkedList<String> startNodes = new LinkedList<String>();
		startNodes.add(parent.toUpperCase());
		LinkedList<String> finalNodes = new LinkedList<String>();
		finalNodes.add(parent.toUpperCase());

		reachableNodes(startNodes, finalNodes, edges);

		nodes.keySet().retainAll(finalNodes);

		for (int i = edges.size() - 1; i >= 0; i--) {
			if (!finalNodes.contains(edges.get(i).getFrom()) && !finalNodes.contains(edges.get(i).getTo())) {
				edges.remove(i);
			}
		}

		Graph graph = new Graph();
		graph.nodes = nodes.values().toArray(new Node[0]);
		graph.edges = edges.toArray(new Edge[0]);
		if (edges.size() >= 300) {
			graph.complete = 1;
		} else {
			graph.complete = 0;
		}
		fetchLinkTypes(graph, parent);

		return graph;

	}

	private void reachableNodes(LinkedList<String> startNodes, LinkedList<String> finalNodes, LinkedList<Edge> edges) {
		LinkedList<String> newStartNodes = new LinkedList<String>();

		for (Edge edge : edges) {
			for (String nodeID : startNodes) {
				//
				if (edge.getFrom().equals(nodeID) && !finalNodes.contains(edge.getTo())) {
					finalNodes.add(edge.getTo());
					newStartNodes.add(edge.getTo());
				}
				//
				if (edge.getTo().equals(nodeID) && !finalNodes.contains(edge.getFrom())) {
					finalNodes.add(edge.getFrom());
					newStartNodes.add(edge.getFrom());
				}
			}
		}

		if (newStartNodes.size() > 0) {
			reachableNodes(newStartNodes, finalNodes, edges);
		}
	}


	private void fetchLinkTypes(Graph graph, String parent) {

		int maxDepth = 0;

		String v_query = "with connections as ( " +
				"    select " +
				"        'STRUCTURE' as LINKTYPE, " +
				"        stc_parent as PARENTCODE, " +
				"        stc_child as CHILDCODE, " +
				"        'Installed In' as LINKDESC, " +
				"        'BLACK' as LINKCOLOR " +
				"    from r5structures " +
				"    UNION ALL " +
				"    select " +
				"        EQP_DEPTYPE as LINKTYPE, " +
				"        eqp_srceqp as PARENTCODE, " +
				"        eqp_maineqp as CHILDCODE, " +
				"        edt_desc as LINKDESC, " +
				"        edt_color as LINKCOLOR " +
				"    from u5eqpdependency " +
				"        inner join u5eqpdepentypes on eqp_deptype = edt_code " +
				") " +
				"select * from ( " +
				"    select level as hlevel, LINKTYPE, PARENTCODE, CHILDCODE, LINKDESC, LINKCOLOR " +
				"    from connections " +
				"    start with CHILDCODE = ? " +
				"    connect by nocycle prior CHILDCODE = PARENTCODE " +
				"    UNION ALL " +
				"    select -1 * level as hlevel, LINKTYPE, PARENTCODE, CHILDCODE, LINKDESC, LINKCOLOR " +
				"    from connections " +
				"    start with CHILDCODE = ? " +
				"    connect by nocycle prior PARENTCODE = CHILDCODE " +
				") " +
				"WHERE ROWNUM <= 300";

		LinkedHashMap<String, GraphLinkType> links = new LinkedHashMap<String, GraphLinkType>();

		PreparedStatement stmt = null;
		Connection v_connection = null;
		try {
			v_connection = datasource.getConnection();

			stmt = v_connection.prepareStatement(v_query);
			stmt.setString(1, parent.toUpperCase().trim());
			stmt.setString(2, parent.toUpperCase().trim());

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				// always add Link type
				if (Math.abs(rs.getInt("HLEVEL")) > maxDepth) {
					maxDepth = Math.abs(rs.getInt("HLEVEL"));
				}
				String linkType = rs.getString("LINKTYPE");
				if (!links.containsKey(rs.getString("LINKTYPE"))) {
					GraphLinkType link = new GraphLinkType(linkType, rs.getString("LINKDESC"), rs.getString("LINKCOLOR"));
					links.put(linkType, link);
				}
			}
			v_connection.close();
		}catch (Exception e) {
			System.out.println("EXCEPTION2: " + e.getMessage());
		}finally {
			try {
				if(v_connection != null) v_connection.close();
			} catch (Exception e) {

			}
		}
		graph.linkTypes = links.values().toArray(new GraphLinkType[0]);
		graph.maxDepth = maxDepth;
	}

	@GET
	@Path("/screen")
	@Produces("application/json")
	@Consumes("application/json")
	public String screen(@QueryParam("user") String user, @QueryParam("function") String function) throws SQLException{

		if (user == null || user.trim().equals("") || function == null || function.trim().equals("")) {
			return null;
		}

		String returnValue = function;
		String v_query = "SELECT EMN_FUNCTION FROM R5EXTMENUS,R5EXTMENULANG,R5FUNCTIONS,R5USERS WHERE USR_CODE = '"+ user.toUpperCase() +"' " + 
				"AND EMN_GROUP = USR_GROUP AND EMN_CODE=EML_EXTMENU AND EMN_FUNCTION=FUN_CODE " +
				"AND COALESCE(EMN_MOBILE,'-')='-' AND COALESCE(EMN_HIDE,'-')='-' " + 
				"AND (EMN_FUNCTION = '" + function.toUpperCase() + "' OR FUN_APPLICATION = '" + function.toUpperCase() + "') ORDER BY FUN_APPLICATION DESC";
		Statement stmt = null;
		Connection v_connection = null;
		try {
			v_connection = datasource.getConnection();
			stmt = v_connection.createStatement();
			ResultSet rs = stmt.executeQuery(v_query);
			if (rs.next()) {
				returnValue = rs.getString("EMN_FUNCTION");
			}
			v_connection.close();
		} catch (Exception e) {
			System.out.println("Exception in mobile/hierarchy/screen: " + e.getMessage());
		} finally {
			if(v_connection != null) v_connection.close();
		}
		return returnValue;
	}
}
