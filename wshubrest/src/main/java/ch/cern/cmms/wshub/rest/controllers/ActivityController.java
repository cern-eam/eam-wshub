package ch.cern.cmms.wshub.rest.controllers;

import ch.cern.cmms.wshub.rest.WSHubController;
import ch.cern.cmms.wshub.rest.annotations.ApiInforAuthentication;
import ch.cern.cmms.wshub.rest.annotations.ApiInforResponse;
import ch.cern.cmms.wshub.rest.tools.AuthenticationTools;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.workorders.entities.Activity;
import ch.cern.eam.wshub.core.tools.InforException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/workorders/activities")
@Api(tags={"Work Order Activities"})
public class ActivityController extends WSHubController {

    @Inject
    private AuthenticationTools authentication;

    @Inject
    private InforClient inforClient;

    @GET
    @Path("/{number}")
    @Produces("application/json")
    @ApiOperation(value = "Read Work Order Activities", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response readWorkOrderActivities(@PathParam("number") String workOrderNumber) {
        try {
            return ok(inforClient.getLaborBookingService().readActivities(authentication.getInforContext(), workOrderNumber, true));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @POST
    @Path("/{number}")
    @Produces("application/json")
    @ApiOperation(value = "Create Work Order Activity", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response createWorkOrderActivity(@PathParam("number") String workOrderNumber, Activity activity) {
        try {
            activity.setWorkOrderNumber(workOrderNumber);
            return ok(inforClient.getLaborBookingService().createActivity(authentication.getInforContext(), activity));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @PUT
    @Path("/{number}")
    @Produces("application/json")
    @ApiOperation(value = "Update Work Order Activity", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response updateWorkOrderActivity(@PathParam("number") String workOrderNumber, Activity activity) {
        try {
            activity.setWorkOrderNumber(workOrderNumber);
            return ok(inforClient.getLaborBookingService().updateActivity(authentication.getInforContext(), activity));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

}
