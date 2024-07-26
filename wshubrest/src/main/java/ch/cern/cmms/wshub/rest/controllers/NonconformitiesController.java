package ch.cern.cmms.wshub.rest.controllers;

import ch.cern.cmms.wshub.rest.WSHubController;
import ch.cern.cmms.wshub.rest.annotations.ApiInforAuthentication;
import ch.cern.cmms.wshub.rest.annotations.ApiInforResponse;
import ch.cern.cmms.wshub.rest.tools.AuthenticationTools;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.equipment.entities.NonConformity;
import ch.cern.eam.wshub.core.tools.InforException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/ncrs")
@Api(tags = {"Non-conformities"})
public class NonconformitiesController extends WSHubController {

    @Inject
    private AuthenticationTools authentication;

    @Inject
    private InforClient inforClient;

    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation(value = "Create Non-conformity", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response createNonConformity(NonConformity nonconformity) {
        try {
            return ok(inforClient.getNonconformityService().createNonconformity(authentication.getInforContext(), nonconformity));
        } catch (InforException e) {
            return badRequest(e);
        } catch (Exception e) {
            return serverError(e);
        }
    }

    @GET
    @Path("/{code}")
    @Produces("application/json")
    @ApiOperation(value = "Read Non-conformity", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response readNonConformity(@PathParam("code") String code) {
        try {
            return ok(inforClient.getNonconformityService().readNonconformity(authentication.getInforContext(), code));
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
    @ApiOperation(value = "Update Non-conformity", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response updateNonconformity(@PathParam("code") String code, NonConformity nonconformity) {
        try {
            nonconformity.setCode(code);
            return ok(inforClient.getNonconformityService().updateNonconformity(authentication.getInforContext(), nonconformity));
        } catch (InforException e) {
            return badRequest(e);
        } catch (Exception e) {
            return serverError(e);
        }
    }

    @DELETE
    @Path("/{code}")
    @Produces("application/json")
    @ApiOperation(value = "Delete Non-conformity", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response deleteNonconformity(@PathParam("code") String code) {
        try {
            return ok(inforClient.getNonconformityService().deleteNonconformity(authentication.getInforContext(), code));
        } catch (InforException e) {
            return badRequest(e);
        } catch (Exception e) {
            return serverError(e);
        }
    }

}
