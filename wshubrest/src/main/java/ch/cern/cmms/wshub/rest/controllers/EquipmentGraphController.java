package ch.cern.cmms.wshub.rest.controllers;


import ch.cern.cmms.wshub.equipment.EquipmentGraphService;
import ch.cern.cmms.wshub.equipment.entities.EquipmentGraphRequest;
import ch.cern.cmms.wshub.equipment.entities.Graph;
import ch.cern.eam.wshub.core.tools.InforException;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.sql.SQLException;

@ManagedBean
@Path("/hierarchy")
public class EquipmentGraphController {

	@Inject
	private EquipmentGraphService equipmentGraphService;
	
	@GET
	@Path("/get")
	@Produces("application/json")
	@Consumes("application/json")
	public Graph readEquipmentGraph(@QueryParam("parent") String parent, @QueryParam("linkTypes") String linkTypes, @QueryParam("maxDepth") int maxDepth) throws InforException {
		EquipmentGraphRequest req = new EquipmentGraphRequest();
		req.setEquipmentCode(parent);
		req.setLinkTypes(linkTypes);
		req.setMaxDepth(maxDepth);
		return equipmentGraphService.readEquipmentGraph(req, null, null);
	}

	@GET
	@Path("/screen")
	@Produces("application/json")
	@Consumes("application/json")
	public String getScreen(@QueryParam("user") String user, @QueryParam("function") String function) throws SQLException {
		return equipmentGraphService.getScreen(user, function);
	}
}
