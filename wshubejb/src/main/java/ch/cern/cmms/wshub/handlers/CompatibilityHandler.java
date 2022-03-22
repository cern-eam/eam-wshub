package ch.cern.cmms.wshub.handlers;

import org.w3c.dom.Node;

import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

@Dependent
public class CompatibilityHandler implements SOAPHandler<SOAPMessageContext> {
	
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			try {
				context.getMessage().getSOAPPart().getEnvelope().setPrefix("S");
				context.getMessage().getSOAPPart().getEnvelope().getBody().setPrefix("S");
				context.getMessage().getSOAPPart().getEnvelope().removeNamespaceDeclaration("soap");
				context.getMessage().getSOAPPart().getEnvelope().addNamespaceDeclaration("S", "http://schemas.xmlsoap.org/soap/envelope/");
				context.getMessage().getSOAPPart().getEnvelope().addNamespaceDeclaration("SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/");
				context.getMessage().getSOAPPart().getEnvelope().getHeader().detachNode();
			} catch (Exception e) {
				// Code kept for backwards compatibility
			}
		} else {
			// SCHEDULING DATES -> SCHEDULED DATES
			try {
				if (context.getMessage().getSOAPPart().getEnvelope().getBody().getElementsByTagName("schedulingStartDate").getLength() > 0) {
					Node schedulingStartDateNode = context.getMessage().getSOAPPart().getEnvelope().getBody().getElementsByTagName("schedulingStartDate").item(0);
					schedulingStartDateNode.getOwnerDocument().renameNode(schedulingStartDateNode, null, "scheduledStartDate");
				}
				if (context.getMessage().getSOAPPart().getEnvelope().getBody().getElementsByTagName("schedulingEndDate").getLength() > 0) {
					Node schedulingEndDateNode = context.getMessage().getSOAPPart().getEnvelope().getBody().getElementsByTagName("schedulingEndDate").item(0);
					schedulingEndDateNode.getOwnerDocument().renameNode(schedulingEndDateNode, null, "scheduledEndDate");
				}
			} catch (Exception e) {
				// Code kept for backwards compatibility
			}
		}

		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		try {
			context.getMessage().getSOAPPart().getEnvelope().setPrefix("S");
			context.getMessage().getSOAPPart().getEnvelope().getBody().setPrefix("S");
			context.getMessage().getSOAPPart().getEnvelope().getBody().getFault().setPrefix("S");
			context.getMessage().getSOAPPart().getEnvelope().getBody().getFault().addNamespaceDeclaration("ns3","http://www.w3.org/2003/05/soap-envelope");
			context.getMessage().getSOAPPart().getEnvelope().getBody().getFault().getFirstChild().setTextContent("S:Server");
			context.getMessage().getSOAPPart().getEnvelope().removeNamespaceDeclaration("soap");
			context.getMessage().getSOAPPart().getEnvelope().addNamespaceDeclaration("S", "http://schemas.xmlsoap.org/soap/envelope/");
			context.getMessage().getSOAPPart().getEnvelope().addNamespaceDeclaration("SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/");
			context.getMessage().getSOAPPart().getEnvelope().getHeader().detachNode();	
		} catch (Exception e) {
			System.out.println("Problem: " + e.getMessage());
		}
		return true;
	}

	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<QName> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}
}

