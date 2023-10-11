package ch.cern.cmms.wshub.beans;

import ch.cern.cmms.wshub.entities.AsyncExecution;
import ch.cern.cmms.wshub.equipment.EquipmentHierarchy;
import ch.cern.cmms.wshub.equipment.entities.EquipmentGraphRequest;
import ch.cern.cmms.wshub.equipment.entities.Graph;
import ch.cern.cmms.wshub.misc.Gas;
import ch.cern.cmms.wshub.misc.GasWorkOrder;
import ch.cern.cmms.wshub.misc.Misc;
import ch.cern.cmms.wshub.userdefinedscreens.EquipmentDependencies;
import ch.cern.cmms.wshub.userdefinedscreens.UDTService;
import ch.cern.cmms.wshub.userdefinedscreens.entities.EquipmentDependency;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.administration.entities.MenuSpecification;
import ch.cern.eam.wshub.core.services.comments.entities.Comment;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.administration.entities.EAMUser;
import ch.cern.eam.wshub.core.services.entities.WorkOrderPart;
import ch.cern.eam.wshub.core.services.equipment.entities.*;
import ch.cern.eam.wshub.core.services.grids.entities.GridDDSpyFieldsResult;
import ch.cern.eam.wshub.core.services.grids.entities.GridDataspy;
import ch.cern.eam.wshub.core.services.grids.entities.GridMetadataRequestResult;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.material.entities.Bin;
import ch.cern.eam.wshub.core.services.material.entities.BuildKitParam;
import ch.cern.eam.wshub.core.services.material.entities.IssueReturnPartTransaction;
import ch.cern.eam.wshub.core.services.material.entities.Lot;
import ch.cern.eam.wshub.core.services.material.entities.MaterialList;
import ch.cern.eam.wshub.core.services.material.entities.Part;
import ch.cern.eam.wshub.core.services.material.entities.PartAssociation;
import ch.cern.eam.wshub.core.services.material.entities.PartKitTemplate;
import ch.cern.eam.wshub.core.services.material.entities.PartManufacturer;
import ch.cern.eam.wshub.core.services.material.entities.PartStock;
import ch.cern.eam.wshub.core.services.material.entities.PartStore;
import ch.cern.eam.wshub.core.services.material.entities.PartSubstitute;
import ch.cern.eam.wshub.core.services.material.entities.PartSupplier;
import ch.cern.eam.wshub.core.services.material.entities.PickTicket;
import ch.cern.eam.wshub.core.services.material.entities.PickTicketPart;
import ch.cern.eam.wshub.core.services.material.entities.PurchaseOrder;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTOpBean;
import ch.cern.eam.wshub.core.services.workorders.entities.*;
import ch.cern.eam.wshub.core.tools.InforException;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.ExcludeClassInterceptors;
import javax.jws.HandlerChain;
import javax.jws.WebParam;
import javax.jws.WebService;


@Stateless(name = "WSHub")
@TransactionAttribute(TransactionAttributeType.NEVER)
@HandlerChain(file = "/handlers.xml")
@WebService(name = "InforWSPortType", portName = "InforWSPort", serviceName = "InforWSService", targetNamespace = "http://cern.ch/cmms/infor/wshub", endpointInterface = "ch.cern.cmms.wshub.beans.WSHub")
public class WSHubBean implements WSHub {

	@Inject
	private EquipmentHierarchy equipmentHierarchy;
	@Inject
	private EquipmentDependencies userDefinedScreens;
	@Inject
	private MessageSender messageSender;
    @Inject
	InforClient inforClient;
    @Inject
    private UDTService udtService;
    @Inject
	private Gas gas;
    @Inject
	private Misc misc;

	//
	// WORK ORDERS CRUD
	//
	public WorkOrder readWorkOrder(String number, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getWorkOrderService().readWorkOrder(inforClient.getTools().getInforContext(credentials, sessionID), number);
	}

	public String createWorkOrder(WorkOrder workOrderParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getWorkOrderService().createWorkOrder(inforClient.getTools().getInforContext(credentials, sessionID), workOrderParam);
	}

