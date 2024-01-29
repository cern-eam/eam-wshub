package ch.cern.cmms.wshub.rest.tools;

import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.eam.wshub.core.tools.InforException;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isNotEmpty;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

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
        String localizeResults = request.getHeader("INFOR_LOCALIZE_RESULTS");
        String authorization = request.getHeader("Authorization");

        InforContext inforContext = new InforContext();

        inforContext.setOrganizationCode(organization);
        inforContext.setTenant(tenant);

        if (isNotEmpty(localizeResults)) {
            inforContext.setLocalizeResults("true".equalsIgnoreCase(localizeResults));
        }

        // Credentials, Session ID
        if (isNotEmpty(user) && isNotEmpty(password)) {
            inforContext.setCredentials(new Credentials(user, password));
        } else if (isNotEmpty(sessionid)) {
            inforContext.setSessionID(sessionid);
        } else if (isNotEmpty(authorization) && authorization.startsWith("Bearer")) {
            inforContext.setSessionID(authorization);
        } else if (isNotEmpty(authorization) && authorization.startsWith("Basic")) {
            inforContext.setCredentials(extractCredentials(authorization));
        }
        else {
            throw inforClient.getTools().generateFault("Credentials, Session ID or an OIDC token is required.");
        }

        return inforContext;
    }

    private Credentials extractCredentials(String authHeader) {
        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] splitCredentials = credentials.split(":", 2);
        return new Credentials(splitCredentials[0], splitCredentials[1]);
    }

}
