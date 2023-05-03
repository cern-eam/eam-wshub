package ch.cern.cmms.wshub.beans;

import java.util.List;

import javax.ejb.Local;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import ch.cern.cmms.wshub.misc.GasWorkOrder;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.administration.entities.EAMUser;
import ch.cern.eam.wshub.core.services.administration.entities.MenuSpecification;
import ch.cern.eam.wshub.core.services.comments.entities.Comment;
import ch.cern.eam.wshub.core.services.entities.*;
import ch.cern.eam.wshub.core.services.equipment.entities.*;
import ch.cern.eam.wshub.core.services.material.entities.*;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTOpBean;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTRow;
import ch.cern.eam.wshub.core.services.workorders.entities.*;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.cmms.wshub.entities.AsyncExecution;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.cmms.wshub.equipment.entities.EquipmentGraphRequest;
import ch.cern.cmms.wshub.equipment.entities.Graph;
import ch.cern.eam.wshub.core.services.grids.entities.GridDDSpyFieldsResult;
import ch.cern.eam.wshub.core.services.grids.entities.GridDataspy;
import ch.cern.eam.wshub.core.services.grids.entities.GridMetadataRequestResult;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.cmms.wshub.userdefinedscreens.entities.EquipmentDependency;
import ch.cern.eam.wshub.core.services.workorders.entities.Employee;
import ch.cern.eam.wshub.core.services.workorders.entities.Activity;
import ch.cern.eam.wshub.core.services.workorders.entities.Aspect;
import ch.cern.eam.wshub.core.services.workorders.entities.InforCaseTask;
import ch.cern.eam.wshub.core.services.workorders.entities.TaskplanCheckList;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrderActivityCheckList;

@Local
@WebService(targetNamespace = "http://cern.ch/cmms/infor/wshub", name = "InforWSPortType")
public interface WSHub {

