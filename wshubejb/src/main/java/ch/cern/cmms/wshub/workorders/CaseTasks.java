package ch.cern.cmms.wshub.workorders;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.cmms.wshub.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.cmms.wshub.tools.Tools;
import ch.cern.eam.wshub.core.services.workorders.entities.InforCaseTask;

@ApplicationScoped
public class CaseTasks {

	@Inject
	private Tools tools;
	@Inject
	private InforClient inforClient;
	@Inject
	private ApplicationData applicationData;

}
