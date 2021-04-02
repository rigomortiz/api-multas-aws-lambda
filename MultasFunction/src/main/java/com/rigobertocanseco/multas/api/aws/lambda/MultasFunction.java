package com.rigobertocanseco.multas.api.aws.lambda;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rigobertocanseco.multas.core.data.entity.MultaVO;
import com.rigobertocanseco.multas.core.utils.MultasCDMXRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

/**
 * Handler for requests to Lambda function.
 */
public class MultasFunction implements RequestStreamHandler {
    private static final Logger logger = Logger.getGlobal();

    private String getPlaca(JSONObject event) {
        if (event.get("pathParameters") != null) {
            JSONObject pathParameters = (JSONObject) event.get("pathParameters");
            if (pathParameters.get("placa") != null)
                return String.valueOf(pathParameters.get("placa"));
        } else if (event.get("queryStringParameters") != null) {
            JSONObject queryStringParameters = (JSONObject) event.get("queryStringParameters");
            if (queryStringParameters.get("placa") != null)
                return String.valueOf(queryStringParameters.get("placa"));
        } else if (event.get("body") != null) {
            JSONObject body = (JSONObject) event.get("body");
            if (body.get("placa") != null)
                return String.valueOf(body.get("placa"));
        }

        return null;
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        JSONParser parser = new JSONParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject responseJson = new JSONObject();

        try {
            JSONObject event = (JSONObject) parser.parse(reader);
            //JSONObject responseBody = new JSONObject();
            List<MultaVO> infractionList = null;

            String placa = getPlaca(event);
            logger.info("PLACA:" + placa);

            if (placa != null && !placa.isEmpty()) {
                infractionList = MultasCDMXRequest.buscarMultas(placa);
            }

            if (infractionList == null || infractionList.get(0).getFolio() == null) {
                //responseBody.put("message", "No found");
                responseJson.put("statusCode", 404);
            } else {
                String list = "";
                for (MultaVO i : infractionList) {
                    list = list.concat(i.toString()).concat(",");
                    logger.info(i.toString());
                }
                Gson gson = new GsonBuilder().create();//.setPrettyPrinting().create();
                responseJson.put("body", gson.toJsonTree(infractionList).toString());
                responseJson.put("statusCode", 200);
            }

            JSONObject headerJson = new JSONObject();
            headerJson.put("Content-Type", "application/json");
            responseJson.put("headers", headerJson);

        } catch (ParseException e) {
            e.printStackTrace();
            responseJson.put("statusCode", 500);
            responseJson.put("exception", e.getMessage());
        }

        responseJson.put("isBase64Encoded", false);

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        writer.write(responseJson.toString());
        writer.close();
    }
}