	public BatchResponse<String> createWorkOrderBatch(List<WorkOrder> workOrderParam, Credentials credentials, String sessionID) throws InforException  {
		return inforClient.getWorkOrderService().createWorkOrderBatch(inforClient.getTools().getInforContext(credentials, sessionID), workOrderParam);
	}

	public BatchResponse<WorkOrder> readWorkOrderBatch(List<String> numbers, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getWorkOrderService().readWorkOrderBatch(inforClient.getTools().getInforContext(credentials, sessionID), numbers);
	}

	public BatchResponse<String> updateWorkOrderBatch(List<WorkOrder> workOrderParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getWorkOrderService().updateWorkOrderBatch(inforClient.getTools().getInforContext(credentials, sessionID), workOrderParam);
	}

	public String updateWorkOrder(WorkOrder workorderParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getWorkOrderService().updateWorkOrder(inforClient.getTools().getInforContext(credentials, sessionID), workorderParam);
	}

	public String deleteWorkOrder(String workOrderNumber, Credentials credentials, String sessionID)throws InforException {
		return inforClient.getWorkOrderService().deleteWorkOrder(inforClient.getTools().getInforContext(credentials, sessionID), workOrderNumber);
	}

	public String updateWOStatus(WorkOrder workOrder, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getWorkOrderService().updateWorkOrderStatus(inforClient.getTools().getInforContext(credentials, sessionID), workOrder.getNumber(), workOrder.getStatusCode());
	}

	//
	// STANDARD WORK ORDERS
	//
	public StandardWorkOrder readStandardWorkOrder(String number, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getStandardWorkOrderService().readStandardWorkOrder(inforClient.getTools().getInforContext(credentials, sessionID), number);
	}

	public String createStandardWorkOrder(StandardWorkOrder standardWorkOrder, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getStandardWorkOrderService().createStandardWorkOrder(inforClient.getTools().getInforContext(credentials, sessionID), standardWorkOrder);
	}

	public String updateStandardWorkOrder(StandardWorkOrder standardWorkOrder, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getStandardWorkOrderService().updateStandardWorkOrder(inforClient.getTools().getInforContext(credentials, sessionID), standardWorkOrder);
	}

	//
	// ACTIVITIES
	//
	public String createActivity(Activity activityParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getLaborBookingService().createActivity(inforClient.getTools().getInforContext(credentials, sessionID), activityParam);
	}

	public String updateActivity(Activity activityParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getLaborBookingService().updateActivity(inforClient.getTools().getInforContext(credentials, sessionID), activityParam);
	}

	public Activity[] readActivities(String workOrderNumber, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getLaborBookingService().readActivities(inforClient.getTools().getInforContext(credentials, sessionID), workOrderNumber, true);
	}

	//
	// LABOR BOOKING
	//
	public List<LaborBooking> readBookedLabor(String workOrderNumber, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getLaborBookingService().readLaborBookings(inforClient.getTools().getInforContext(credentials, sessionID), workOrderNumber);
	}

	public String createLaborBooking(LaborBooking laborBookingParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getLaborBookingService().createLaborBooking(inforClient.getTools().getInforContext(credentials, sessionID), laborBookingParam);
	}

	//
	// PURCHASE ORDERS
	//
	public String updatePurchaseOrder(PurchaseOrder purchaseOrderParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPurchaseOrdersService().updatePurchaseOrder(inforClient.getTools().getInforContext(credentials, sessionID), purchaseOrderParam);
	}

	//
	// CASE MANAGEMENT
	//
	public InforCase readCase(String caseID, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getCaseService().readCase(inforClient.getTools().getInforContext(credentials, sessionID), caseID);
	}

	public String createCase(InforCase caseMT, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getCaseService().createCase(inforClient.getTools().getInforContext(credentials, sessionID), caseMT);
	}

	public String updateCase(InforCase caseMT, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getCaseService().updateCase(inforClient.getTools().getInforContext(credentials, sessionID), caseMT);
	}

