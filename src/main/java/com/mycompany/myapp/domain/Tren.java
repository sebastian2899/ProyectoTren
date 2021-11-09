package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tren.
 */
@Entity
@Table(name = "tren")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tren implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "asientos")
    private Integer asientos;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tren id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAsientos() {
        return this.asientos;
    }

    public Tren asientos(Integer asientos) {
        this.setAsientos(asientos);
        return this;
    }

    public void setAsientos(Integer asientos) {
        this.asientos = asientos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tren)) {
            return false;
        }
        return id != null && id.equals(((Tren) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tren{" +
            "id=" + getId() +
            ", asientos=" + getAsientos() +
            "}";
    }
}
