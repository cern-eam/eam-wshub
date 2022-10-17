package ch.cern.cmms.wshub.rest.controllers;

import ch.cern.cmms.wshub.equipment.EquipmentReplacementService;
import ch.cern.cmms.wshub.rest.WSHubController;
import ch.cern.cmms.wshub.rest.annotations.ApiInforAuthentication;
import ch.cern.cmms.wshub.rest.annotations.ApiInforResponse;
import ch.cern.cmms.wshub.rest.tools.AuthenticationTools;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentReplacement;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentStructure;
import ch.cern.eam.wshub.core.tools.InforException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/equipmentstructure")
@Api(tags={"Equipment Structure"})
public class EquipmentStructureController extends WSHubController {

    @Inject
    private AuthenticationTools authentication;

    @Inject
    private EquipmentReplacementService equipmentReplacementService;

    @Inject
    private InforClient inforClient;

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Attach Equipment")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response addEquipmentToStructure(EquipmentStructure equipmentStructure) {
        try {
            return ok(inforClient.getEquipmentStructureService().addEquipmentToStructure(authentication.getInforContext(), equipmentStructure));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Update Equipment Structure")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response updateEquipmentStructure(EquipmentStructure equipmentStructure) {
        try {
            return ok(inforClient.getEquipmentStructureService().updateEquipmentStructure(authentication.getInforContext(), equipmentStructure));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @DELETE
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Remove Equipment From Structure")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response removeEquipmentFromStructure(EquipmentStructure equipmentStructure) {
        try {
            return ok(inforClient.getEquipmentStructureService().removeEquipmentFromStructure(authentication.getInforContext(), equipmentStructure));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @PUT
    @Path("/replace")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Replace Equipment On Structure")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response replaceEquipmentOnStructure(EquipmentReplacement replacement) {
        try {
            return ok(equipmentReplacementService.replaceEquipment(authentication.getInforContext(), replacement));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }
}
