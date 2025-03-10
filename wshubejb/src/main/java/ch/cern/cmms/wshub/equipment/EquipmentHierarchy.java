package ch.cern.cmms.wshub.equipment;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.ws.rs.NotFoundException;

import ch.cern.cmms.wshub.equipment.entities.*;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestFilter;
import ch.cern.eam.wshub.core.tools.InforException;

@ApplicationScoped
public class EquipmentHierarchy {

	@Inject
	private InforClient inforClient;

	//
	// GRAPH
	//
	public Graph readEquipmentGraph(EquipmentGraphRequest hierarchyrequest, Credentials credentials, String session)
			throws InforException {
		if (hierarchyrequest == null) {
			throw inforClient.getTools().generateFault("Please supply the necessery information ");
		}

		if (hierarchyrequest.getEquipmentCode() == null || hierarchyrequest.getEquipmentCode().trim().equals("")) {
			return null;
		}

		if (hierarchyrequest.getLinkTypes() == null || hierarchyrequest.getLinkTypes().trim().equals("")) {
			hierarchyrequest.setLinkTypes("STRUCTURE");
		}

		if (hierarchyrequest.getMaxDepth() <= 0) {
			hierarchyrequest.setMaxDepth(1);
		}

		hierarchyrequest.setLinkTypes("('" + hierarchyrequest.getLinkTypes().replace(",", "','") + "')");

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
				+ ") EQPLINKS WHERE EQPLINKS.linktype IN " + hierarchyrequest.getLinkTypes() + " AND ABS(HLEVEL) <= '"
				+ hierarchyrequest.getMaxDepth() + "' AND ROWNUM <= 100"
				+ ") eqplinks, r5objects eqpparent, r5ucodes ucodesparent where obj_code = eqplinks.parentcode and uco_entity = 'OBST' and uco_code = obj_status "
				+ ") eqplinks, r5objects eqpchild, r5ucodes ucodeschild where obj_code = eqplinks.childcode and uco_entity = 'OBST' and uco_code = obj_status";

		LinkedHashMap<String, Node> nodes = new LinkedHashMap<String, Node>();
		LinkedList<Edge> edges = new LinkedList<Edge>();

		PreparedStatement stmt = null;
		Connection v_connection = null;
		try {
			v_connection = inforClient.getTools().getDataSource().getConnection();

			stmt = v_connection.prepareStatement(v_query);
			stmt.setString(1, hierarchyrequest.getEquipmentCode().toUpperCase().trim());
			stmt.setString(2, hierarchyrequest.getEquipmentCode().toUpperCase().trim());
			stmt.setString(3, hierarchyrequest.getEquipmentCode().toUpperCase().trim());
			stmt.setString(4, hierarchyrequest.getEquipmentCode().toUpperCase().trim());

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String parentCode = rs.getString("PARENTCODE");
				String parentType = rs.getString("PARENTTYPE");
				String childCode = rs.getString("CHILDCODE");
				String childType = rs.getString("CHILDTYPE");
				// add Node first (if not already present)
				if (!nodes.containsKey(parentCode)) {
					Node node1 = new Node(parentCode, rs.getString("PARENTDESC"), parentType);
					nodes.put(parentCode, node1);
				}
				if (!nodes.containsKey(childCode)) {
					Node node2 = new Node(childCode, rs.getString("CHILDDESC"), childType);
					nodes.put(childCode, node2);
				}
				// add Edge
				Edge edge = new Edge(parentCode, childCode, rs.getString("LINKTYPE"));
				edges.add(edge);

			}
			v_connection.close();
		} catch (Exception e) {
			System.out.println("EXCEPTION1: " + e.getMessage());
		} finally {
			try {
				if (v_connection != null)
					v_connection.close();
			} catch (Exception e) {
				//TODO
			}
		}

