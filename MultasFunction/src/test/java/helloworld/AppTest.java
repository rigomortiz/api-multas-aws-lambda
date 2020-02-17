package helloworld;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.rigobertocanseco.multas.api.aws.lambda.App;
import com.rigobertocanseco.multas.api.aws.lambda.GatewayResponse;
import com.rigobertocanseco.multas.api.aws.lambda.Request;
import com.rigobertocanseco.multas.core.data.entity.MultaVO;
import org.junit.Test;

import java.util.List;

public class AppTest {
  @Test
  public void successfulResponse() {
    App app = new App();
    Request request = new Request("490WFU");
    GatewayResponse result = (GatewayResponse) app.handleRequest(request, null);
    assertEquals(result.getStatusCode(), 200);
    assertEquals(result.getHeaders().get("Content-Type"), "application/json");
    List<MultaVO> infractionList = result.getBody();
    assertNotNull(infractionList);
    System.out.println(infractionList.toString());
  }
}
