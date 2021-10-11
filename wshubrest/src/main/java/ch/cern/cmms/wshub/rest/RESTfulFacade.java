package ch.cern.cmms.wshub.rest;

import ch.cern.cmms.plugins.SharedPluginImpl;
import io.swagger.jaxrs.config.BeanConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/REST/apis")
public class RESTfulFacade extends Application {

   public RESTfulFacade() {
       super();
       BeanConfig beanConfig = new BeanConfig();
       beanConfig.setVersion("1.0.1");
       beanConfig.setTitle("Infor Web Services Hub API");
       String baseUrl = new SharedPluginImpl().getBaseUrl();
       beanConfig.setBasePath(baseUrl);
       beanConfig.setResourcePackage("ch.cern.cmms.wshub.rest");
       beanConfig.setScan(true);
   }

}