		//
		LinkedList<String> startNodes = new LinkedList<String>();
		startNodes.add(hierarchyrequest.getEquipmentCode().toUpperCase());
		LinkedList<String> finalNodes = new LinkedList<String>();
		finalNodes.add(hierarchyrequest.getEquipmentCode().toUpperCase());

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
		if (edges.size() >= 100) {
			graph.complete = 1;
		} else {
			graph.complete = 0;
		}
		fetchLinkTypes(graph, hierarchyrequest.getEquipmentCode().toUpperCase());

		return graph;

	}

	/**
	 * For the target "code" look for connections using the procedure CAMMS.GET_EQUIPMENT_LINKS to it (where it is EQP_MAINEQP)
	 * and from it (where it is EQP_SRCEQP) then do the same for the parents and children until maxDepth is reached.
	 * Only consider connections of types "linkTypes". Only return at most maxRows.
	 * @param code // R5OBJECTS code. WIll use it to look in U5EQPDEPENDENCY (EQP_MAINEQP, EQP_SRCEQP)
	 * @param linkTypes U5EQPDEPENDENCY - EQP_DEPTYPE
	 * @param maxDepth HLEVEL (from the procedure) max depth
	 * @param maxRows
	 * @return A list of ElectricalLinkDTO's
	 */
	public Graph readElectricalGraph(
		EquipmentGraphRequest hierarchyrequest
	) throws InforException {

		if (hierarchyrequest == null) {
			throw inforClient.getTools().generateFault("Please supply the necessery information ");
		}
		if (hierarchyrequest.getEquipmentCode() == null
			|| hierarchyrequest.getEquipmentCode().trim().equals("")
		) { return null; }
		if (hierarchyrequest.getLinkTypes() == null
			|| hierarchyrequest.getLinkTypes().trim().equals("")
		) {
			hierarchyrequest.setLinkTypes("POWERED_BY");
		}
		hierarchyrequest.setLinkTypes(hierarchyrequest.getLinkTypes());
		if (hierarchyrequest.getMaxDepth() <= 0) {
			hierarchyrequest.setMaxDepth(1);
		}
		try {
			EntityManager entityManager = inforClient.getTools().getEntityManager();
			StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("CAMMS.GET_EQUIPMENT_LINKS");

			// Set the procedure parameter types
			storedProcedureQuery.registerStoredProcedureParameter("code", String.class, ParameterMode.IN);
			storedProcedureQuery.registerStoredProcedureParameter("linkTypes", String.class, ParameterMode.IN);
			storedProcedureQuery.registerStoredProcedureParameter("maxDepth", Integer.class, ParameterMode.IN);
			storedProcedureQuery.registerStoredProcedureParameter("maxRows", Integer.class, ParameterMode.IN);
			// Set the procedure output, which is a set of rows
			storedProcedureQuery.registerStoredProcedureParameter("dto_result", ResultSet.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("code", hierarchyrequest.getEquipmentCode());
			// Concatenate linKTypes into a comma separated string, to be managed by the procedure. Easier than passing an array to a procedure.
			storedProcedureQuery.setParameter("linkTypes", String.join(",", hierarchyrequest.getLinkTypes()));
			storedProcedureQuery.setParameter("maxDepth", hierarchyrequest.getMaxDepth());
			storedProcedureQuery.setParameter("maxRows", 100);

			storedProcedureQuery.execute();

			Graph returnResult = new Graph();
			List<Object[]> queryResult = new ArrayList<>();
			// Map each result row to the DTO class
			for (Object[] row : queryResult) {
				ElectricalLinkDTO dto = mapRow2DTO(row, ElectricalLinkDTO.class);

				/*List<GridRequestFilter> gridFilters = new ArrayList<>(Arrays.asList(
						new GridRequestFilter("pf1", dto.getEQP_SRCEQP(), "=", GridRequestFilter.JOINER.AND, true, false),
						new GridRequestFilter("pf2", dto.getEQP_MAINEQP(), "=", GridRequestFilter.JOINER.OR, false, true),
						new GridRequestFilter("pf1", dto.getEQP_MAINEQP(), "=", GridRequestFilter.JOINER.AND, true, false),
						new GridRequestFilter("pf2", dto.getEQP_SRCEQP(), "=", GridRequestFilter.JOINER.OR, false, true),
						new GridRequestFilter("elem1", dto.getEQP_SRCEQP(), "=", GridRequestFilter.JOINER.AND, true, false),
						new GridRequestFilter("elem2", dto.getEQP_SRCEQP(), "=", GridRequestFilter.JOINER.OR, false, true),
						new GridRequestFilter("elem1", dto.getEQP_SRCEQP(), "=", GridRequestFilter.JOINER.AND, true, false),
						new GridRequestFilter("elem2", dto.getEQP_SRCEQP(), "=", null, false, true)
				));
				List<Cable> cables = eamCableService.getCables(gridFilters, username);

				HashMap<String, Node> nodes = graph.getNodes();
				if (!nodes.containsKey(dto.getEQP_MAINEQP())) {
					nodes.put(
							dto.getEQP_MAINEQP(),
							new Node(dto.getEQP_MAINEQP(), dto.getHLEVEL())
					);
				}
				HashMap<String, Edge> edges = graph.getEdges();
				if(!edges.containsKey(dto.getEQP_ID())) {
					edges.put(
							dto.getEQP_ID().toString(),
							new Edge(
									dto.getEQP_ID(),
									"attribute",
									dto.getEQP_SRCEQP(),
									dto.getEQP_MAINEQP(),
									dto.getEQP_DEPDESC(),
									cables
							)
					);
				}*/
			}
			return null;
		}

		catch(Exception e) {
			throw new NotFoundException(e.getMessage());
		}
	}

	/**
	 * Convert the array of Objects, corresponding to a table row, obtained by storedProcedureQuery into an array of Objects <T>.
	 * The order of row is the same as the one returned in SQL, but the parameter names are missing. They are obtained ehre, through reflection.
	 * @param <T>
	 * @param row
	 * @param DTOClass must pass the class into which to convert, not an object
	 * @return
	 */
	public <T> T mapRow2DTO(
			Object[] row,
			Class<T> DTOClass
	) {
		try {
			// Get the constructor corresponding to query row's number of values
			Constructor<?>[] constructors = DTOClass.getConstructors();
			Constructor<?> targetConstructor = Arrays.stream(constructors)
					.filter(c -> c.getParameterCount() == row.length)
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException("No matching constructor found"));

			Class[] paramTypes = targetConstructor.getParameterTypes();
			Object[] constructorParameters = new Object[row.length];

			// pipulate the constructor arguments. If new types are used by the DTO's or queries, they need to be added here.
			for (int i = 0; i < paramTypes.length; i++) {
				if (paramTypes[i].getName() == "java.lang.String") {
					constructorParameters[i] = (String)row[i];
				} else if (paramTypes[i].getName() == "java.lang.Integer") {
					constructorParameters[i] = (Integer)row[i];
				} else if (paramTypes[i].getName() == "java.math.BigDecimal") {
					constructorParameters[i] = (BigDecimal)row[i];
				}
			}
			// Create an object of that type with the parameters obtained
			return (T) targetConstructor.newInstance(constructorParameters);
		} catch (Exception e) {
			throw new RuntimeException("Error mapping row to DTO", e);
		}
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

		String v_query = "select * from ("
				+ "select level as hlevel, stc_parent as PARENTCODE, stc_child as CHILDCODE, 'STRUCTURE' as LINKTYPE, 'Installed In' as LINKDESC, 'BLACK' as LINKCOLOR "
				+ "from r5structures where stc_parent_org = '*' and stc_child_org = '*' "
				+ "start with stc_parent = ?  " + "connect by nocycle prior stc_child = stc_parent " + "union  "
				+ "select -1 * level as hlevel, stc_parent as PARENTCODE, stc_child as CHILDCODE, 'STRUCTURE' as LINKTYPE, 'Installed In' as LINKDESC, 'BLACK' as LINKCOLOR  "
				+ "from r5structures where stc_parent_org = '*' and stc_child_org = '*'  "
				+ "start with stc_child = ?  " + "connect by nocycle prior stc_parent = stc_child  " + "union "
				+ "select level as hlevel, eqp_maineqp AS PARENTCODE, eqp_srceqp AS CHILDCODE, EQP_DEPTYPE as LINKTYPE, EDT_DESC as LINKDESC, edt_color as LINKCOLOR  "
				+ "from u5eqpdependency, u5eqpdepentypes " + "where eqp_deptype = edt_code "
				+ "start with eqp_maineqp = ? " + "connect by nocycle prior eqp_srceqp = eqp_maineqp " + "union "
				+ "select level as hlevel, eqp_maineqp AS PARENTCODE, eqp_srceqp AS CHILDCODE, EQP_DEPTYPE as LINKTYPE, EDT_DESC as LINKDESC, edt_color as LINKCOLOR  "
				+ "from u5eqpdependency, u5eqpdepentypes " + "where eqp_deptype = edt_code "
				+ "start with eqp_srceqp = ? "
				+ "connect by nocycle prior eqp_maineqp = eqp_srceqp ) where ROWNUM <= 100";

		LinkedHashMap<String, GraphLinkType> links = new LinkedHashMap<String, GraphLinkType>();

		PreparedStatement stmt = null;
		Connection v_connection = null;
		try {
			v_connection = inforClient.getTools().getDataSource().getConnection();

			stmt = v_connection.prepareStatement(v_query);
			stmt.setString(1, parent.toUpperCase().trim());
			stmt.setString(2, parent.toUpperCase().trim());
			stmt.setString(3, parent.toUpperCase().trim());
			stmt.setString(4, parent.toUpperCase().trim());

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				// always add Link type
				if (Math.abs(rs.getInt("HLEVEL")) > maxDepth) {
					maxDepth = Math.abs(rs.getInt("HLEVEL"));
				}
				String linkType = rs.getString("LINKTYPE");
				if (!links.containsKey(rs.getString("LINKTYPE"))) {
					GraphLinkType link = new GraphLinkType(linkType, rs.getString("LINKDESC"));
					links.put(linkType, link);
				}
			}
			v_connection.close();
		} catch (Exception e) {
			System.out.println("EXCEPTION2: " + e.getMessage());
		} finally {
			try {
				if (v_connection != null)
					v_connection.close();
			} catch (Exception e) {

			}
		}
		graph.linkTypes = links.values().toArray(new GraphLinkType[0]);
		graph.maxDepth = maxDepth;
	}

	public String screen(String user, String function) throws SQLException {

		if (user == null || user.trim().equals("") || function == null || function.trim().equals("")) {
			return null;
		}

		String returnValue = function;
		String v_query = "SELECT EMN_FUNCTION FROM R5EXTMENUS,R5EXTMENULANG,R5FUNCTIONS,R5USERS WHERE USR_CODE = '"
				+ user.toUpperCase() + "' "
				+ "AND EMN_GROUP = USR_GROUP AND EMN_CODE=EML_EXTMENU AND EMN_FUNCTION=FUN_CODE "
				+ "AND COALESCE(EMN_MOBILE,'-')='-' AND COALESCE(EMN_HIDE,'-')='-' " + "AND (EMN_FUNCTION = '"
				+ function.toUpperCase() + "' OR FUN_APPLICATION = '" + function.toUpperCase()
				+ "') ORDER BY FUN_APPLICATION DESC";
		Statement stmt = null;
		Connection v_connection = null;
		try {
			v_connection = inforClient.getTools().getDataSource().getConnection();
			stmt = v_connection.createStatement();
			ResultSet rs = stmt.executeQuery(v_query);
			if (rs.next()) {
				returnValue = rs.getString("EMN_FUNCTION");
			}
			v_connection.close();
		} catch (Exception e) {
			System.out.println("Exception in mobile/hierarchy/screen: " + e.getMessage());
		} finally {
			if (v_connection != null)
				v_connection.close();
		}
		return returnValue;
	}

}
