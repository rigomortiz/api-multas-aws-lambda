package com.rigobertocanseco.multas.api.aws.lambda;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonStreamParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MultasFunctionTest {
    @Test
    public void successfulResponse() {

        try {
            MultasFunction app = new MultasFunction();

            String buffer = "{\n" +
                    "  \"body\": {\n" +
                    "    \"placa\": \"490WFU\"\n" +
                    "  },\n" +
                    "  \"resource\": \"/{proxy+}\",\n" +
                    "  \"path\": \"/path/to/resource\",\n" +
                    "  \"httpMethod\": \"GET\",\n" +
                    "  \"queryStringParameters\": {\n" +
                    "    \"placa\": \"490WFU\"\n" +
                    "  },\n" +
                    "  \"pathParameters\": {\n" +
                    "    \"placa\": \"490WFU\"\n" +
                    "  },\n" +
                    "  \"stageVariables\": {\n" +
                    "    \"baz\": \"qux\"\n" +
                    "  },\n" +
                    "  \"headers\": {\n" +
                    "    \"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\",\n" +
                    "    \"Accept-Encoding\": \"gzip, deflate, sdch\",\n" +
                    "    \"Accept-Language\": \"en-US,en;q=0.8\",\n" +
                    "    \"Cache-Control\": \"max-age=0\",\n" +
                    "    \"CloudFront-Forwarded-Proto\": \"https\",\n" +
                    "    \"CloudFront-Is-Desktop-Viewer\": \"true\",\n" +
                    "    \"CloudFront-Is-Mobile-Viewer\": \"false\",\n" +
                    "    \"CloudFront-Is-SmartTV-Viewer\": \"false\",\n" +
                    "    \"CloudFront-Is-Tablet-Viewer\": \"false\",\n" +
                    "    \"CloudFront-Viewer-Country\": \"US\",\n" +
                    "    \"Host\": \"1234567890.execute-api.{dns_suffix}\",\n" +
                    "    \"Upgrade-Insecure-Requests\": \"1\",\n" +
                    "    \"User-Agent\": \"Custom User Agent String\",\n" +
                    "    \"Via\": \"1.1 08f323deadbeefa7af34d5feb414ce27.cloudfront.net (CloudFront)\",\n" +
                    "    \"X-Amz-Cf-Id\": \"cDehVQoZnx43VYQb9j2-nvCh-9z396Uhbp027Y2JvkCPNLmGJHqlaA==\",\n" +
                    "    \"X-Forwarded-For\": \"127.0.0.1, 127.0.0.2\",\n" +
                    "    \"X-Forwarded-Port\": \"443\",\n" +
                    "    \"X-Forwarded-Proto\": \"https\"\n" +
                    "  },\n" +
                    "  \"requestContext\": {\n" +
                    "    \"accountId\": \"123456789012\",\n" +
                    "    \"resourceId\": \"123456\",\n" +
                    "    \"stage\": \"prod\",\n" +
                    "    \"requestId\": \"c6af9ac6-7b61-11e6-9a41-93e8deadbeef\",\n" +
                    "    \"identity\": {\n" +
                    "      \"cognitoIdentityPoolId\": null,\n" +
                    "      \"accountId\": null,\n" +
                    "      \"cognitoIdentityId\": null,\n" +
                    "      \"caller\": null,\n" +
                    "      \"apiKey\": null,\n" +
                    "      \"sourceIp\": \"127.0.0.1\",\n" +
                    "      \"cognitoAuthenticationType\": null,\n" +
                    "      \"cognitoAuthenticationProvider\": null,\n" +
                    "      \"userArn\": null,\n" +
                    "      \"userAgent\": \"Custom User Agent String\",\n" +
                    "      \"user\": null\n" +
                    "    },\n" +
                    "    \"resourcePath\": \"/{proxy+}\",\n" +
                    "    \"httpMethod\": \"GET\",\n" +
                    "    \"apiId\": \"1234567890\"\n" +
                    "  }\n" +
                    "}";
            InputStream inputStream = new ByteArrayInputStream(buffer.getBytes(StandardCharsets.UTF_8));
            OutputStream outputStream = new ByteArrayOutputStream(1024);
            app.handleRequest(inputStream, outputStream, null);
            assertNotNull(outputStream);
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(outputStream.toString());
            System.out.println(response.toString());
            assertEquals(response.get("statusCode"), 200);
            //assertEquals(response.getHeaders().get("Content-Type"), "application/json");
            //List<MultaVO> infractionList = result.getBody();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
