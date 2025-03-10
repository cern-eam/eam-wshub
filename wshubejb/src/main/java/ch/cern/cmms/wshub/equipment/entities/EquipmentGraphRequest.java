package ch.cern.cmms.wshub.equipment.entities;

public class EquipmentGraphRequest {

	private String equipmentCode;
	private String linkTypes;
	private int maxDepth;

	public EquipmentGraphRequest(String equipmentCode, String linkTypes, int maxDepth) {
		this.equipmentCode = equipmentCode;
		this.linkTypes = linkTypes;
		this.maxDepth = maxDepth;
	}

	public EquipmentGraphRequest() {}

	public String getLinkTypes() {
		return linkTypes;
	}
	public void setLinkTypes(String linkTypes) {
		this.linkTypes = linkTypes;
	}
	public int getMaxDepth() {
		return maxDepth;
	}
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	
}