	//
	//
	//
	public EAMUser readEAMUser(@WebParam(name = "userCode")String userCode,
                               @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String createEAMUser(@WebParam(name = "eamUser")EAMUser eamUser,
							   @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String updateEAMUser(@WebParam(name = "eamUser")EAMUser eamUser,
							   @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	//
	// WORK ORDERS
	//
	@WebResult(name = "workOrder")
	public WorkOrder readWorkOrder(@WebParam(name = "number", header = false) String number,
								   @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "workOrderNumber")
	public String updateWorkOrder(@WebParam(name = "workOrder") WorkOrder workorder,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "workOrderNumber")
	public String createWorkOrder(@WebParam(name = "workOrder") WorkOrder workorder,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "workOrderNumber")
	public BatchResponse<String> createWorkOrderBatch(@WebParam(name = "workOrder") List<WorkOrder> workorder,
								  @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;


	@WebResult(name = "workOrder")
	public BatchResponse<WorkOrder> readWorkOrderBatch(@WebParam(name = "number", header = false) List<String> number,
													   @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "workOrderNumber")
	public BatchResponse<String> updateWorkOrderBatch(@WebParam(name = "workOrder") List<WorkOrder> workorder,
													  @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String deleteWorkOrder(@WebParam(name = "workOrderNumber") String workorderNumber,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	//
	// STANDARD WORK ORDERS
	//
	@WebResult(name = "workOrder")
	public StandardWorkOrder readStandardWorkOrder(@WebParam(name = "workOrder") String number,
												   @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "workOrder")
	public String createStandardWorkOrder(@WebParam(name = "standardWorkOrder") StandardWorkOrder standardWorkOrder,
										  @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "workOrder")
	public String updateStandardWorkOrder(@WebParam(name = "standardWorkOrder") StandardWorkOrder standardWorkOrder,
										  @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	//
	//
	//

	public String createLaborBooking(@WebParam(name = "laborBooking") LaborBooking laborBookingParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String createActivity(@WebParam(name = "activity") Activity activityParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String updateActivity(@WebParam(name = "activity") Activity activityParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public Activity[] readActivities(@WebParam(name = "workOrderNumber") String workOrderNumber,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public List<LaborBooking> readBookedLabor(@WebParam(name = "workOrderNumber") String workOrderNumber,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String createTaskPlan(@WebParam(name = "taskPlan") TaskPlan taskPlan,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String updateWorkOrderChecklists(
			@WebParam(name = "workOrderChecklist") WorkOrderActivityCheckList WorkOrderChecklist,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String createRouteEquipment(@WebParam(name = "routeEquipment") RouteEquipment routeEquipment,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String createRoute(@WebParam(name = "route") Route route,
									   @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String createSalesPrice(@WebParam(name = "salesPrice") SalesPrice salesPrice,
							  @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	//
	// PURCHASE ORDER
	//
	@WebResult(name = "purchaseOrderCode")
	public String updatePurchaseOrder(@WebParam(name = "PurchaseOrder") PurchaseOrder purchaseOrderParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	//
	// CASE MANAGEMENT
	//
	@WebResult(name = "case")
	public InforCase readCase(@WebParam(name = "caseID") String caseID,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "caseCode")
	public String createCase(@WebParam(name = "InforCase") InforCase caseMT,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "caseCode")
	public String updateCase(@WebParam(name = "InforCase") InforCase caseMT,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "caseCode")
	public String deleteCase(@WebParam(name = "caseID") String caseID,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	//
	// CASE TASK MANAGEMENT
	//
	@WebResult(name = "caseTaskCode")
	public InforCaseTask readCaseTask(@WebParam(name = "taskID") String caseTaskID,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "caseTaskCode")
	public String createCaseTask(@WebParam(name = "InforCaseTask") InforCaseTask caseTaskMT,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "caseTaskCode")
	public String updateCaseTask(@WebParam(name = "InforCaseTask") InforCaseTask caseTaskMT,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "caseTaskCode")
	public String deleteCaseTask(@WebParam(name = "caseTaskID") String caseTaskID,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	//
	//
	//
	@WebResult(name = "meterCode")
	public String createMeterReading(@WebParam(name = "MeterReading") MeterReading meterReading,
			@WebParam(name = "credentials") Credentials paramCredentials,
			@WebParam(name = "sessionID") String sessionID) throws InforException;

	public String createWorkOrderAdditionalCost(
			@WebParam(name = "workOrderAddCosts") WorkOrderAdditionalCosts workOrderAddCostsParam,
			@WebParam(name = "credentials") Credentials paramCredentials,
			@WebParam(name = "sessionID") String paramString4) throws InforException;

	public String addWorkOrderPart(
			@WebParam(name = "workOrderPart") WorkOrderPart workOrderPart,
			@WebParam(name = "credentials") Credentials paramCredentials,
			@WebParam(name = "sessionID") String paramString4) throws InforException;

	public String createMatarialList(@WebParam(name = "materialList") MaterialList materialList,
			@WebParam(name = "credentials") Credentials paramCredentials,
			@WebParam(name = "sessionID") String paramString4) throws InforException;

	public String updateWOStatus(@WebParam(name = "workOrder") WorkOrder workOrder,
			@WebParam(name = "credentials") Credentials paramCredentials,
			@WebParam(name = "sessionID") String paramString4) throws InforException;

	public String createTaskplanChecklist(@WebParam(name = "taskPlanChecklist") TaskplanCheckList taskChecklist,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	//
	// InspectionService
	//
	@WebResult(name = "status")
	public String createAspect(@WebParam(name = "aspect") Aspect aspect,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	//
	// COMMENTS
	//
	public String createComment(@WebParam(name = "comment") Comment commentParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String updateComment(@WebParam(name = "comment") Comment commentParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String deleteComment(@WebParam(name = "comment") Comment commentParam,
								@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "comments")
	public Comment[] readComments(@WebParam(name = "comment") Comment commentParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	//
	// EQUIPMENT
	//
	public String updateEquipment(@WebParam(name = "equipment") Equipment equipmentParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "equipmentCode")
	public String createEquipment(@WebParam(name = "equipment") Equipment equipmentParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public Equipment readEquipment(@WebParam(name = "equipmentCode") String equipmentCode,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String deleteEquipment(@WebParam(name = "equipmentCode") String equipmentCode,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	String updateEquipmentCode(@WebParam(name = "currentCode") String equipmentCode,
							   @WebParam(name = "newCode") String equipmentNewCode,
							   @WebParam(name = "equipmentType") String equipmentType,
							   @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID) throws InforException;


	@WebResult(name = "equipmentCode")
	public BatchResponse<String> createEquipmentBatch(@WebParam(name = "equipment") List<Equipment> workorder,
													  @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;


	public BatchResponse<Equipment> readEquipmentBatch(@WebParam(name = "equipmentCode", header = false) List<String> number,
													   @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public BatchResponse<String> updateEquipmentBatch(@WebParam(name = "equipment") List<Equipment> workorder,
													  @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;


	public Location readLocation(@WebParam(name = "locationCode") String locationCode,
								   @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String hideEquipment(@WebParam(name = "equipmentCode") String equipmentCode,
								 @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "linearReferenceID")
	public String createEquipmentLinearReference(@WebParam(name = "linearReference") LinearReference linearReference,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "linearReferenceID")
	public String updateEquipmentLinearReference(@WebParam(name = "linearReference") LinearReference linearReference,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "linearReferenceID")
	public String deleteEquipmentLinearReference(@WebParam(name = "linearReferenceID") String linearReferenceID,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "status")
	public String addEquipmentToStructure(@WebParam(name = "equipmentStructure") EquipmentStructure equipmentStructure,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "status")
	public String updateEquipmentStructure(@WebParam(name = "equipmentStructure") EquipmentStructure equipmentStructure,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "status")
	public String removeEquipmentFromStructure(
			@WebParam(name = "equipmentStructure") EquipmentStructure equipmentStructure,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String createEquipmentWarrantyCoverage(
			@WebParam(name = "equipmentWarranty") EquipmentWarranty equipmentWarrantyParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String updateEquipmentWarrantyCoverage(
			@WebParam(name = "equipmentWarranty") EquipmentWarranty equipmentWarrantyParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String createEquipmentPMSchedule(@WebParam(name = "equipmentPMSchedule") EquipmentPMSchedule pmSchedule,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String deleteEquipmentPMSchedule(@WebParam(name = "equipmentPMSchedule") EquipmentPMSchedule pmSchedule,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String updateEquipmentPMSchedule(@WebParam(name = "equipmentPMSchedule") EquipmentPMSchedule pmSchedule,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String updateReleasedPMSchedule(@WebParam(name = "releasedPMSchedule") ReleasedPMSchedule releasedPMSchedule,
											@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String createEquipmentDepreciation(
			@WebParam(name = "equipmentDepreciation") EquipmentDepreciation equipmentDepreciation,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String updateEquipmentDepreciation(
			@WebParam(name = "equipmentDepreciation") EquipmentDepreciation equipmentDepreciation,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "equipmentGraph")
	public Graph readEquipmentGraph(@WebParam(name = "equipmentGraphRequest") EquipmentGraphRequest graph,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String createEquipmentCampaign(@WebParam(name = "equipmentCampaign") EquipmentCampaign equipmentCampaign,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	//
	// MATERIAL MANAGEMENT
	//
	public String createPart(@WebParam(name = "part") Part partParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String updatePart(@WebParam(name = "part") Part partParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public Part readPart(@WebParam(name = "part") String partCode,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String deletePart(@WebParam(name = "partCode") String partCode,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String createIssueReturnPartTransaction(
			@WebParam(name = "issueReturnTransaction") IssueReturnPartTransaction issueReturnPartTransaction,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String addPartStore(@WebParam(name = "partStore") PartStore partStoreParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String updatePartStore(@WebParam(name = "partStore") PartStore partStoreParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String addPartKitTemplate(@WebParam(name = "partKit") PartKitTemplate partKitParam,
									 @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String createKitSession(@WebParam(name = "partKit") BuildKitParam partKitParam,
								   @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String buildKit(@WebParam(name = "partKit") String kitSessionId,
						   @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String addPartSupplier(@WebParam(name = "partSupplier") PartSupplier partSupplierParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String addPartStock(@WebParam(name = "partStock") PartStock partStockParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String updatePartStock(@WebParam(name = "partStock") PartStock partStockParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String addPartManufacturer(@WebParam(name = "partManufacturer") PartManufacturer partManufacturerParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String updatePartManufacturer(@WebParam(name = "partManufacturer") PartManufacturer partManufacturerParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String deletePartManufacturer(@WebParam(name = "partManufacturer") PartManufacturer partManufacturerParam,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "associationID")
	public String createPartAssociation(@WebParam(name = "partAssociation") PartAssociation partAssociation,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "status")
	public String deletePartAssociation(@WebParam(name = "partAssociation") PartAssociation partAssociation,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "partManufacturers")
	public PartManufacturer[] readPartManufacturers(@WebParam(name = "partNumber") String partCode,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "result")
	public String createPartSubstitute(@WebParam(name = "partSubstitute") PartSubstitute partSubstitute,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;


	//
	// BINS
	//
	@WebResult(name = "result")
	public String createStoreBin(@WebParam(name = "bin") Bin bin,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "result")
	public Bin readStoreBin(@WebParam(name = "bin") Bin bin,
								 @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "result")
	public String updateStoreBin(@WebParam(name = "bin") Bin bin,
								 @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "result")
	public String deleteStoreBin(@WebParam(name = "bin") Bin bin,
								 @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;


	@WebResult(name = "result")
	public String createLot(@WebParam(name = "lot") Lot lot,
								 @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	//
	//
	//
	public String executeAsync(@WebParam(name = "asyncExecution") AsyncExecution asyncExecution,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	//
	// Base
	//
	@WebResult(name = "loginStatus")
	public String login(@WebParam(name = "data") String data, @WebParam(name = "Credentials") Credentials credentials,
			@WebParam(name = "sessionID") String sessionID) throws InforException;

	@WebResult(name = "customFields")
	public GridRequestResult getGridResult(@WebParam(name = "gridRequest") GridRequest gridRequest,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "gridMetadata")
	public GridMetadataRequestResult getGridMetadata(@WebParam(name = "gridCode") String gridCode,
			@WebParam(name = "viewType") String viewType, @WebParam(name = "language") String language,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "gridDataspy")
	public GridDataspy getDefaultDataspy(@WebParam(name = "gridCode") String gridCode,
			@WebParam(name = "viewType") String viewType, @WebParam(name = "credentials") Credentials credentials,
			@WebParam(name = "sessionID") String sessionID) throws InforException;

	@WebResult(name = "ddspyFields")
	public GridDDSpyFieldsResult getDDspyFields(@WebParam(name = "gridCode") String gridCode,
			@WebParam(name = "viewType") String viewType, @WebParam(name = "ddSpyId") String ddSpyId,
			@WebParam(name = "language") String language, @WebParam(name = "credentials") Credentials credentials,
			@WebParam(name = "sessionID") String sessionID) throws InforException;

	//
	// GRID
	//
	@WebResult(name = "customFields")
	public CustomField[] readMTCustomFields(@WebParam(name = "entityCode") String entity,
			@WebParam(name = "classCode") String inforClass, @WebParam(name = "credentials") Credentials credentials,
			@WebParam(name = "sessionID") String sessionID) throws InforException;

	//
	// User Defined Screens
	//
	public String createEquipmentDependency(
			@WebParam(name = "EquipmentDependency") EquipmentDependency equipmentDependency,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String updateEquipmentDependency(
			@WebParam(name = "EquipmentDependency") EquipmentDependency equipmentDependency,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String deleteEquipmentDependency(
			@WebParam(name = "EquipmentDependency") EquipmentDependency equipmentDependency,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;


	//
	// USER SETUP CRUD
	//
	@WebResult(name = "user")
	public EAMUser readUserSetup(@WebParam(name = "userCode") String userCode,
								 @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "userCode")
	public String createUserSetup(@WebParam(name = "user") EAMUser user,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "userCode")
	public String updateUserSetup(@WebParam(name = "user") EAMUser caseMT,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "userCode")
	public String deleteUserSetup(@WebParam(name = "userCode") String userCode,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;
	

	//
	// EMPLOYEES
	//
	@WebResult(name = "employee")
	public Employee readEmployee(@WebParam(name = "employeeCode") String employeeCode,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "employeeCode")
	public String createEmployee(@WebParam(name = "employee") Employee employee,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "employeeCode")
	public String updateEmployee(@WebParam(name = "employee") Employee employee,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	@WebResult(name = "employeeCode")
	public String deleteEmployee(@WebParam(name = "employeeCode") String employeeCode,
			@WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	//
	// PICK TICKET
	//
	public String createPickTicket(@WebParam(name = "pickTicketParam") PickTicket pickTicketParam,
								   @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String updatePickTicket(@WebParam(name = "pickTicketParam") PickTicket pickTicketParam,
								   @WebParam(name = "credentials") Credentials credentials, @WebParam(name = "sessionID") String sessionID)
			throws InforException;

	public String addPartToPickList(@WebParam(name = "pickTicketPartParam") PickTicketPart pickTicketPartParam,
                                    @WebParam(name = "credentials") Credentials credentials,
                                    @WebParam(name = "sessionID") String sessionID) throws InforException;


	//
	// USER DEFINED SCREEN
	//
	@WebResult(name = "result")
	String createUserDefinedTableRows(
				@WebParam(name = "udtOpBean") UDTOpBean udtOpBean,
				@WebParam(name = "credentials")Credentials credentials,
				@WebParam(name = "sessionID")String sessionID
		) throws InforException;

	@WebResult(name = "result")
	String deleteUserDefinedTableRows(
				@WebParam(name = "udtOpBean") UDTOpBean udtOpBean,
				@WebParam(name = "credentials") Credentials credentials,
				@WebParam(name = "sessionID") String sessionID
		) throws InforException;

	@WebResult(name = "result")
	String updateUserDefinedTableRows(
			@WebParam(name = "udtOpBean") UDTOpBean udtOpBean,
			@WebParam(name = "credentials") Credentials credentials,
			@WebParam(name = "sessionID") String sessionID
	) throws InforException;

	//
	// GAS
	//
	@WebResult(name = "workOrderNumber")
	String createGasWorkOrder(@WebParam(name = "gasWorkOrder") GasWorkOrder workOrder,
								   @WebParam(name = "credentials") Credentials credentials,
								   @WebParam(name = "sessionID") String sessionID
	) throws InforException;


	//
	// ExtMenusHierarchy
	//
	@WebResult(name = "result")
	public String addToMenuHierarchy(@WebParam(name = "menuSpecification") MenuSpecification menuSpecification,
									 @WebParam(name = "credentials") Credentials credentials,
									 @WebParam(name = "sessionID") String sessionID
	) throws InforException;

	@WebResult(name = "result")
	public BatchResponse<String> addToMenuHierarchyBatch(@WebParam(name = "menuSpecificationList") List<MenuSpecification> menuSpecificationList,
														 @WebParam(name = "credentials") Credentials credentials,
														 @WebParam(name = "sessionID") String sessionID
	) throws InforException;

	@WebResult(name = "result")
	public BatchResponse<String> addToMenuHierarchyManyUsergroups(@WebParam(name = "userGroupsList") List<String> userGroupsList,
																  @WebParam(name = "menuSpecification") MenuSpecification menuSpecification,
																  @WebParam(name = "credentials") Credentials credentials,
																  @WebParam(name = "sessionID") String sessionID
	) throws InforException;

	@WebResult(name = "result")
	public String deleteFromMenuHierarchy(@WebParam(name = "menuSpecification") MenuSpecification menuSpecification,
										  @WebParam(name = "credentials") Credentials credentials,
										  @WebParam(name = "sessionID") String sessionID

	) throws InforException;

	@WebResult(name = "result")
	public BatchResponse<String> deleteFromMenuHierarchyBatch(@WebParam(name = "menuSpecificationList") List<MenuSpecification> menuSpecificationList,
															  @WebParam(name = "credentials") Credentials credentials,
															  @WebParam(name = "sessionID") String sessionID
	) throws InforException;


	@WebResult(name = "result")
	public BatchResponse<String> deleteFromMenuHierarchyManyUsergroups(@WebParam(name = "userGroupsList") List<String> userGroupsList,
																	   @WebParam(name = "menuSpecification") MenuSpecification menuSpecification,
																	   @WebParam(name = "credentials") Credentials credentials,
																	   @WebParam(name = "sessionID") String sessionID
	) throws InforException;

}
