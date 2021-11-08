package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tiket.
 */
@Entity
@Table(name = "tiket")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tiket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha")
    private Instant fecha;

    @Column(name = "tren_id")
    private Integer trenId;

    @Column(name = "cliente_id")
    private Integer clienteId;

    @Column(name = "puesto")
    private Integer puesto;

    @Column(name = "estado")
    private String estado;

    @Column(name = "jordana")
    private String jordana;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tiket id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Tiket fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Integer getTrenId() {
        return this.trenId;
    }

    public Tiket trenId(Integer trenId) {
        this.setTrenId(trenId);
        return this;
    }

    public void setTrenId(Integer trenId) {
        this.trenId = trenId;
    }

    public Integer getClienteId() {
        return this.clienteId;
    }

    public Tiket clienteId(Integer clienteId) {
        this.setClienteId(clienteId);
        return this;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getPuesto() {
        return this.puesto;
    }

    public Tiket puesto(Integer puesto) {
        this.setPuesto(puesto);
        return this;
    }

    public void setPuesto(Integer puesto) {
        this.puesto = puesto;
    }

    public String getEstado() {
        return this.estado;
    }

    public Tiket estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getJordana() {
        return this.jordana;
    }

    public Tiket jordana(String jordana) {
        this.setJordana(jordana);
        return this;
    }

    public void setJordana(String jordana) {
        this.jordana = jordana;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tiket)) {
            return false;
        }
        return id != null && id.equals(((Tiket) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tiket{" +
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
