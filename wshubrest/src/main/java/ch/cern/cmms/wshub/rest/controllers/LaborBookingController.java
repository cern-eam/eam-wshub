package ch.cern.cmms.wshub.rest.controllers;

import ch.cern.cmms.wshub.rest.WSHubController;
import ch.cern.cmms.wshub.rest.annotations.ApiInforAuthentication;
import ch.cern.cmms.wshub.rest.annotations.ApiInforResponse;
import ch.cern.cmms.wshub.rest.tools.AuthenticationTools;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.workorders.entities.LaborBooking;
import ch.cern.eam.wshub.core.tools.InforException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/workorders/laborbookings")
@Api(tags={"Work Orders Labor Bookings"})
public class LaborBookingController extends WSHubController {

    @Inject
    private AuthenticationTools authentication;

    @Inject
    private InforClient inforClient;

    @GET
    @Path("/{number}")
    @Produces("application/json")
    @ApiOperation(value = "Read Work Order Labor", authorizations = {@Authorization(value = "X-Auth-Token")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response readWorkOrderLaborBooking(@PathParam("number") String workOrderNumber) {
        try {
            return ok(inforClient.getLaborBookingService().readLaborBookings(authentication.getInforContext(), workOrderNumber));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @POST
    @Path("/{number}")
    @Produces("application/json")
    @ApiOperation(value = "Book Work Order Labor", authorizations = {@Authorization(value = "X-Auth-Token")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response createWorkOrderLaborBooking(@PathParam("number") String workOrderNumber, LaborBooking laborBooking) {
        try {
            laborBooking.setWorkOrderNumber(workOrderNumber);
            return ok(inforClient.getLaborBookingService().createLaborBooking(authentication.getInforContext(), laborBooking));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

}
