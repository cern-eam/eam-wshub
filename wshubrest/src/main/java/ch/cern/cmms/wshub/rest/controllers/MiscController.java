package ch.cern.cmms.wshub.rest.controllers;

import ch.cern.cmms.wshub.misc.Gas;
import ch.cern.cmms.wshub.misc.GasWorkOrder;
import ch.cern.cmms.wshub.rest.WSHubController;
import ch.cern.cmms.wshub.rest.tools.AuthenticationTools;
import ch.cern.eam.wshub.core.tools.InforException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/misc")
public class MiscController extends WSHubController {

    @Inject
    private AuthenticationTools authenticationTools;
    @Inject
    private Gas gas;

    @POST
    @Path("/gasworkorder")
    @Produces("application/json")
    @Consumes("application/json")
    public Response createGasWorkOrder(GasWorkOrder gasWorkOrder) {
        try {
            return ok(gas.createGasWorkOrder(authenticationTools.getInforContext(), gasWorkOrder));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

}