	public String deleteCase(String caseID, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getCaseService().deleteCase(inforClient.getTools().getInforContext(credentials, sessionID), caseID);
	}

	//
	// CASE TASK MANAGEMENT
	//
	public InforCaseTask readCaseTask(String caseTaskID, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getCaseTaskService().readCaseTask(inforClient.getTools().getInforContext(credentials, sessionID), caseTaskID);
	}

	public String createCaseTask(InforCaseTask caseTaskMT, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getCaseTaskService().createCaseTask(inforClient.getTools().getInforContext(credentials, sessionID), caseTaskMT);
	}

	public String updateCaseTask(InforCaseTask caseTaskMT, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getCaseTaskService().updateCaseTask(inforClient.getTools().getInforContext(credentials, sessionID), caseTaskMT);
	}

	public String deleteCaseTask(String caseTaskID, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getCaseTaskService().deleteCaseTask(inforClient.getTools().getInforContext(credentials, sessionID), caseTaskID);
	}

	//
	// WORK ORDERS MISC
	//
	public String createMeterReading(MeterReading meterReading, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getWorkOrderMiscService().createMeterReading(inforClient.getTools().getInforContext(credentials, sessionID), meterReading);
	}

	public String createWorkOrderAdditionalCost( WorkOrderAdditionalCosts workOrderAddCostsParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getWorkOrderMiscService().createWorkOrderAdditionalCost(inforClient.getTools().getInforContext(credentials, sessionID), workOrderAddCostsParam);
	}

	public String addWorkOrderPart(WorkOrderPart workOrderPart, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getWorkOrderMiscService().addWorkOrderPart(inforClient.getTools().getInforContext(credentials, sessionID), workOrderPart);
	}

	public String createMatarialList(MaterialList materialList, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getWorkOrderMiscService().createMaterialList(inforClient.getTools().getInforContext(credentials, sessionID), materialList);
	}

	public String createTaskplanChecklist(TaskplanCheckList taskChecklist, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getChecklistService().createTaskplanChecklist(inforClient.getTools().getInforContext(credentials, sessionID), taskChecklist);
	}

	public String createTaskPlan(TaskPlan taskPlan, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getWorkOrderMiscService().createTaskPlan(inforClient.getTools().getInforContext(credentials, sessionID), taskPlan);
	}

	public WorkOrderActivityCheckList[] readWOActivityChecklists(Activity activity, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getChecklistService().readWorkOrderChecklists(inforClient.getTools().getInforContext(credentials, sessionID), activity);
	}

	public String updateWorkOrderChecklists(WorkOrderActivityCheckList WorkOrderChecklist, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getChecklistService().updateWorkOrderChecklist(inforClient.getTools().getInforContext(credentials, sessionID), WorkOrderChecklist);
	}

	public String createRouteEquipment(RouteEquipment routeEquipment, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getWorkOrderMiscService().createRouteEquipment(inforClient.getTools().getInforContext(credentials, sessionID), routeEquipment);
	}

	public String deleteRouteEquipment(RouteEquipment routeEquipment, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getWorkOrderMiscService().deleteRouteEquipment(inforClient.getTools().getInforContext(credentials, sessionID), routeEquipment);
	}

	public String createRoute(Route route, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getRouteService().createRoute(inforClient.getTools().getInforContext(credentials, sessionID), route);
	}

	public String createSalesPrice(SalesPrice salesPrice, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getSalesPriceService().createSalesPrice(inforClient.getTools().getInforContext(credentials, sessionID), salesPrice);
	}

	//
	// InspectionService
	//
	public String createAspect(Aspect aspect, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getInspectionService().addAspect(inforClient.getTools().getInforContext(credentials, sessionID), aspect);
	}

	//
	// COMMENTS
	//
	public String createComment(Comment commentParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getCommentService().createComment(inforClient.getTools().getInforContext(credentials, sessionID), commentParam);
	}

	public String updateComment(Comment commentParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getCommentService().updateComment(inforClient.getTools().getInforContext(credentials, sessionID), commentParam);
	}

