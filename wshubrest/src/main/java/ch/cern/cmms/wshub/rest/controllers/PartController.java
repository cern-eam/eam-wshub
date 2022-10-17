package ch.cern.cmms.wshub.rest.controllers;

import ch.cern.cmms.wshub.rest.WSHubController;
import ch.cern.cmms.wshub.rest.annotations.ApiInforAuthentication;
import ch.cern.cmms.wshub.rest.annotations.ApiInforResponse;
import ch.cern.cmms.wshub.rest.tools.AuthenticationTools;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.material.entities.Part;
import ch.cern.eam.wshub.core.tools.InforException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/parts")
@Api(tags={"Parts"})
public class PartController extends WSHubController {

    @Inject
    private AuthenticationTools authentication;

    @Inject
    private InforClient inforClient;

    @GET
    @Path("/{number}")
    @Produces("application/json")
    @ApiOperation("Read Part")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response readPart(@PathParam("number") String code) {
        try {
            return ok(inforClient.getPartService().readPart(authentication.getInforContext(), code));
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
    @ApiOperation("Update Part")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response updatePart(@PathParam("code") String code, Part part) {
        try {
            part.setCode(code);
            return ok(inforClient.getPartService().updatePart(authentication.getInforContext(), part));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Create Part")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response createPart(Part part) {
        try {
            return ok(inforClient.getPartService().createPart(authentication.getInforContext(), part));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @DELETE
    @Path("/{code}")
    @Produces("application/json")
    @ApiOperation("Delete Part")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response deletePart(@PathParam("code") String code) {
        try {
            return ok(inforClient.getPartService().deletePart(authentication.getInforContext(), code));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

}
