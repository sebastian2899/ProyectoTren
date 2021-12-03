package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class RegistroHistoricoTiketDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String nombreCliente;

    private Long puesto;

    private BigDecimal precioTotal;

    private String estado;

    private String jornada;

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Long getPuesto() {
        return puesto;
    }

    public void setPuesto(Long puesto) {
        this.puesto = puesto;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }
}
