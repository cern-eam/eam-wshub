package ch.cern.cmms.wshub.userdefinedscreens;

import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTOpBean;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTRow;
import ch.cern.eam.wshub.core.tools.InforException;
import java.util.List;
import java.util.Map;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;


@RequestScoped
public class UDTService {

    @Inject
    private InforClient inforClient;

    public String createUserDefinedTableRows(UDTOpBean udtOpBean, InforContext inforContext) throws InforException {
        return inforClient.getUserDefinedScreenService().createUserDefinedScreenRow(
            inforContext,
            udtOpBean.getTableName(),
            udtOpBean.getRow()
        );
    }

    public String deleteUserDefinedTableRows(UDTOpBean udtOpBean, InforContext inforContext)
        throws InforException {
        return inforClient.getUserDefinedScreenService().deleteUserDefinedScreenRow(
            inforContext,
            udtOpBean.getTableName(),
            udtOpBean.getRow()
        );
    }
}
