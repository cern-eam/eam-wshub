package ch.cern.cmms.wshub.rest.controllers;

import ch.cern.cmms.wshub.rest.WSHubController;
import ch.cern.cmms.wshub.rest.annotations.ApiInforAuthentication;
import ch.cern.cmms.wshub.rest.annotations.ApiInforResponse;
import ch.cern.cmms.wshub.rest.tools.AuthenticationTools;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.InforException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/workorders")
@Api(tags={"Work Orders"})
public class WorkOrderController extends WSHubController {

    @Inject
    private AuthenticationTools authentication;

    @Inject
    private InforClient inforClient;

    @GET
    @Path("/{number}")
    @Produces("application/json")
    @ApiOperation(value = "Read Work Order", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response readWorkOrder(@PathParam("number") String number) {
        try {
            return ok(inforClient.getWorkOrderService().readWorkOrder(authentication.getInforContext(), number));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @PUT
    @Path("/{number}")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation(value = "Update Work Order", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response updateWorkOrder(@PathParam("number") String number, WorkOrder workOrder) {
        try {
            workOrder.setNumber(number);
            return ok(inforClient.getWorkOrderService().updateWorkOrder(authentication.getInforContext(), workOrder));
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
    @ApiOperation(value = "Create Work Order", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response createWorkOrder(WorkOrder workOrder) {
        try {
            return ok(inforClient.getWorkOrderService().createWorkOrder(authentication.getInforContext(), workOrder));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }


    @DELETE
    @Path("/{number}")
    @Produces("application/json")
    @ApiOperation(value = "Delete Work Order", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response deleteWorkOrder(@PathParam("number") String number) {
        try {
            return ok(inforClient.getWorkOrderService().deleteWorkOrder(authentication.getInforContext(), number));
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
    @ApiOperation(value = "Create Multiple Work Orders", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response createWorkOrderList(List<WorkOrder> workOrders) {
        try {
            return ok(inforClient.getWorkOrderService().createWorkOrderBatch(authentication.getInforContext(), workOrders));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @GET
    @Path("/list/{workorders}")
    @Produces("application/json")
    @ApiOperation(value = "Read Multiple Work Orders", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response readWorkOrderList(@PathParam("workorders") String workOrders) {
        try {
            return ok(inforClient.getWorkOrderService().readWorkOrderBatch(authentication.getInforContext(), Arrays.asList(workOrders.split(","))));
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
    @ApiOperation(value = "Update Multiple Work Orders", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response updateWorkOrderList(List<WorkOrder> workOrders) {
        try {
            return ok(inforClient.getWorkOrderService().updateWorkOrderBatch(authentication.getInforContext(), workOrders));
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
    @ApiOperation(value = "Delete Multiple Work Orders", authorizations = {@Authorization(value = "basicAuth")})
    @ApiInforAuthentication
    @ApiInforResponse
    public Response deleteWorkOrderList(List<String> workOrders) {
        try {
            return ok(inforClient.getWorkOrderService().deleteWorkOrderBatch(authentication.getInforContext(), workOrders));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

}
