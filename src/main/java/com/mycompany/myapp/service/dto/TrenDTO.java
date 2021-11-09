package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Tren} entity.
 */
public class TrenDTO implements Serializable {

    private Long id;

    private Integer asientos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAsientos() {
        return asientos;
    }

    public void setAsientos(Integer asientos) {
        this.asientos = asientos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrenDTO)) {
            return false;
        }

        TrenDTO trenDTO = (TrenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrenDTO{" +
            "id=" + getId() +
            ", asientos=" + getAsientos() +
            "}";
    }
}
