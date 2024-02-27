package ch.cern.cmms.wshub.rest.controllers;

import ch.cern.cmms.wshub.rest.WSHubController;
import ch.cern.cmms.wshub.rest.annotations.ApiInforAuthentication;
import ch.cern.cmms.wshub.rest.annotations.ApiInforResponse;
import ch.cern.cmms.wshub.rest.tools.AuthenticationTools;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.contractmanagement.entities.EquipmentReservationAdjustment;
import ch.cern.eam.wshub.core.tools.InforException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/equipmentreservationadjustments")
@Api(tags = {"Equipment Reservation Adjustments"})
public class EquipmentReservationAdjustmentController extends WSHubController {

    @Inject
    private AuthenticationTools authentication;

    @Inject
    private InforClient inforClient;

    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation(value = "Create Equipment Reservation Adjustment", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response createEquipmentReservationAdjustment(EquipmentReservationAdjustment equipmentReservationAdjustment) {
        try {
            return ok(inforClient.getEquipmentReservationAdjustmentService().createEquipmentReservationAdjustment(authentication.getInforContext(), equipmentReservationAdjustment));
        } catch (InforException e) {
            return badRequest(e);
        } catch (Exception e) {
            return serverError(e);
        }
    }

    @GET
    @Path("/{code}")
    @Produces("application/json")
    @ApiOperation(value = "Read Equipment Reservation Adjustment", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response readEquipmentReservationAdjustment(@PathParam("code") String code) {
        try {
            return ok(inforClient.getEquipmentReservationAdjustmentService().readEquipmentReservationAdjustment(authentication.getInforContext(), code));
        } catch (InforException e) {
            return badRequest(e);
        } catch (Exception e) {
            return serverError(e);
        }
    }


    @PUT
    @Path("/{code}")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation(value = "Update Equipment Reservation Adjustment", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response updateEquipmentReservationAdjustment(@PathParam("code") String code, EquipmentReservationAdjustment equipmentReservationAdjustment) {
        try {
            equipmentReservationAdjustment.setCode(code);
            return ok(inforClient.getEquipmentReservationAdjustmentService().updateEquipmentReservationAdjustment(authentication.getInforContext(), equipmentReservationAdjustment));
        } catch (InforException e) {
            return badRequest(e);
        } catch (Exception e) {
            return serverError(e);
        }
    }

    @DELETE
    @Path("/{code}")
    @Produces("application/json")
    @ApiOperation(value = "Delete Equipment Reservation Adjustment", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response deleteEquipmentReservationAdjustment(@PathParam("code") String code) {
        try {
            return ok(inforClient.getEquipmentReservationAdjustmentService().deleteEquipmentReservationAdjustment(authentication.getInforContext(), code));
        } catch (InforException e) {
            return badRequest(e);
        } catch (Exception e) {
            return serverError(e);
        }
    }

}
