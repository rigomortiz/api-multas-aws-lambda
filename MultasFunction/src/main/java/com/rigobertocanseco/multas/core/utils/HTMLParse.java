package com.rigobertocanseco.multas.core.utils;


import com.rigobertocanseco.multas.core.data.entity.MultaVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParse {

    /**
     * Lista de infracciones
     * @param html String
     * @return
     */
    public static List<MultaVO> getListaMultas(String html){
        List<MultaVO> multaVOList = new ArrayList<MultaVO>();
        Document doc = Jsoup.parse(html);

        Element form = doc.getElementById("contPrincipalCuerpo").child(2).child(0);

        if(doc.getElementById("busca_placa") == null)
            return null;
        String plate = doc.getElementById("busca_placa").child(0).child(0).child(0).child(0).text();
        if(plate == null)
            return null;

        if(doc.getElementById("contenido") == null)
            return null;

        Elements InfractionVOsTables = doc.getElementById("contenido").getElementsByAttributeValue("id", "tablaDatos");
        if(InfractionVOsTables == null)
            return null;
        else if (InfractionVOsTables.size() == 0)
            return null;

        // PARSE InfractionBean ELEMENT TO DATA MODEL
        for(Element table: InfractionVOsTables){
            MultaVO multaVO = getMulta(table);
            if(multaVO == null){
                multaVOList.add(null);
            }else{
                multaVOList.add(multaVO);
            }
        }
        return multaVOList;
    }

    /**
     * Get MultaVO de element
     * @param table Element
     * @return MultaVO
     */
    public static MultaVO getMulta(Element table){
        MultaVO multa = new MultaVO();
        Elements trs = table.select("tr");

        if(trs.size() >1) {

            Elements td1 = trs.get(1).select("td");
            Elements td2 = trs.get(2).select("td");
            Elements td3 = trs.get(3).select("td");
            Elements td4 = trs.get(4).select("td");

            String folio = td1.get(0).text();
            String fecha = td1.get(1).text();
            String pagada = td1.get(2).child(0).attr("class");

            String motivo = td2.get(1).text();
            String fundamento = td3.get(1).text();
            String sancion = td4.get(1).select("b").text();
            multa.setFolio(folio);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = formatter.parse(fecha);
                multa.setFechaInfraccion(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
            if (pagada.equals("pagada")) {
                multa.setPagada(Boolean.TRUE);
            } else {
                multa.setPagada(Boolean.FALSE);
            }


            multa.setMotivo(motivo);
            multa.setFundamento(fundamento);
            multa.setSancion(Integer.parseInt(sancion));
        }

        return multa;
    }

    /**
     * Extrae multa de HTML
     * @param html String
     * @return MultaVO
     */
    public static MultaVO getDetalleMulta(String html){
        MultaVO multaVO = new MultaVO();
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        Element table = doc.getElementById("tabla_Lc");

        if(table != null){

            Elements trs = table.getElementsByTag("tr");
            if(trs.size() != 0) {
                Elements td1 = trs.get(1).select("td");
                Elements td2 = trs.get(2).select("td");
                Elements td3 = trs.get(3).select("td");
                Elements td5 = trs.get(5).select("td");
                Elements td6 = trs.get(6).select("td");
                Elements td7 = trs.get(7).select("td");
                Elements td8 = trs.get(8).select("td");
                Elements td9 = trs.get(9).select("td");
                Elements td10 = trs.get(10).select("td");
                Elements td11 = trs.get(11).select("td");
                Elements td12 = trs.get(12).select("td");

                Float importe = strPesosToFloat(td1.get(1).text());
                String linea_captura = td2.get(1).text();
                Date fecha_valida = formatDate(td3.get(1).text());
                Date fecha_alta_infraccion = strToDate(td5.get(1).text());
                Float unidad_cuenta = strPesosToFloat(td6.get(1).text());
                Integer dias_unidad_cuenta = Integer.parseInt(td7.get(1).text());
                Float monto = strPesosToFloat(td8.get(1).text());
                Float descuento = strPesosToFloat(td9.get(1).text());
                Float actualizacion = strPesosToFloat(td10.get(1).text());
                Float recargo = strPesosToFloat(td11.get(1).text());
                Float total = strPesosToFloat(td12.get(1).text());

                multaVO.setImporte(importe);
                multaVO.setLineCaptura(linea_captura);
                multaVO.setFechaValida(fecha_valida);
                multaVO.setFechaAltaInfraccion(fecha_alta_infraccion);
                multaVO.setUnidadCuenta(unidad_cuenta);
                multaVO.setDiasUnidadCuenta(dias_unidad_cuenta);
                multaVO.setMonto(monto);
                multaVO.setDescuento(descuento);
                multaVO.setActualizacion(actualizacion);
                multaVO.setRecargo(recargo);
                multaVO.setTotal(total);

                return multaVO;
            }

            return null;

        }else{
            return null;
        }

    }

    /**
     * Convierte formato moneda(peso) a Float
     * @param pesos
     * @return
     */
    private static Float strPesosToFloat(String pesos){
        return Float.valueOf( pesos.replaceAll(" ", "").replace("$", "")
                .replaceAll(",", "").replace("-", "")
                .replace("+", ""));
    }

    /**
     * Convierte String a Date
     * @param strDate
     * @return
     */
    private static Date strToDate(String strDate){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    /**
     * Formatea Fecha
     * @param strDate
     * @return Date
     */
    private static Date formatDate(String strDate){
        String[] date = strDate.split(" ");
        String d = date[0];
        String m = date[2];
        String y = date[4];

        switch (m){
            case "Enero":
                m = "01";
                break;
            case "Febrero":
                m = "02";
                break;
            case "Marzo":
                m = "03";
                break;
            case "Abril":
                m = "04";
                break;
            case "Mayo":
                m = "05";
                break;
            case "Junio":
                m = "06";
                break;
            case "Julio":
                m = "07";
                break;
            case "Agosto":
                m = "08";
                break;
            case "Septiembre":
                m = "09";
                break;
            case "Octubre":
                m = "10";
                break;
            case "Noviembre":
                m = "11";
                break;
            case "Diciembre":
                m ="12";
                break;
        }
        return strToDate(y+"-"+m+"-"+d);
    }

    /**
     * Limpia XML
     * @param xml XML a limpiar
     * @return
     */
    private static String cleanPageAllInfractions(String xml){
        //remove style
        Pattern patron = Pattern.compile("<style*.*style>");
        Matcher clean_xml = patron.matcher(xml);
        String str_sin_style = clean_xml.replaceAll("");

        //remove </b>
        patron = Pattern.compile("</b></span>");
        clean_xml = patron.matcher(str_sin_style);
        String str = clean_xml.replaceAll("</span></B>");

        //add </tr>
        patron = Pattern.compile("</td>*\\s*</table>");
        clean_xml = patron.matcher(str);
        str = clean_xml.replaceAll("</tr></table>");

        patron = Pattern.compile("</span></a>\\.");
        clean_xml = patron.matcher(str);
        str = clean_xml.replaceAll("</a>");

        patron = Pattern.compile("<hr*.*</table>");
        clean_xml = patron.matcher(str);
        str = clean_xml.replaceAll("");

        patron = Pattern.compile("</thead>");
        clean_xml = patron.matcher(str);
        str = clean_xml.replaceAll("</tr></thead>");

        return str;
    }
}
