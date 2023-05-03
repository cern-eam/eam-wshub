package ch.cern.cmms.wshub.misc;

import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import ch.cern.eam.wshub.core.services.entities.WorkOrderPart;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestFilter;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.InforException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Date;

import static ch.cern.eam.wshub.core.tools.Tools.generateFault;
import static ch.cern.eam.wshub.core.tools.GridTools.isNotEmpty;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;

@RequestScoped
public class Gas {

    @Inject
    InforClient inforClient;

    public static final String WO_TYPE = "EX";
    public static final String WO_STATUS = "RADT";
    public static final String WO_STDWO = "CRFDGDG";
    public static final String WO_CLASS = "CRFDGDG";
    public static final String WO_ORDER_LINE_CF = "DFG18";

    public String createGasWorkOrder(InforContext inforContext, GasWorkOrder gasWorkOrder) throws InforException {

        validate(gasWorkOrder);

        WorkOrder workOrder = new WorkOrder();
        workOrder.setDescription(gasWorkOrder.getDescription());
        workOrder.setDepartmentCode(fetchDepartment(inforContext, gasWorkOrder.getSCEMCode(), gasWorkOrder.getSupplierRank()));
        workOrder.setTypeCode(WO_TYPE);

        if (isEmpty(gasWorkOrder.getWorkOrderStatus())) {
            workOrder.setStatusCode(WO_STATUS);
        } else {
            workOrder.setStatusCode(gasWorkOrder.getWorkOrderStatus());
        }

        workOrder.setEquipmentCode(gasWorkOrder.getGasEquipmentCode());
        workOrder.setLocationCode(gasWorkOrder.getGasPoint());
        workOrder.setStandardWO(WO_STDWO);
        workOrder.setReportedBy(gasWorkOrder.getRequestorCode());
        workOrder.setPriorityCode(gasWorkOrder.getPriority());
        workOrder.setReportedDate(new Date());
        workOrder.setRequestedEndDate(gasWorkOrder.getExpectedDeliveryDate());
        workOrder.setScheduledStartDate(gasWorkOrder.getExpectedDeliveryDate());
        workOrder.setScheduledEndDate(gasWorkOrder.getExpectedReturnDate());
        workOrder.setCostCode(gasWorkOrder.getBudgetCode());
        workOrder.setComment(gasWorkOrder.getComment());
        workOrder.setClassCode(WO_CLASS);
        if (gasWorkOrder.getOrderLine() != null) {
            workOrder.setCustomFields(new CustomField[1]);
            workOrder.getCustomFields()[0] = new CustomField();
            workOrder.getCustomFields()[0].setCode(WO_ORDER_LINE_CF);
            workOrder.getCustomFields()[0].setValue(gasWorkOrder.getOrderLine());
        }
        workOrder.setUserDefinedFields(new UserDefinedFields());
        workOrder.getUserDefinedFields().setUdfchar20(gasWorkOrder.getEdhDocumentNumber());
        workOrder.getUserDefinedFields().setUdfchar40(gasWorkOrder.getSCEMCode());
        workOrder.setTargetValue(gasWorkOrder.getTargetValue());

        String workOrderNumber = inforClient.getWorkOrderService().createWorkOrder(inforContext, workOrder);

        WorkOrderPart workOrderPart = new WorkOrderPart();
        workOrderPart.setPartCode(gasWorkOrder.getSCEMCode());
        workOrderPart.setPlannedQty(gasWorkOrder.getSCEMCodeQuantity());
        workOrderPart.setWorkOrderNumber(workOrderNumber);
        workOrderPart.setActivityCode("5");
        workOrderPart.setPlannedSource("false");
        inforClient.getWorkOrderMiscService().addWorkOrderPart(inforContext, workOrderPart);

        return workOrderNumber;
    }


    private String fetchDepartment(InforContext inforContext, String partCode, String preferredSupplier) throws InforException {
        GridRequest gridRequest = new GridRequest("WSCCON_CSP");
        gridRequest.setUserFunctionName("WSCCON");
        gridRequest.addParam("parameter.organization", "*");
        gridRequest.addParam("parameter.revision", "0");
        gridRequest.addFilter("udfnum01", preferredSupplier, "=", GridRequestFilter.JOINER.AND);
        gridRequest.addFilter("code", partCode, "=");

        gridRequest.getParams().put("parameter.customercontractcode", "XA5A");
        if (isNotEmpty(inforClient.getGridsService().executeQuery(inforContext, gridRequest))) {
            return "XA5A";
        }

        gridRequest.getParams().put("parameter.customercontractcode", "XAP7");
        if (isNotEmpty(inforClient.getGridsService().executeQuery(inforContext, gridRequest))) {
            return "XAP7";
        }

        gridRequest.getParams().put("parameter.customercontractcode", "XA0B");
        if (isNotEmpty(inforClient.getGridsService().executeQuery(inforContext, gridRequest))) {
            return "XA0B";
        }

        throw generateFault("The preferred supplier couldn't be fetched.");

    }

    private void validate(GasWorkOrder gasWorkOrder) throws InforException {
        if (isEmpty(gasWorkOrder.getSupplierRank())) {
            throw generateFault("Supplier Rank can not be empty");
        }
        //TODO further validations
    }

}