	public String deleteComment(Comment commentParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getCommentService().deleteComment(inforClient.getTools().getInforContext(credentials, sessionID), commentParam);
	}

	public Comment[] readComments(Comment commentParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getCommentService().readComments(inforClient.getTools().getInforContext(credentials, sessionID), commentParam.getEntityCode(), commentParam.getEntityKeyCode(), commentParam.getTypeCode());
	}

	//
	// EQUIPMENT - CRUD
	//
	public String updateEquipment(Equipment equipmentParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEquipmentFacadeService().updateEquipment(inforClient.getTools().getInforContext(credentials, sessionID), equipmentParam);
	}

	public String createEquipment(Equipment equipmentParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEquipmentFacadeService().createEquipment(inforClient.getTools().getInforContext(credentials, sessionID), equipmentParam);
	}

	public Equipment readEquipment(String equipmentCode, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEquipmentFacadeService().readEquipment(inforClient.getTools().getInforContext(credentials, sessionID), equipmentCode);
	}

	public String deleteEquipment(String equipmentCode, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEquipmentFacadeService().deleteEquipment(inforClient.getTools().getInforContext(credentials, sessionID), equipmentCode);
	}

	public String updateEquipmentCode(String currentCode, String newCode, String equipmentType, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEquipmentOtherService().updateEquipmentCode(inforClient.getTools().getInforContext(credentials, sessionID), currentCode, newCode, equipmentType);
	}

	public BatchResponse<String> createEquipmentBatch(List<Equipment> equipmentList, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEquipmentFacadeService().createEquipmentBatch(inforClient.getTools().getInforContext(credentials, sessionID), equipmentList);
	}

	public BatchResponse<Equipment> readEquipmentBatch(List<String> numbers, Credentials credentials, String sessionID) {
		return inforClient.getEquipmentFacadeService().readEquipmentBatch(inforClient.getTools().getInforContext(credentials, sessionID), numbers);
	}

	public BatchResponse<String> updateEquipmentBatch(List<Equipment> equipmentList, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEquipmentFacadeService().updateEquipmentBatch(inforClient.getTools().getInforContext(credentials, sessionID), equipmentList);
	}

	public Location readLocation(String equipmentCode, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getLocationService().readLocation(inforClient.getTools().getInforContext(credentials, sessionID), equipmentCode);
	}

	public String hideEquipment(String equipmentCode, Credentials credentials, String sessionID) throws InforException {
		Credentials adminCredentials = new Credentials("R5", credentials.getPassword());
		return misc.hideEquipment(inforClient.getTools().getInforContext(adminCredentials, sessionID), equipmentCode);
	}

	//
	// EQUIPMENT LINEAR REFERENCES
	//
	public String createEquipmentLinearReference(LinearReference linearReference, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getLinearReferenceService().createEquipmentLinearReference(inforClient.getTools().getInforContext(credentials, sessionID), linearReference);
	}

	public String updateEquipmentLinearReference(LinearReference linearReference, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getLinearReferenceService().updateEquipmentLinearReference(inforClient.getTools().getInforContext(credentials, sessionID), linearReference);
	}

	public String deleteEquipmentLinearReference(String linearReferenceID, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getLinearReferenceService().deleteEquipmentLinearReference(inforClient.getTools().getInforContext(credentials, sessionID), linearReferenceID);
	}

	//
	// EQUIPMENT STRUCTURE
	//
	public String addEquipmentToStructure(EquipmentStructure equipmentStructure, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEquipmentStructureService().addEquipmentToStructure(inforClient.getTools().getInforContext(credentials, sessionID), equipmentStructure);
	}

	public String updateEquipmentStructure(EquipmentStructure equipmentStructure, Credentials credentials,
			String sessionID) throws InforException {
		return inforClient.getEquipmentStructureService().updateEquipmentStructure(inforClient.getTools().getInforContext(credentials, sessionID), equipmentStructure);
	}

	public String removeEquipmentFromStructure(EquipmentStructure equipmentStructure, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEquipmentStructureService().removeEquipmentFromStructure(inforClient.getTools().getInforContext(credentials, sessionID), equipmentStructure);
	}

