package ch.cern.cmms.wshub.equipment.entities;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElectricalLinkDTO {
    private BigDecimal HLEVEL;
    private BigDecimal EQP_ID;
    private String EQP_SRCEQP;
    private String EQP_MAINEQP;
    private String EQP_DEPTYPE;
    private String EQP_DEPDESC;
    private String EDT_DESC;
    private String EDT_COLOR;
    private String PARENT_OBJ_DESC;
    private String PARENT_OBJ_MRC;
    private String PARENT_OBJ_OBRTYPE;
    private String PARENT_OBJ_STATUS;
    private String PARENT_UCO_DESC;
    private String CHILD_OBJ_DESC;
    private String CHILD_OBJ_MRC;
    private String CHILD_OBJ_OBRTYPE;
    private String CHILD_OBJ_STATUS;
    private String CHILD_UCO_DESC;
}