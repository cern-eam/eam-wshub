package ch.cern.cmms.wshub.rest.annotations;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import javax.enterprise.inject.Stereotype;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ApiImplicitParams({
        @ApiImplicitParam(name="INFOR_USER", value = "Username", required = false, dataType = "string", paramType = "header"),
        @ApiImplicitParam(name="INFOR_PASSWORD", value = "Password", required = false, dataType = "string", paramType = "header", format = "password"),
        @ApiImplicitParam(name="INFOR_TENANT", value = "Infor Tenant", dataType = "string", paramType = "header"),
        @ApiImplicitParam(name="INFOR_ORGANIZATION", value = "Infor Organization", defaultValue =  "*", dataType = "string", paramType = "header"),
        @ApiImplicitParam(name="INFOR_LOCALIZE_RESULTS", value = "Localize Results", defaultValue =  "true", dataType = "boolean", paramType = "header"),
        @ApiImplicitParam(name="Authorization", value = "OIDC Auth Token", dataType = "string", paramType = "header")
})
@Stereotype
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiInforAuthentication {
}