	//
	// EQUIPMENT WARRANTY COVERAGE
	//
	public String createEquipmentWarrantyCoverage(EquipmentWarranty equipmentWarrantyParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEquipmentWarrantyCoverageService().createEquipmentWarrantyCoverage(inforClient.getTools().getInforContext(credentials, sessionID), equipmentWarrantyParam);
	}

	public String updateEquipmentWarrantyCoverage(EquipmentWarranty equipmentWarrantyParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEquipmentWarrantyCoverageService().updateEquipmentWarrantyCoverage(inforClient.getTools().getInforContext(credentials, sessionID), equipmentWarrantyParam);
	}

	//
	// EQUIPMENT PM SCHEDULES
	//
	public String createEquipmentPMSchedule(EquipmentPMSchedule pmSchedule, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPmScheduleService().createEquipmentPMSchedule(inforClient.getTools().getInforContext(credentials, sessionID), pmSchedule);
	}

	public String deleteEquipmentPMSchedule(EquipmentPMSchedule pmSchedule, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPmScheduleService().deleteEquipmentPMSchedule(inforClient.getTools().getInforContext(credentials, sessionID), pmSchedule);
	}

	public String updateEquipmentPMSchedule(EquipmentPMSchedule pmSchedule, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPmScheduleService().updateEquipmentPMSchedule(inforClient.getTools().getInforContext(credentials, sessionID), pmSchedule);
	}

	public String updateReleasedPMSchedule(ReleasedPMSchedule releasedPMSchedule, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPmScheduleService().updateReleasedPMSchedule(inforClient.getTools().getInforContext(credentials, sessionID), releasedPMSchedule);
	}

	//
	// EQUIPMENT DEPRECIATION
	//
	public String createEquipmentDepreciation(EquipmentDepreciation equipmentDepreciation, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEquipmentOtherService().createEquipmentDepreciation(inforClient.getTools().getInforContext(credentials, sessionID), equipmentDepreciation);
	}

	public String updateEquipmentDepreciation(EquipmentDepreciation equipmentDepreciation, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEquipmentOtherService().updateEquipmentDepreciation(inforClient.getTools().getInforContext(credentials, sessionID), equipmentDepreciation);
	}

	public Graph readEquipmentGraph(EquipmentGraphRequest graph, Credentials credentials, String sessionID)
			throws InforException {
		return equipmentHierarchy.readEquipmentGraph(graph, credentials, sessionID);
	}

	public String createEquipmentCampaign(EquipmentCampaign equipmentCampaign, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEquipmentOtherService().createEquipmentCampaign(inforClient.getTools().getInforContext(credentials, sessionID), equipmentCampaign);
	}

	//
	// MATERIAL - CRUD
	//
	public String createPart(Part partParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartService().createPart(inforClient.getTools().getInforContext(credentials, sessionID), partParam);
	}

	public String updatePart(Part partParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartService().updatePart(inforClient.getTools().getInforContext(credentials, sessionID), partParam);
	}

	public Part readPart(String partCode, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartService().readPart(inforClient.getTools().getInforContext(credentials, sessionID), partCode);
	}

	public String deletePart(String partCode, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartService().deletePart(inforClient.getTools().getInforContext(credentials, sessionID), partCode);
	}

	public String createIssueReturnPartTransaction(IssueReturnPartTransaction issueReturnPartTransaction, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartMiscService().createIssueReturnTransaction(inforClient.getTools().getInforContext(credentials, sessionID), issueReturnPartTransaction);
	}

	public String addPartStore(PartStore partStoreParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartStoreService().addPartStore(inforClient.getTools().getInforContext(credentials, sessionID), partStoreParam);
	}

	public String updatePartStore(PartStore partStoreParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartStoreService().updatePartStore(inforClient.getTools().getInforContext(credentials, sessionID), partStoreParam);
	}

	public String addPartSupplier(PartSupplier partSupplierParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartMiscService().addPartSupplier(inforClient.getTools().getInforContext(credentials, sessionID), partSupplierParam);
	}

