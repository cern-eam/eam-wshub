package ch.cern.cmms.wshub.userdefinedscreens.entities;

import java.io.Serializable;
import java.util.Date;

public class EquipmentDependency implements Serializable {

	private static final long serialVersionUID = -2998397968939474540L;

	private long dependencyID;
	private String mainEquipment;
	private String sourceEquipment;
	private String dependencyType;
	private String attribute;
	private String createdBy;
	private String updatedBy;
	private Date created;
	private Date updated;
	private long updateCount;
	
	public long getDependencyID() {
		return dependencyID;
	}
	public void setDependencyID(long dependencyID) {
		this.dependencyID = dependencyID;
	}
	public String getMainEquipment() {
		return mainEquipment;
	}
	public void setMainEquipment(String mainEquipment) {
		this.mainEquipment = mainEquipment;
	}
	public String getSourceEquipment() {
		return sourceEquipment;
	}
	public void setSourceEquipment(String sourceEquipment) {
		this.sourceEquipment = sourceEquipment;
	}
	public String getDependencyType() {
		return dependencyType;
	}
	public void setDependencyType(String dependencyType) {
		this.dependencyType = dependencyType;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreated() {
		if (created == null) {
			created = new Date();
		}
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public long getUpdateCount() {
		return updateCount;
	}
	public void setUpdateCount(long updateCount) {
		this.updateCount = updateCount;
	}

	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
}
