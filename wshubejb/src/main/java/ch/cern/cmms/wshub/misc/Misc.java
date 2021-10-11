package ch.cern.cmms.wshub.misc;

import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.tools.InforException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Random;

@RequestScoped
public class Misc {

    @Inject
    private InforClient inforClient;

    public String hideEquipment(InforContext inforContext, String equipmentCode) throws InforException {
        Equipment equipment = new Equipment();
        equipment.setCode(equipmentCode);
        equipment.setOutOfService(true);
        equipment.setDepartmentCode("*INA");
        inforClient.getEquipmentFacadeService().updateEquipment(inforContext, equipment);
        Random random = new Random();
        String newEquipmentCode = "HIDDEN_" + (random.nextInt(9000000) + 1000000);
        inforClient.getEquipmentOtherService().updateEquipmentCode(inforContext, equipmentCode, newEquipmentCode, null);
        return newEquipmentCode;
    }

}