	public String addPartStock(PartStock partStockParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartBinStockService().addPartStock(inforClient.getTools().getInforContext(credentials, sessionID), partStockParam);
	}

	public String updatePartStock(PartStock partStockParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartBinStockService().updatePartStock(inforClient.getTools().getInforContext(credentials, sessionID), partStockParam);
	}

	//
	// KIT
	//
	public String addPartKitTemplate(PartKitTemplate partKitParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartKitService().addPartKitTemplate(inforClient.getTools().getInforContext(credentials, sessionID),partKitParam);
	}

	public String createKitSession(BuildKitParam partKitParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartKitService().createKitSession(inforClient.getTools().getInforContext(credentials, sessionID),partKitParam);
	}

	public String buildKit(String kitSessionId, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartKitService().buildKit(inforClient.getTools().getInforContext(credentials, sessionID),kitSessionId);
	}

	//
	// PART MANUFACTURER
	//
	public PartManufacturer[] readPartManufacturers(String partCode, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartMiscService().getPartManufacturers(inforClient.getTools().getInforContext(credentials, sessionID), partCode);
	}

	public String addPartManufacturer(PartManufacturer partManufacturerParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartManufacturerService().addPartManufacturer(inforClient.getTools().getInforContext(credentials, sessionID), partManufacturerParam);
	}

	public String updatePartManufacturer(PartManufacturer partManufacturerParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartManufacturerService().updatePartManufacturer(inforClient.getTools().getInforContext(credentials, sessionID), partManufacturerParam);
	}

	public String deletePartManufacturer(PartManufacturer partManufacturerParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartManufacturerService().deletePartManufacturer(inforClient.getTools().getInforContext(credentials, sessionID), partManufacturerParam);
	}

	//
	// PART ASSOCIATION
	//
	public String createPartAssociation(PartAssociation partAssociation, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartMiscService().createPartAssociation(inforClient.getTools().getInforContext(credentials, sessionID), partAssociation);
	}

	public String deletePartAssociation(PartAssociation partAssociation, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartMiscService().deletePartAssociation(inforClient.getTools().getInforContext(credentials, sessionID), partAssociation);
	}

	public String createPartSubstitute(PartSubstitute partSubstitute, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartMiscService().createPartSubstitute(inforClient.getTools().getInforContext(credentials, sessionID), partSubstitute);
	}

	public String updateEquipmentReservation(EquipmentReservation equipmentReservation, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEquipmentReservationService().updateEquipmentReservation(inforClient.getTools().getInforContext(credentials, sessionID), equipmentReservation);
	}

	public String createStoreBin(Bin bin, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartMiscService().addStoreBin(inforClient.getTools().getInforContext(credentials, sessionID), bin);
	}

	public Bin readStoreBin(Bin bin, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartMiscService().readStoreBin(inforClient.getTools().getInforContext(credentials, sessionID), bin);
	}

	public String updateStoreBin(Bin bin, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartMiscService().updateStoreBin(inforClient.getTools().getInforContext(credentials, sessionID), bin);
	}

	public String deleteStoreBin(Bin bin, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartMiscService().deleteStoreBin(inforClient.getTools().getInforContext(credentials, sessionID), bin);
	}

	public String createLot(Lot lot, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPartLotService().createLot(inforClient.getTools().getInforContext(credentials, sessionID), lot);
	}

	//
	// Async Execution
	//
	@ExcludeClassInterceptors
	public String executeAsync(AsyncExecution asyncExecution, Credentials credentials, String sessionID)
			throws InforException {
		return messageSender.executeAsync(asyncExecution, credentials, sessionID);
	}

	//
	//
	//
	public String login(String data, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getUserSetupService().login(inforClient.getTools().getInforContext(credentials, sessionID), "NONULL");
	}

	public CustomField[] readMTCustomFields(String entity, String inforClass, Credentials credentials, String sessionID)
			throws InforException {
		return inforClient.getTools().getCustomFieldsTools().getWSHubCustomFields(inforClient.getTools().getInforContext(credentials, sessionID), entity, inforClass);
	}

