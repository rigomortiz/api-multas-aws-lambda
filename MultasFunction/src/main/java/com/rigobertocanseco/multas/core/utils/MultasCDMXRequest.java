package com.rigobertocanseco.multas.core.utils;

import com.rigobertocanseco.multas.core.data.entity.MultaVO;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.logging.Logger;

public class MultasCDMXRequest {
    private static final String URL_INICIO = "https://data.finanzas.cdmx.gob.mx/sma/Consultaciudadana/inicio/detalleplaca";

    private static final String URL_DETALLE = "https://data.finanzas.cdmx.gob.mx/formato_lc/infracciones/inf_html_folio3.php";

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String PARAMS = "info=cXpmSHo4cng4ZUN6NVZZL3pWWlBtY2ZBTDBVdUtPRVN4czMvRHNsUFVXbitIUW13dG1mc2c1SWF1V1FESVg2ZmUvTC81bk5FbEFEaTV0NmYvVDh4ZFQ2YnErOVAvZW1hTEFSS1J6MDhGU3dwb21tRUhDNFBYeUc4L0RYQkRGWTk5TGVuL1dKcEZwSld6a0hJSGtpdzhRPT0=";

    private static HttpsURLConnection con;
    private static final Logger logger = Logger.getGlobal();

    /**
     * Busca multas por placa
     * @param placa String
     * @return List<MultaVO>
     */
    public static List<MultaVO> buscarMultas(String placa) {
        if(placa == null || placa.isEmpty() || placa.length()> 10 )
            return null;

        String response = MultasCDMXRequest.post(URL_INICIO + "?" + PARAMS, placa.toUpperCase());

        if(response == null || response.isEmpty()){
            return null;
        }

        return MultasCDMXRequest.getMultas(HTMLParse.getListaMultas(response), placa);
    }

    /**
     * Busca información de multa por folio
     * @param folio String
     * @return MultaVO
     */
    public static MultaVO getDetallePagoMulta(String folio) {
        String response = MultasCDMXRequest.get(URL_DETALLE + "?folio=" + folio);

        if(response == null){
            return null;
        }

        return HTMLParse.getDetalleMulta(response);
    }

    /**
     * Obtiene detalle de multa
     * @param multaList
     * @return
     */
    private static List<MultaVO> getMultas(List<MultaVO> multaList, String placa) {
        if(multaList != null){
            for(MultaVO multaVO : multaList){
                if(multaVO != null && multaVO.getPagada() != null) {
                    multaVO.setPlaca(placa);
                    if(!multaVO.getPagada()){
                        MultaVO tmp = MultasCDMXRequest.getDetallePagoMulta(multaVO.getFolio());
                        if (tmp != null) {
                            multaVO.setActualizacion(tmp.getActualizacion());
                            multaVO.setSancion(tmp.getSancion());
                            multaVO.setPagada(tmp.getPagada());
                            multaVO.setFechaInfraccion(tmp.getFechaInfraccion());
                            multaVO.setRecargo(tmp.getRecargo());
                            multaVO.setDescuento(tmp.getDescuento());
                            multaVO.setMonto(tmp.getMonto());
                            multaVO.setDiasUnidadCuenta(tmp.getDiasUnidadCuenta());
                            multaVO.setUnidadCuenta(tmp.getUnidadCuenta());
                            multaVO.setFechaAltaInfraccion(tmp.getFechaAltaInfraccion());
                            multaVO.setTotal(tmp.getTotal());
                            multaVO.setFechaValida(tmp.getFechaValida());
                            multaVO.setLineaCaptura(tmp.getLineaCaptura());
                            multaVO.setImporte(tmp.getImporte());
                            multaVO.setFolio(tmp.getFolio());
                            multaVO.setFundamento(tmp.getFundamento());
                            multaVO.setMotivo(tmp.getMotivo());
                        }
                    }
                }
            }
            return multaList;
        }else {
            return null;
        }
    }

    /**
     * Request POST
     * @param url String
     * @param placa String
     * @return Response
     */
    private static String post(String url, String placa)  {
        try {
            con = (HttpsURLConnection) new URL(url).openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            con.setRequestProperty("User-Agent", USER_AGENT);

            Map<String,String> arguments = new HashMap<>();
            arguments.put("placa", placa);
            arguments.put("envia", "Consulta"); // This is a fake password obviously
            StringJoiner sj = new StringJoiner("&");
            for(Map.Entry<String,String> entry : arguments.entrySet())
                sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                        + URLEncoder.encode(entry.getValue(), "UTF-8"));
            byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            con.setFixedLengthStreamingMode(length);
            logger.info("Sending 'POST' request to URL: " + url);
            logger.info("Post parameters: " + sj);

            con.connect();
            try(OutputStream os = con.getOutputStream()) {
                os.write(out);
            }

            if(con.getResponseCode() == 200){
                if(con.getResponseMessage() != null){
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    return  response.toString();
                }
            }
        } catch (ProtocolException e) {
            logger.info("ProtocolException: " + e.getMessage());
            e.printStackTrace();
        } catch (MalformedURLException e) {
            logger.info("MalformedURLException: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.info("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.info("Exception: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Request GET
     * @param url String
     * @return Response
     */
    private static String get(String url)  {
        try {
            con = (HttpsURLConnection) new URL(url).openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            logger.info("Sending 'GET' request to URL:" + url);
            logger.info("Response Code:" + con.getResponseCode());

            if(con.getResponseCode() == 200){
                if(con.getResponseMessage() != null){
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    return  response.toString();
                }
            }
        } catch (ProtocolException e) {
            logger.info("ProtocolException: " + e.getMessage());
            e.printStackTrace();
        } catch (MalformedURLException e) {
            logger.info("MalformedURLException: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.info("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.info("Exception: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
