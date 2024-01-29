package ch.cern.cmms.wshub.rest.controllers;

import ch.cern.cmms.wshub.rest.WSHubController;
import ch.cern.cmms.wshub.rest.annotations.ApiInforAuthentication;
import ch.cern.cmms.wshub.rest.annotations.ApiInforResponse;
import ch.cern.cmms.wshub.rest.tools.AuthenticationTools;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTOpBean;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTRow;
import ch.cern.eam.wshub.core.tools.InforException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/userdefinedscreens")
@Api(tags={"User Defined Screens"})
public class UserDefinedScreenController extends WSHubController {

    @Inject
    private AuthenticationTools authentication;

    @Inject
    private InforClient inforClient;

    @POST
    @Produces("application/json")
    @ApiOperation(value = "Create User Defined Screen Row", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response createUserDefinedScreenRow(UDTOpBean udtOpBean) throws InforException {
        try {
            return ok(inforClient.getUserDefinedScreenService().createUserDefinedScreenRow(authentication.getInforContext(), udtOpBean.getTableName(), udtOpBean.getRow()));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @PUT
    @Produces("application/json")
    @ApiOperation(value = "Update User Defined Screen Row", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response updateUserDefinedScreenRow(UDTOpBean udtOpBean) throws InforException {
        try {
            return ok(inforClient.getUserDefinedScreenService().updateUserDefinedScreenRow(authentication.getInforContext(), udtOpBean.getTableName(), udtOpBean.getRow(), udtOpBean.getWhereFilters()));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @DELETE
    @Produces("application/json")
    @ApiOperation(value = "Delete User Defined Screen Row", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response deleteUserDefinedScreenRow(UDTOpBean udtOpBean) throws InforException {
        try {
            return ok(inforClient.getUserDefinedScreenService().deleteUserDefinedScreenRow(authentication.getInforContext(), udtOpBean.getTableName(), udtOpBean.getRow()));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }




}
