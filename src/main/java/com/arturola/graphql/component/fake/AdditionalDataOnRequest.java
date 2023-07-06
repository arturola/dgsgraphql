package com.arturola.graphql.component.fake;

import com.netflix.dgs.codegen.generated.DgsConstants;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import org.springframework.web.bind.annotation.RequestHeader;

@DgsComponent
public class AdditionalDataOnRequest {
    @DgsData(parentType = DgsConstants.QUERY_TYPE,
    field = DgsConstants.QUERY.AdditionalDataOnRequest)
    public String additionalDataOnRequest(
            @RequestHeader(name = "optionalHeader", required = false) String optionalHeader,
            @RequestHeader(name = "mandatoryHeader", required = false) String mandatoryHeader,
            @RequestHeader(name = "optionalParam", required = false)String optionalParam,
            @RequestHeader(name = "mandatoryParam", required = false)String mandatoryParam
    ) {
        var sb = new StringBuilder();

        sb.append("Optional Header: " + optionalHeader);
        sb.append(", ");
        sb.append("Mandatory Header: " + mandatoryHeader);
        sb.append(", ");
        sb.append("Optional Param: " + optionalParam);
        sb.append(", ");
        sb.append("Mandatory Param: " + mandatoryParam);



        return sb.toString();
    }

}
