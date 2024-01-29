package ch.cern.cmms.wshub.rest.controllers;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import ch.cern.cmms.wshub.rest.WSHubController;
import ch.cern.cmms.wshub.rest.annotations.ApiInforAuthentication;
import ch.cern.cmms.wshub.rest.annotations.ApiInforResponse;
import ch.cern.cmms.wshub.rest.tools.AuthenticationTools;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.grids.entities.*;
import ch.cern.eam.wshub.core.tools.GridTools;
import ch.cern.eam.wshub.core.tools.InforException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import java.util.*;
import java.util.stream.Collectors;

@Path("/grids")
@Api(tags={"Grid"})
public class Grid extends WSHubController {

	@Inject
	private AuthenticationTools authentication;

	@Inject
	private InforClient inforClient;

	@POST
	@Path("/data")
	@Produces("application/json")
	@Consumes("application/json")
	@ApiOperation(value = "Execute Grid Request", authorizations = {@Authorization(value = "basicAuth")})
	@ApiInforAuthentication
	@ApiInforResponse
	public Response executeQuery(GridRequest gridRequest) {
		try {
			return ok(inforClient.getGridsService().executeQuery(authentication.getInforContext(), gridRequest));
		} catch (InforException e) {
			return badRequest(e);
		} catch(Exception e) {
			return serverError(e);
		}
	}

	@POST
	@Path("/{gridName}/data")
	@Produces("application/json")
	@Consumes("application/json")
	@ApiOperation(value = "Execute Grid Request", authorizations = {@Authorization(value = "basicAuth")})
	@ApiInforAuthentication
	@ApiInforResponse
	public Response executeGridQuery(@PathParam("gridName") String gridName, GridRequest gridRequest) {
		try {
			gridRequest.setGridName(gridName);
			GridRequestResult gridRequestResult = inforClient.getGridsService().executeQuery(authentication.getInforContext(), gridRequest);
			LinkedList<LinkedHashMap<String, String>> collect = Arrays.stream(gridRequestResult.getRows())
					.map(row -> GridTools.gridRequestRowMapper(row, null))
					.collect(Collectors.toCollection(LinkedList::new));
			return ok(collect);
		} catch (InforException e) {
			return badRequest(e);
		} catch(Exception e) {
			return serverError(e);
		}
	}

}
