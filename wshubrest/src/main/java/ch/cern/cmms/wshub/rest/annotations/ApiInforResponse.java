package ch.cern.cmms.wshub.rest.annotations;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.inject.Stereotype;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ApiResponses({
        @ApiResponse(code=200, message="Success"),
        @ApiResponse(code=400, message="Error"),
        @ApiResponse(code=500, message="Server Error")
})
@Stereotype
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiInforResponse {
}
