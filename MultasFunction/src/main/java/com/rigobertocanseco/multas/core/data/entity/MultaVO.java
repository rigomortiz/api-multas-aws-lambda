package com.rigobertocanseco.multas.core.data.entity;

import java.util.Date;


public class MultaVO {
    private String placa;
    private String folio;
    private Date fechaInfraccion;
    private Boolean pagada;
    private String motivo;
    private String fundamento;
    private Integer sancion;
    private Float importe;
    private String lineCaptura;
    private Date fechaValida;
    private Date fechaAltaInfraccion;
    private Float unidadCuenta;
    private Integer diasUnidadCuenta;
    private Float monto;
    private Float descuento;
    private Float actualizacion;
    private Float recargo;
    private Float total;

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Date getFechaInfraccion() {
        return fechaInfraccion;
    }

    public void setFechaInfraccion(Date fechaInfraccion) {
        this.fechaInfraccion = fechaInfraccion;
    }

    public Boolean getPagada() {
        return pagada;
    }

    public void setPagada(Boolean pagada) {
        this.pagada = pagada;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getFundamento() {
        return fundamento;
    }

    public void setFundamento(String fundamento) {
        this.fundamento = fundamento;
    }

    public Integer getSancion() {
        return sancion;
    }

    public void setSancion(Integer sancion) {
        this.sancion = sancion;
    }

    public Float getImporte() {
        return importe;
    }

    public void setImporte(Float importe) {
        this.importe = importe;
    }

    public String getLineCaptura() {
        return lineCaptura;
    }

    public void setLineCaptura(String lineCaptura) {
        this.lineCaptura = lineCaptura;
    }

    public Date getFechaValida() {
        return fechaValida;
    }

    public void setFechaValida(Date fechaValida) {
        this.fechaValida = fechaValida;
    }

    public Date getFechaAltaInfraccion() {
        return fechaAltaInfraccion;
    }

    public void setFechaAltaInfraccion(Date fechaAltaInfraccion) {
        this.fechaAltaInfraccion = fechaAltaInfraccion;
    }

    public Float getUnidadCuenta() {
        return unidadCuenta;
    }

    public void setUnidadCuenta(Float unidadCuenta) {
        this.unidadCuenta = unidadCuenta;
    }

    public Integer getDiasUnidadCuenta() {
        return diasUnidadCuenta;
    }

    public void setDiasUnidadCuenta(Integer diasUnidadCuenta) {
        this.diasUnidadCuenta = diasUnidadCuenta;
    }

    public Float getMonto() {
        return monto;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public Float getDescuento() {
        return descuento;
    }

    public void setDescuento(Float descuento) {
        this.descuento = descuento;
    }

    public Float getActualizacion() {
        return actualizacion;
    }

    public void setActualizacion(Float actualizacion) {
        this.actualizacion = actualizacion;
    }

    public Float getRecargo() {
        return recargo;
    }

    public void setRecargo(Float recargo) {
        this.recargo = recargo;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "{" +
                "\"placa\":\"" + placa + '\"' +
                ", \"folio\":\"" + folio + '\"' +
                ", \"fechaInfraccion\":" + fechaInfraccion +
                ", \"pagada\":" + pagada +
                ", \"motivo\":\"" + motivo + '\"' +
                ", \"fundamento\":\"" + fundamento + '\"' +
                ", \"sancion\":" + sancion +
                ", \"importe\":" + importe +
                ", \"lineCaptura\":\"" + lineCaptura + '\"' +
                ", \"fechaValida\":" + fechaValida +
                ", \"fechaAltaInfraccion\":" + fechaAltaInfraccion +
                ", \"unidadCuenta\":" + unidadCuenta +
                ", \"diasUnidadCuenta\":" + diasUnidadCuenta +
                ", \"monto\":" + monto +
                ", \"descuento\":" + descuento +
                ", \"actualizacion\":" + actualizacion +
                ", \"recargo\":" + recargo +
                ", \"total\":" + total +
                '}';
    }
}