package ch.cern.cmms.wshub.entities;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

@SuppressWarnings("serial")
public class EMailNotification implements Serializable{

	private String To;
	private String Subject;
	private String Content;
	private String AdditionalContent;
	
	public String getRecepient() {
		return To;
	}
	public void setRecepient(String recepient) {
		To = recepient;
	}
	public String getSubject() {
		return Subject;
	}
	public void setSubject(String subject) {
		Subject = subject;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	
	@XmlTransient
	public String getAdditionalContent() {
		return AdditionalContent;
	}
	public void setAdditionalContent(String additionalContent) {
		AdditionalContent = additionalContent;
	}
	@Override
	public String toString() {
		return "EMailNotification [To=" + To + ", Subject=" + Subject + ", Content=" + Content + ", AdditionalContent="
				+ AdditionalContent + "]";
	}
	
}
