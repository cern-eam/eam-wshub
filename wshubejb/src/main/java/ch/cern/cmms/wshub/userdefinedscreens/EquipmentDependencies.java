package ch.cern.cmms.wshub.userdefinedscreens;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTRow;
import ch.cern.eam.wshub.core.tools.InforException;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isNotEmpty;

import ch.cern.cmms.wshub.userdefinedscreens.entities.EquipmentDependency;

@Dependent
public class EquipmentDependencies {

    @Inject
    private InforClient inforClient;

    private UDTRow createUDTRow(EquipmentDependency equipmentDependency) {
        UDTRow udtRow = new UDTRow();
        if (isNotEmpty(equipmentDependency.getMainEquipment())) {
            udtRow.getStrings().put("EQP_MAINEQP", equipmentDependency.getMainEquipment());
        }

        if (isNotEmpty(equipmentDependency.getSourceEquipment())) {
            udtRow.getStrings().put("EQP_SRCEQP", equipmentDependency.getSourceEquipment());
        }

        if (isNotEmpty(equipmentDependency.getDependencyType())) {
            udtRow.getStrings().put("EQP_DEPTYPE", equipmentDependency.getDependencyType());
        }

        return udtRow;
    }

    public String createEquipmentDependency(EquipmentDependency equipmentDependency, Credentials credentials,
                                            String sessionID) throws InforException {
        return inforClient.getUserDefinedScreenService()
                .createUserDefinedScreenRow(inforClient.getTools().getInforContext(credentials, sessionID),
                        "YUEQPD",
                        createUDTRow(equipmentDependency));
    }

    public String deleteEquipmentDependency(EquipmentDependency equipmentDependency, Credentials credentials,
                                            String sessionID) throws InforException {

        if (isEmpty(equipmentDependency.getSourceEquipment())) {
            throw inforClient.getTools().generateFault("Please provide the Source Equipment.");
        }

        if (isEmpty(equipmentDependency.getMainEquipment())) {
            throw inforClient.getTools().generateFault("Please provide the Main Equipment.");
        }

        return inforClient.getUserDefinedScreenService()
                .deleteUserDefinedScreenRow(inforClient.getTools().getInforContext(credentials, sessionID),
                        "YUEQPD",
                        createUDTRow(equipmentDependency));
    }


    public String updateEquipmentDependency(EquipmentDependency equipmentDependency, Credentials credentials,
                                            String sessionID) throws InforException {
        throw inforClient.getTools().generateFault("Updating of Equipment Dependencies is currently not supported.");
    }

}
