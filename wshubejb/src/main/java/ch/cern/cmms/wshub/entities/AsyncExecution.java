package ch.cern.cmms.wshub.entities;

import java.io.Serializable;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class AsyncExecution implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1612019355925633989L;
	private EMailNotification eMailNotification;
	private AsyncOperation[] asyncOperations;
	
	public AsyncExecution() {
		
	}
	
	public EMailNotification geteMailNotification() {
		return eMailNotification;
	}
	public void seteMailNotification(EMailNotification eMailNotification) {
		this.eMailNotification = eMailNotification;
	}
	@XmlElementWrapper(name="asyncOperations") 
    @XmlElement(name="asyncOperation")
	public AsyncOperation[] getAsyncOperations() {
		return asyncOperations;
	}
	public void setAsyncOperations(AsyncOperation[] asyncOperations) {
		this.asyncOperations = asyncOperations;
	}

	@Override
	public String toString() {
		return "AsyncExecution [eMailNotification=" + eMailNotification + ", asyncOperations="
				+ Arrays.toString(asyncOperations) + "]";
	}
	
}
