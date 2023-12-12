package ch.cern.cmms.wshub.rest.controllers;

import ch.cern.cmms.wshub.rest.WSHubController;
import ch.cern.cmms.wshub.rest.annotations.ApiInforAuthentication;
import ch.cern.cmms.wshub.rest.annotations.ApiInforResponse;
import ch.cern.cmms.wshub.rest.tools.AuthenticationTools;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.material.entities.Bin;
import ch.cern.eam.wshub.core.services.material.entities.IssueReturnPartTransaction;
import ch.cern.eam.wshub.core.tools.InforException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/issuereturnparttransaction")
@Api(tags={"Issue Return"})
public class IssueReturnPartTransactionController extends WSHubController {
    @Inject
    private AuthenticationTools authentication;

    @Inject
    private InforClient inforClient;

    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation(value = "Create Issue Return Part Transaction", authorizations = {@Authorization(value = "X-Auth-Token")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response createIssueReturnPartTransaction(IssueReturnPartTransaction issueReturnPartTransaction)  {
        try {
            return ok(inforClient.getPartMiscService().createIssueReturnTransaction(authentication.getInforContext(), issueReturnPartTransaction));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @POST
    @Path("/list/")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation(value = "Create Multiple Issue Return Part Transactions", authorizations = {@Authorization(value = "X-Auth-Token")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response createIssueReturnPartTransaction(List<IssueReturnPartTransaction> issueReturnPartTransactions)  {
        try {
            return ok(inforClient.getPartMiscService().createIssueReturnTransaction(authentication.getInforContext(), issueReturnPartTransactions));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }
}
