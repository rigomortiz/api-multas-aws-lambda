package com.rigobertocanseco.multas.api.aws.lambda;

public class Request {
    private String placa;

    public Request() {
    }

    public Request(String placa) {
        this.placa = placa;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Override
    public String toString() {
        return "Request{" +
                "placa='" + placa + '\'' +
                '}';
    }
}
