package ch.cern.cmms.wshub.rest.controllers.parts;

import ch.cern.cmms.wshub.rest.controllers.WSHubController;
import ch.cern.cmms.wshub.rest.tools.AuthenticationTools;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.services.material.entities.Part;
import ch.cern.eam.wshub.core.tools.InforException;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/parts")
public class PartController extends WSHubController {

    @Inject
    private AuthenticationTools authentication;

    @Inject
    private InforClient inforClient;

    @GET
    @Path("/{number}")
    @Produces("application/json")
    public Response readPart(@PathParam("number") String code) {
        try {
            return ok(inforClient.getPartService().readPart(authentication.getInforContext(), code));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @PUT
    @Path("/{code}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response updatePart(@PathParam("code") String code, Part part) {
        try {
            part.setCode(code);
            return ok(inforClient.getPartService().updatePart(authentication.getInforContext(), part));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    public Response createPart(Part part) {
        try {
            return ok(inforClient.getPartService().createPart(authentication.getInforContext(), part));
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

    @DELETE
    @Path("/{code}")
    @Produces("application/json")
    public Response deletePart(@PathParam("code") String code) {
        try {
            inforClient.getPartService().deletePart(authentication.getInforContext(), code);
            return noConent();
        } catch (InforException e) {
            return badRequest(e);
        } catch(Exception e) {
            return serverError(e);
        }
    }

}
