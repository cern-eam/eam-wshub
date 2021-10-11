package ch.cern.cmms.wshub.tools;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApplicationData {

	public String getPassphrase() {
		return Tools.getVariableValue("WSHUB_PASSPHRASE");
	}

}
