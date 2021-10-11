package ch.cern.cmms.wshub.rest.controllers;

import ch.cern.cmms.wshub.rest.WSHubController;
import ch.cern.cmms.wshub.tools.ApplicationData;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.InforException;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@ManagedBean
@Path("/services")
public class Monitoring extends WSHubController {

    @Inject
    private InforClient inforClient;
    @Inject
    private ApplicationData applicationData;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH-mm-ss", Locale.ENGLISH);

    private String EQUIPMENT_CODE = "CR-MONITORING";
    private String PART_CODE = "";
    private String WORKORDER_CODE = "25316908";

    @GET
    @Path("/monitoring")
    @Produces("application/json")
    public Response monitor() {

        Credentials credentials = new Credentials();
        credentials.setUsername("R5");
        credentials.setPassword(applicationData.getPassphrase());
        InforContext inforContext = inforClient.getTools().getInforContext(credentials);
        Boolean success = true;
        success = updateEquipment(inforContext);
        success = updateWorkOrder(inforContext);
        if (success) {
            return ok("OK");
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    private boolean updateWorkOrder(InforContext inforContext) {
        try {
            WorkOrder workOrder = new WorkOrder();
            workOrder.setNumber(WORKORDER_CODE);
            workOrder.setDescription("MONITORING WO / " + getCurrentDate());
            inforClient.getWorkOrderService().updateWorkOrder(inforContext, workOrder);
            return true;
        } catch (InforException inforException ) {
            return false;
        }
    }


    private boolean updateEquipment(InforContext inforContext) {
        try {
            Equipment equipment = new Equipment();
            equipment.setCode(EQUIPMENT_CODE);
            equipment.setDescription("MONITORING ASSET / " + getCurrentDate());
            inforClient.getEquipmentFacadeService().updateEquipment(inforContext, equipment);
            return true;
        } catch (InforException inforException ) {
            return false;
        }
    }

    private String getCurrentDate() {
       return simpleDateFormat.format(new Date()).toString();
    }


}
