package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Tiket} entity.
 */
public class TiketDTO implements Serializable {

    private Long id;

    private Instant fecha;

    private Integer trenId;

    private Integer clienteId;

    private Integer puesto;

    private String estado;

    private String jordana;

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

    public Integer getTrenId() {
        return trenId;
    }

    public void setTrenId(Integer trenId) {
        this.trenId = trenId;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getPuesto() {
        return puesto;
    }

    public void setPuesto(Integer puesto) {
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
