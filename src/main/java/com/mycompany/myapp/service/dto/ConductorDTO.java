package com.mycompany.myapp.service.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class ConductorDTO {

    private Long id;

    private String nombres;

    private String apellidos;

    private String telefono;

    private BigDecimal sueldo;

    private int edad;

    private Instant fechaNacimiento;

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

    private byte[] foto;

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
        if (!(o instanceof ConductorDTO)) return false;
        ConductorDTO that = (ConductorDTO) o;
        return (
            Objects.equals(getId(), that.getId()) &&
            Objects.equals(getNombres(), that.getNombres()) &&
            Objects.equals(getApellidos(), that.getApellidos()) &&
            Objects.equals(getTelefono(), that.getTelefono()) &&
            Objects.equals(getSueldo(), that.getSueldo())
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNombres(), getApellidos(), getTelefono(), getSueldo());
    }

    @Override
    public String toString() {
        return (
            "ConductorDTO{" +
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
