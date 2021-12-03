package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "conductor")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Conductor implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "nombre")
    private String nombres;

    @Column(name = "apellido")
    private String apellidos;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "fecha_nacimiento")
    private Instant fechaNacimiento;

    @Column(name = "edad")
    private int edad;

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Instant getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Instant fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    private BigDecimal sueldo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public BigDecimal getSueldo() {
        return sueldo;
    }

    public void setSueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Conductor)) return false;
        Conductor conductor = (Conductor) o;
        return (
            Objects.equals(getId(), conductor.getId()) &&
            Objects.equals(getNombres(), conductor.getNombres()) &&
            Objects.equals(getApellidos(), conductor.getApellidos()) &&
            Objects.equals(getTelefono(), conductor.getTelefono()) &&
            Objects.equals(getSueldo(), conductor.getSueldo())
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNombres(), getApellidos(), getTelefono(), getSueldo());
    }

    @Override
    public String toString() {
        return (
            "Conductor{" +
            "id=" +
            id +
            ", nombres='" +
            nombres +
            '\'' +
            ", apellidos='" +
            apellidos +
            '\'' +
            ", telefono='" +
            telefono +
            '\'' +
            ", sueldo=" +
            sueldo +
            '}'
        );
    }
}