	public EAMUser readEAMUser(String userCode, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getUserSetupService().readUserSetup(inforClient.getTools().getInforContext(credentials, sessionID), userCode);
	}

	public String createEAMUser(EAMUser userCode, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getUserSetupService().createUserSetup(inforClient.getTools().getInforContext(credentials, sessionID), userCode);
	}

	public String updateEAMUser(EAMUser userCode, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getUserSetupService().updateUserSetup(inforClient.getTools().getInforContext(credentials, sessionID), userCode);
	}

	//
	// GRIDS
	//
	public GridRequestResult getGridResult(GridRequest gridRequest, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getGridsService().executeQuery(inforClient.getTools().getInforContext(credentials, sessionID), gridRequest);
	}

	public GridMetadataRequestResult getGridMetadata(String gridCode, String viewType, String language, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getGridsService().getGridMetadata(inforClient.getTools().getInforContext(credentials, sessionID), gridCode, viewType, language);
	}

	public GridDataspy getDefaultDataspy(String gridCode, String viewType, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getGridsService().getDefaultDataspy(inforClient.getTools().getInforContext(credentials, sessionID),gridCode, viewType);
	}

	public GridDDSpyFieldsResult getDDspyFields(String gridCode, String viewType, String ddSpyId, String language, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getGridsService().getDDspyFields(inforClient.getTools().getInforContext(credentials, sessionID), gridCode, viewType, ddSpyId, language);
	}

	//
	// User Defined Screens
	//
	public String createEquipmentDependency(EquipmentDependency equipmentDependency, Credentials credentials,
			String sessionID) throws InforException {
		return userDefinedScreens.createEquipmentDependency(equipmentDependency, credentials, sessionID);
	}

	public String updateEquipmentDependency(EquipmentDependency equipmentDependency, Credentials credentials,
			String sessionID) throws InforException {
		return userDefinedScreens.updateEquipmentDependency(equipmentDependency, credentials, sessionID);
	}

	public String deleteEquipmentDependency(EquipmentDependency equipmentDependency, Credentials credentials,
			String sessionID) throws InforException {
		return userDefinedScreens.deleteEquipmentDependency(equipmentDependency, credentials, sessionID);
	}

	//
	// USER SETUP
	//
	public EAMUser readUserSetup(String userCode, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getUserSetupService().readUserSetup(inforClient.getTools().getInforContext(credentials, sessionID), userCode);
	}

	public String createUserSetup(EAMUser user, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getUserSetupService().createUserSetup(inforClient.getTools().getInforContext(credentials, sessionID), user);
	}

	public String updateUserSetup(EAMUser user, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getUserSetupService().updateUserSetup(inforClient.getTools().getInforContext(credentials, sessionID), user);
	}

	public String deleteUserSetup(String userCode, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getUserSetupService().deleteUserSetup(inforClient.getTools().getInforContext(credentials, sessionID), userCode);
	}

	//
	// EMPLOYEES
	//
	public Employee readEmployee(String employeeCode, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEmployeeService().readEmployee(inforClient.getTools().getInforContext(credentials, sessionID), employeeCode);
	}

	public String createEmployee(Employee employee, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEmployeeService().createEmployee(inforClient.getTools().getInforContext(credentials, sessionID), employee);
	}

	public String updateEmployee(Employee employee, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEmployeeService().updateEmployee(inforClient.getTools().getInforContext(credentials, sessionID), employee);
	}

	public String deleteEmployee(String employeeCode, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getEmployeeService().deleteEmployee(inforClient.getTools().getInforContext(credentials, sessionID), employeeCode);
	}


	//
	// PICK TICKET
	//
	public String createPickTicket(PickTicket pickTicketParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPickTicketService().createPickTicket(inforClient.getTools().getInforContext(credentials, sessionID), pickTicketParam);
	}

	//
	// PICK TICKET - working as a PATCH method
	//
	public String updatePickTicket(PickTicket pickTicketParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPickTicketService().updatePickTicket(inforClient.getTools().getInforContext(credentials, sessionID), pickTicketParam);
	}

