package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Conductor;
import com.mycompany.myapp.repository.ConductorRepository;
import com.mycompany.myapp.service.ConductorService;
import com.mycompany.myapp.service.dto.ConductorDTO;
import com.mycompany.myapp.service.mapper.ConductorMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConductorServiceImpl implements ConductorService {

    private final Logger log = LoggerFactory.getLogger(ConductorServiceImpl.class);

    private final ConductorRepository conductorRepository;
    private final ConductorMapper conductorMapper;

    public ConductorServiceImpl(ConductorRepository conductorRepository, ConductorMapper conductorMapper) {
        this.conductorRepository = conductorRepository;
        this.conductorMapper = conductorMapper;
    }

    @Override
    public List<ConductorDTO> findAll() {
        log.debug("request to all conductors");

        return conductorRepository.findAll().stream().map(conductorMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public ConductorDTO save(ConductorDTO conductorDto) {
        log.debug("request to save conductor {}" + conductorDto);
        Conductor conductor = conductorMapper.toEntity(conductorDto);
        Instant fechaNacimiento = conductor.getFechaNacimiento();

        ZonedDateTime zdt = ZonedDateTime.ofInstant(fechaNacimiento, ZoneId.systemDefault());

        Calendar fecNacimiento = GregorianCalendar.from(zdt);

        Calendar fechaActual = Calendar.getInstance();

        long edadEnDias = (fechaActual.getTimeInMillis() - fecNacimiento.getTimeInMillis()) / 1000 / 60 / 60 / 24;

        int anios = Double.valueOf(edadEnDias / 365.25d).intValue();

        conductor.setEdad(anios);

        conductor = conductorRepository.save(conductor);

        return conductorMapper.toDto(conductor);
    }

    @Override
    public Optional<ConductorDTO> partialUpdate(ConductorDTO conductorDto) {
        log.debug("Request to partially update Conductor : {}", conductorDto);

        return conductorRepository
            .findById(conductorDto.getId())
            .map(existingConductor -> {
                conductorMapper.partialUpdate(existingConductor, conductorDto);

                return existingConductor;
            })
            .map(conductorRepository::save)
            .map(conductorMapper::toDto);
    }

    @Override
    public Optional<ConductorDTO> findOne(Long id) {
        log.debug("request to get conductor {}" + id);
        return conductorRepository.findById(id).map(conductorMapper::toDto);
    }

    @Override
    public List<ConductorDTO> filtrarCondcutor(String nombreApellido) {
        String FiltroRecibido = "%" + nombreApellido + "%";

        List<Conductor> conductores = conductorRepository.conductorFiltro(FiltroRecibido);

        return conductorMapper.toDto(conductores);
    }

    @Override
    public List<ConductorDTO> ValidarFiltro(String nombreApellido) {
        if (nombreApellido == null || nombreApellido.isEmpty() || nombreApellido.equalsIgnoreCase("undefined")) {
            return findAll();
        } else {
            return filtrarCondcutor(nombreApellido);
        }
    }

    @Override
    public Long cargarFotoConductor(byte[] foto) {
        Conductor conductor = new Conductor();
        conductor.setFoto(foto);
        conductor = conductorRepository.save(conductor);

        return conductor.getId();
    }

    @Override
    public byte[] actualizarFotoConductor(byte[] bytes, Long idConductor) {
        Conductor conductor = conductorRepository.buscarConductor(idConductor);
        conductor.setFoto(bytes);
        conductor = conductorRepository.save(conductor);
        return bytes;
    }

    @Override
    public byte[] consultarFoto(Long id) {
        byte[] bytes = conductorRepository.fotoConductor(id);

        return bytes;
    }

    public ConductorDTO actualizarConductor(ConductorDTO conductorDTO) {
        Conductor conductor = conductorMapper.toEntity(conductorDTO);
        Conductor conductorTemp = conductorRepository.buscarConductor(conductorDTO.getId());
        conductor.setFoto(conductorTemp.getFoto());

        Instant fechaNacimiento = conductor.getFechaNacimiento();

        ZonedDateTime zdt = ZonedDateTime.ofInstant(fechaNacimiento, ZoneId.systemDefault());

        Calendar fecNacimiento = GregorianCalendar.from(zdt);

        Calendar fechaActual = Calendar.getInstance();

        long edadEnDias = (fechaActual.getTimeInMillis() - fecNacimiento.getTimeInMillis()) / 1000 / 60 / 60 / 24;

        int anios = Double.valueOf(edadEnDias / 365.25d).intValue();

        conductor.setEdad(anios);
        conductor = conductorRepository.save(conductor);

        return conductorMapper.toDto(conductor);
    }

    @Override
    public void delete(Long id) {
        log.debug("request to delete conductor {}" + id);

        conductorRepository.deleteById(id);
    }
}
