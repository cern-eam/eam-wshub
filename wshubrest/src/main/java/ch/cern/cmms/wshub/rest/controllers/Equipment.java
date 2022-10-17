package ch.cern.cmms.wshub.rest.controllers;

import ch.cern.cmms.wshub.rest.WSHubController;
import ch.cern.cmms.wshub.rest.annotations.ApiInforAuthentication;
import ch.cern.cmms.wshub.rest.annotations.ApiInforResponse;
import ch.cern.cmms.wshub.rest.tools.AuthenticationTools;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentWarranty;
import ch.cern.eam.wshub.core.tools.InforException;
import io.swagger.annotations.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Path("/equipment")
@Api(tags={"Equipment"})
public class Equipment extends WSHubController {

    @Inject
    private AuthenticationTools authentication;

    @Inject
    private InforClient inforClient;

    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Create Equipment")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response createEquipment(ch.cern.eam.wshub.core.services.equipment.entities.Equipment equipment) {
        try {
            return ok(inforClient.getEquipmentFacadeService().createEquipment(authentication.getInforContext(), equipment));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @GET
    @Path("/{code}")
    @Produces("application/json")
    @ApiOperation("Read Equipment")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response readEquipment(@PathParam("code") String code, @QueryParam("code") String codeQuery) {
        try {
            return ok(inforClient.getEquipmentFacadeService().readEquipment(authentication.getInforContext(), codeQuery != null ? codeQuery : code));
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
    @ApiOperation("Update Equipment")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response updateEquipment(@PathParam("code") String code, ch.cern.eam.wshub.core.services.equipment.entities.Equipment equipment) {
        try {
            equipment.setCode(code);
            return ok(inforClient.getEquipmentFacadeService().updateEquipment(authentication.getInforContext(), equipment));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @DELETE
    @Path("/{code}")
    @Produces("application/json")
    @ApiOperation("Delete Equipment")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response deleteEquipment(@PathParam("code") String code) {
        try {
            return ok(inforClient.getEquipmentFacadeService().deleteEquipment(authentication.getInforContext(), code));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    //
    // BATCH PROCESSING
    //

    @POST
    @Path("/list/")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Create Multiple Equipment")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response createEquipmentList(List<ch.cern.eam.wshub.core.services.equipment.entities.Equipment> equipment) {
        try {
            return ok(inforClient.getEquipmentFacadeService().createEquipmentBatch(authentication.getInforContext(), equipment));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @GET
    @Path("/list/{equipmentcodes}")
    @Produces("application/json")
    @ApiOperation("Read Multiple Equipment")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response readEquipmentList(@PathParam("equipmentcodes") String equipmentCodes) {
        try {
            return ok(inforClient.getEquipmentFacadeService().readEquipmentBatch(authentication.getInforContext(), Arrays.asList(equipmentCodes.split(","))));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @PUT
    @Path("/list/")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Update Multiple Equipment")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response updateEquipmentList(List<ch.cern.eam.wshub.core.services.equipment.entities.Equipment> equipment) {
        try {
            return ok(inforClient.getEquipmentFacadeService().updateEquipmentBatch(authentication.getInforContext(), equipment));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @DELETE
    @Path("/list/")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Delete Multiple Equipment")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response deleteEquipmentList(List<String> equipmentCodes) {
        try {
            return ok(inforClient.getEquipmentFacadeService().deleteEquipmentBatch(authentication.getInforContext(), equipmentCodes));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @POST
    @Path("/warrantycoverage/")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Create Equipment Warranty Coverage")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response createEquipmentWarrantyCoverage(EquipmentWarranty equipmentWarranty) {
        try {
            return ok(inforClient.getEquipmentWarrantyCoverageService().createEquipmentWarrantyCoverage(authentication.getInforContext(), equipmentWarranty));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @PUT
    @Path("/warrantycoverage/")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Update Equipment Warranty Coverage")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response updateEquipmentWarrantyCoverage(EquipmentWarranty equipmentWarranty) {
        try {
            return ok(inforClient.getEquipmentWarrantyCoverageService().updateEquipmentWarrantyCoverage(authentication.getInforContext(), equipmentWarranty));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @PUT
    @Path("/code/")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Update Equipment Code")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response updateEquipmentCode(@QueryParam("currentCode") String currentCode, @QueryParam("newCode")  String newCode, @QueryParam("equipmentType") String equipmentType) {
        try {
            return ok(inforClient.getEquipmentOtherService().updateEquipmentCode(authentication.getInforContext(), currentCode, newCode, equipmentType));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

}