	public String addPartToPickList(PickTicketPart pickTicketPartParam, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getPickTicketService().addPartToPickTicket(inforClient.getTools().getInforContext(credentials, sessionID), pickTicketPartParam);
	}

	//
	// USER DEFINED SCREEN
	//
	public String createUserDefinedTableRows(UDTOpBean udtOpBean, Credentials credentials, String sessionID) throws InforException {
		return udtService.createUserDefinedTableRows(
            udtOpBean,
            inforClient.getTools().getInforContext(credentials, sessionID)
		);
	}

	public String deleteUserDefinedTableRows(UDTOpBean udtOpBean,  Credentials credentials, String sessionID) throws InforException {
		return udtService.deleteUserDefinedTableRows(
				udtOpBean,
                inforClient.getTools().getInforContext(credentials, sessionID)
		);
	}

	public String updateUserDefinedTableRows(UDTOpBean udtOpBean, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getUserDefinedScreenService().updateUserDefinedScreenRow(inforClient.getTools().getInforContext(credentials, sessionID), udtOpBean.getTableName(), udtOpBean.getRow(), udtOpBean.getWhereFilters());
	}

	//
	// GAS
	//
	public String createGasWorkOrder(GasWorkOrder gasWorkOrder, Credentials credentials, String sessionID) throws InforException {
		return gas.createGasWorkOrder(inforClient.getTools().getInforContext(credentials, sessionID), gasWorkOrder);
	}

	//
	// INFOR EXTENDED MENU HIERARCHY
	//
	public String addToMenuHierarchy(MenuSpecification ms, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getUserGroupMenuService().addToMenuHierarchy(inforClient.getTools().getInforContext(credentials, sessionID), ms);
	}

	public BatchResponse<String> addToMenuHierarchyBatch(List<MenuSpecification> menuSpecificationList, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getUserGroupMenuService().addToMenuHierarchyBatch(inforClient.getTools().getInforContext(credentials, sessionID), menuSpecificationList);
	}

	public BatchResponse<String> addToMenuHierarchyManyUsergroups(List<String> userGroups, MenuSpecification menuSpecification, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getUserGroupMenuService().addToMenuHierarchyManyUsergroups(inforClient.getTools().getInforContext(credentials, sessionID), userGroups, menuSpecification);
	}

	public BatchResponse<String> deleteFromMenuHierarchyBatch(List<MenuSpecification> menuSpecificationList, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getUserGroupMenuService().deleteFromMenuHierarchyBatch(inforClient.getTools().getInforContext(credentials, sessionID), menuSpecificationList);
	}

	public BatchResponse<String> deleteFromMenuHierarchyManyUsergroups(List<String> userGroups, MenuSpecification menuSpecification, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getUserGroupMenuService().deleteFromMenuHierarchyManyUsergroups(inforClient.getTools().getInforContext(credentials, sessionID), userGroups, menuSpecification);
	}

	public String deleteFromMenuHierarchy(MenuSpecification ms, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getUserGroupMenuService().deleteFromMenuHierarchy(inforClient.getTools().getInforContext(credentials, sessionID), ms);
	}

	public Nonconformity readNonconformityDefault(String nc, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getNonconformityService().readNonconformityDefault(inforClient.getTools().getInforContext(credentials,
				sessionID));
	}
	public String createNonconformity(Nonconformity nc, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getNonconformityService().createNonconformity(inforClient.getTools().getInforContext(credentials,
				sessionID), nc);
	}

	public Nonconformity readNonconformity(String nc, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getNonconformityService().readNonconformity(inforClient.getTools().getInforContext(credentials,
				sessionID), nc);
	}

	public String updateNonconformity(Nonconformity nc, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getNonconformityService().updateNonconformity(inforClient.getTools().getInforContext(credentials,
				sessionID), nc);
	}

	public String deleteNonconformity(String nc, Credentials credentials, String sessionID) throws InforException {
		return inforClient.getNonconformityService().deleteNonconformity(inforClient.getTools().getInforContext(credentials,
				sessionID), nc);
	}
}
