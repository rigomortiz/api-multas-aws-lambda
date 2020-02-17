package com.rigobertocanseco.multas.api.aws.lambda;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.rigobertocanseco.multas.core.data.entity.MultaVO;
import com.rigobertocanseco.multas.core.utils.MultasCDMXRequest;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Request, Object> {
    private static final Logger logger = Logger.getGlobal();

    

    public Object handleRequest(final Request input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        List<MultaVO> infractionList = MultasCDMXRequest.buscarMultas(input.getPlaca());

        if(infractionList == null || infractionList.get(0).getFolio() == null)
            return new GatewayResponse(infractionList, headers, 200);

        //throw new OrderNotFoundException();
        String list = "";
        for (MultaVO i : infractionList) {
            list = list.concat(i.toString()).concat(",");
            logger.info(i.toString());
        }

        return new GatewayResponse(infractionList, headers, 200);

    }
}
