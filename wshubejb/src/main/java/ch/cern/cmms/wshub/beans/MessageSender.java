package ch.cern.cmms.wshub.beans;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;

import ch.cern.eam.wshub.core.client.InforClient;

import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.cmms.wshub.entities.AsyncExecution;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.logging.Level;

@Dependent
public class MessageSender {

	@Inject
	@JMSConnectionFactory("java:/ConnectionFactory")
	private JMSContext jmsContext;
	@Resource(lookup="java:/jms/ayncWSQueue")
	private Queue destination;
	@Inject
	private InforClient inforClient;
	
	public String executeAsync(AsyncExecution asyncExecution, Credentials credentials, String sessionID) throws InforException {
		
		// Basic validation
		if (asyncExecution.getAsyncOperations() == null 
				|| asyncExecution.geteMailNotification() == null
				|| asyncExecution.geteMailNotification().getRecepient() == null
				|| asyncExecution.geteMailNotification().getRecepient().trim().equals("")
				|| asyncExecution.geteMailNotification().getSubject() == null
				|| asyncExecution.geteMailNotification().getSubject().trim().equals("")
				|| asyncExecution.geteMailNotification().getContent() == null
				|| asyncExecution.geteMailNotification().getContent().trim().equals("")) {
			throw inforClient.getTools().generateFault("Please supply all parameters.");
		}
		// Send the message to the queue
		try {
			JMSProducer producer = jmsContext.createProducer();
			ObjectMessage objectMessage = jmsContext.createObjectMessage();
			objectMessage.setObject(asyncExecution);
			objectMessage.setStringProperty("username", credentials.getUsername());
			objectMessage.setStringProperty("password", credentials.getPassword());
			producer.send(destination, objectMessage);
		} catch (Exception e) {
			inforClient.getTools().log(Level.FINE, "Couldn't invoke the message bean: " + e.getMessage());
		}
		
		return "Request has been placed in the queue.";
	}
	
}
