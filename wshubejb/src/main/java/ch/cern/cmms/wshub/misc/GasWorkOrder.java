package ch.cern.cmms.wshub.misc;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.DateAdapter;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.Date;

public class GasWorkOrder {

    private String description;
    private String supplierRank;
    private String gasEquipmentCode;
    private String gasPoint;
    private String requestorCode;
    private String priority;
    private Date expectedReturnDate;
    private Date expectedDeliveryDate;
    private String comment;
    private String edhDocumentNumber;
    private String SCEMCode;
    private String orderLine;
    private String budgetCode;
    private BigDecimal SCEMCodeQuantity;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupplierRank() {
        return supplierRank;
    }

    public void setSupplierRank(String supplierRank) {
        this.supplierRank = supplierRank;
    }

    public String getGasEquipmentCode() {
        return gasEquipmentCode;
    }

    public void setGasEquipmentCode(String gasEquipmentCode) {
        this.gasEquipmentCode = gasEquipmentCode;
    }

    public String getGasPoint() {
        return gasPoint;
    }

    public void setGasPoint(String gasPoint) {
        this.gasPoint = gasPoint;
    }

    public String getRequestorCode() {
        return requestorCode;
    }

    public void setRequestorCode(String requestorCode) {
        this.requestorCode = requestorCode;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(Date expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEdhDocumentNumber() {
        return edhDocumentNumber;
    }

    public void setEdhDocumentNumber(String edhDocumentNumber) {
        this.edhDocumentNumber = edhDocumentNumber;
    }

    public String getOrderLine() {
        return orderLine;
    }

    public void setOrderLine(String orderLine) {
        this.orderLine = orderLine;
    }

    public String getSCEMCode() {
        return SCEMCode;
    }

    @JsonProperty("SCEMCode")
    public void setSCEMCode(String SCEMCode) {
        this.SCEMCode = SCEMCode;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getSCEMCodeQuantity() {
        return SCEMCodeQuantity;
    }

    @JsonProperty("SCEMCodeQuantity")
    public void setSCEMCodeQuantity(BigDecimal SCEMCodeQuantity) {
        this.SCEMCodeQuantity = SCEMCodeQuantity;
    }

    public String getBudgetCode() {
        return budgetCode;
    }

    public void setBudgetCode(String budgetCode) {
        this.budgetCode = budgetCode;
    }
}
