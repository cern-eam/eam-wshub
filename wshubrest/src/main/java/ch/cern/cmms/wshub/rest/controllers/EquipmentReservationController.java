package ch.cern.cmms.wshub.rest.controllers;

import ch.cern.cmms.wshub.rest.WSHubController;
import ch.cern.cmms.wshub.rest.annotations.ApiInforAuthentication;
import ch.cern.cmms.wshub.rest.annotations.ApiInforResponse;
import ch.cern.cmms.wshub.rest.tools.AuthenticationTools;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentReservation;
import ch.cern.eam.wshub.core.tools.InforException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/equipmentreservations")
@Api(tags={"Equipment Reservations"})
public class EquipmentReservationController extends WSHubController {

    @Inject
    private AuthenticationTools authentication;

    @Inject
    private InforClient inforClient;

    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation(value = "Create Equipment Reservation", authorizations = {@Authorization(value = "X-Auth-Token")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response createEquipmentReservation(EquipmentReservation equipmentReservation) {
        try {
            return ok(inforClient.getEquipmentReservationService().createEquipmentReservation(authentication.getInforContext(), equipmentReservation));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @GET
    @Path("/{code}")
    @Produces("application/json")
    @ApiOperation(value = "Read Equipment Reservation", authorizations = {@Authorization(value = "X-Auth-Token")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response readEquipmentReservation(@PathParam("code") String code) {
        try {
            return ok(inforClient.getEquipmentReservationService().readEquipmentReservation(authentication.getInforContext(), code));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }


    @PUT
    @Path("/{code}")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation(value = "Update Equipment Reservation", authorizations = {@Authorization(value = "X-Auth-Token")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response updateEquipmentReservation(@PathParam("code") String code, EquipmentReservation equipmentReservation) {
        try {
            equipmentReservation.setCode(code);
            return ok(inforClient.getEquipmentReservationService().updateEquipmentReservation(authentication.getInforContext(), equipmentReservation));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @DELETE
    @Path("/{code}")
    @Produces("application/json")
    @ApiOperation(value = "Delete Equipment Reservation", authorizations = {@Authorization(value = "X-Auth-Token")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response deleteEquipmentReservation(@PathParam("code") String code) {
        try {
            return ok(inforClient.getEquipmentReservationService().deleteEquipmentReservation(authentication.getInforContext(), code));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

}
