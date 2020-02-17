package com.rigobertocanseco.multas.api.aws.lambda;

import com.rigobertocanseco.multas.core.data.entity.MultaVO;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * POJO containing response object for API Gateway.
 */
public class GatewayResponse {

    private final List<MultaVO> body;
    private final Map<String, String> headers;
    private final int statusCode;

    public GatewayResponse(final  List<MultaVO>  body, final Map<String, String> headers, final int statusCode) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = Collections.unmodifiableMap(new HashMap<>(headers));
    }

    public  List<MultaVO>  getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
