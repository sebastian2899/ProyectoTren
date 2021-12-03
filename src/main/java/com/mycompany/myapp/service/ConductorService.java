package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ConductorDTO;
import java.util.List;
import java.util.Optional;

public interface ConductorService {
    List<ConductorDTO> findAll();

    ConductorDTO save(ConductorDTO conductorDto);

    Optional<ConductorDTO> partialUpdate(ConductorDTO conductorDto);

    Optional<ConductorDTO> findOne(Long id);

    List<ConductorDTO> filtrarCondcutor(String nombreApellido);

    List<ConductorDTO> ValidarFiltro(String nombreApellido);

    Long cargarFotoConductor(byte[] foto);

    byte[] actualizarFotoConductor(byte[] foto, Long idConductor);

    byte[] consultarFoto(Long id);

    ConductorDTO actualizarConductor(ConductorDTO conductorDto);

    void delete(Long id);
}
