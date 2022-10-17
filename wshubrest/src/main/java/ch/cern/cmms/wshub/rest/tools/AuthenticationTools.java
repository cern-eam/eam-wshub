package ch.cern.cmms.wshub.rest.tools;

import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@RequestScoped
public class AuthenticationTools {

    @Inject
    private HttpServletRequest request;
    @Inject
    private InforClient inforClient;

    public InforContext getInforContext() throws InforException
    {
        String user = request.getHeader("INFOR_USER");
        String password = request.getHeader("INFOR_PASSWORD");
        String organization = request.getHeader("INFOR_ORGANIZATION");
        String sessionid = request.getHeader("INFOR_SESSIONID");
        String tenant = request.getHeader("INFOR_TENANT");
        String authorization = request.getHeader("Authorization");

        InforContext inforContext = new InforContext();

        inforContext.setOrganizationCode(organization);
        inforContext.setTenant(tenant);

        // Credentials, Session ID
        if (notEmpty(user) && notEmpty(password)) {
            Credentials credentials = new Credentials();
            credentials.setUsername(user);
            credentials.setPassword(password);
            inforContext.setCredentials(credentials);
        } else if (notEmpty(sessionid)) {
            inforContext.setSessionID(sessionid);
        } else if (notEmpty(authorization) ) {
            inforContext.setSessionID(authorization);
        }
        else {
            throw inforClient.getTools().generateFault("Credentials, Session ID or an OIDC token is required.");
        }

        return inforContext;
    }

    private boolean notEmpty(String string) {
        return string != null && !string.trim().equals("");
    }
}
