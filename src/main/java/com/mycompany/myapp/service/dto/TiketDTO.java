package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Tiket} entity.
 */
public class TiketDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private Instant fecha;

    private Long trenId;

    private Long clienteId;

    private Long puesto;

    private String estado;

    private String jordana;

    private String nombreCliente;

    private BigDecimal precioTiket;

    private BigDecimal precioTotal;

    public TiketDTO() {}

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Long getTrenId() {
        return trenId;
    }

    public BigDecimal getPrecioTiket() {
        return precioTiket;
    }

    public void setPrecioTiket(BigDecimal precioTiket) {
        this.precioTiket = precioTiket;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public void setTrenId(Long trenId) {
        this.trenId = trenId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getPuesto() {
        return puesto;
    }

    public void setPuesto(Long puesto) {
        this.puesto = puesto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getJordana() {
        return jordana;
    }

    public void setJordana(String jordana) {
        this.jordana = jordana;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TiketDTO)) {
            return false;
        }

        TiketDTO tiketDTO = (TiketDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tiketDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TiketDTO{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", trenId=" + getTrenId() +
            ", clienteId=" + getClienteId() +
            ", puesto=" + getPuesto() +
            ", estado='" + getEstado() + "'" +
            ", jordana='" + getJordana() + "'" +
            "}";
    }
}
