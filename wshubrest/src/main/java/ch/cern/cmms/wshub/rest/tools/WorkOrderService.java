package ch.cern.cmms.wshub.rest.tools;

import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.InforException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

@RequestScoped
public class WorkOrderService {
    @Inject
    private InforClient inforClient;

    @Transactional
    public String createWorkOrder(WorkOrder workOrder, InforContext inforContext) throws InforException {
        return inforClient.getWorkOrderService().createWorkOrder(inforContext, workOrder);
    }
}
