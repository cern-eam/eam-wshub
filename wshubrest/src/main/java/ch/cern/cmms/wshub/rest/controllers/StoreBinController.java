package ch.cern.cmms.wshub.rest.controllers;

import ch.cern.cmms.wshub.rest.WSHubController;
import ch.cern.cmms.wshub.rest.annotations.ApiInforAuthentication;
import ch.cern.cmms.wshub.rest.annotations.ApiInforResponse;
import ch.cern.cmms.wshub.rest.tools.AuthenticationTools;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.material.entities.Bin;
import ch.cern.eam.wshub.core.tools.InforException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/storebin")
@Api(tags={"Store Bin"})
public class StoreBinController extends WSHubController {

    @Inject
    private AuthenticationTools authentication;

    @Inject
    private InforClient inforClient;

    @POST
    @Path("/create")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Create Store Bin")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response createStoreBin(Bin bin)  {
        try {
            return ok(inforClient.getPartMiscService().addStoreBin(authentication.getInforContext(), bin));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @POST
    @Path("/read")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Read Store Bin")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response readStoreBin(Bin bin)  {
        try {
            return ok(inforClient.getPartMiscService().readStoreBin(authentication.getInforContext(), bin));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @PUT
    @Path("/update")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Update Store Bin")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response updateStoreBin(Bin bin)  {
        try {
            return ok(inforClient.getPartMiscService().updateStoreBin(authentication.getInforContext(), bin));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @DELETE
    @Path("/delete")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation("Delete Store Bin")
    @ApiInforAuthentication
    @ApiInforResponse
    public Response deleteStoreBin(Bin bin)  {
        try {
            return ok(inforClient.getPartMiscService().deleteStoreBin(authentication.getInforContext(), bin));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }
}