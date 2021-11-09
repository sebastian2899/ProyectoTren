package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Tiket;
import com.mycompany.myapp.domain.Tren;
import com.mycompany.myapp.repository.TiketRepository;
import com.mycompany.myapp.repository.TrenRepository;
import com.mycompany.myapp.service.TiketService;
import com.mycompany.myapp.service.dto.TiketDTO;
import com.mycompany.myapp.service.dto.TrenDTO;
import com.mycompany.myapp.service.mapper.TiketMapper;
import com.mycompany.myapp.service.mapper.TrenMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tiket}.
 */
@Service
@Transactional
public class TiketServiceImpl implements TiketService {

    private final Logger log = LoggerFactory.getLogger(TiketServiceImpl.class);

    private final TiketRepository tiketRepository;

    private final TrenRepository trenRepository;

    private final TiketMapper tiketMapper;

    private final TrenMapper trenMapper;

    public TiketServiceImpl(
        TrenMapper trenMapper,
        TiketRepository tiketRepository,
        TiketMapper tiketMapper,
        TrenRepository trenRepository
    ) {
        this.tiketRepository = tiketRepository;
        this.tiketMapper = tiketMapper;
        this.trenMapper = trenMapper;
        this.trenRepository = trenRepository;
    }

    @Override
    public TiketDTO save(TiketDTO tiketDTO) {
        log.debug("Request to save Tiket : {}", tiketDTO);
        Tiket tiket = tiketMapper.toEntity(tiketDTO);
        tiket = tiketRepository.save(tiket);
        return tiketMapper.toDto(tiket);
    }

    @Override
    public Optional<TiketDTO> partialUpdate(TiketDTO tiketDTO) {
        log.debug("Request to partially update Tiket : {}", tiketDTO);

        return tiketRepository
            .findById(tiketDTO.getId())
            .map(existingTiket -> {
                tiketMapper.partialUpdate(existingTiket, tiketDTO);

                return existingTiket;
            })
            .map(tiketRepository::save)
            .map(tiketMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TiketDTO> findAll() {
        log.debug("Request to get all Tikets");

        List<Tiket> clientesTikest = tiketRepository.consultaClientes();

        for (Tiket tiket : clientesTikest) {
            tiket.setNombreCliente(nombreCliente(tiket.getClienteId()));
        }

        return tiketRepository.findAll().stream().map(tiketMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    private String nombreCliente(Long id) {
        return tiketRepository.nombreCliente(id);
    }

    @Override
    @Transactional(readOnly = true)
    public TrenDTO totalPuestos(Long id) {
        log.debug("Request to get puestos");

        Tren tren = trenRepository.consultarPuesto(id);

        return trenMapper.toDto(tren);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TiketDTO> findOne(Long id) {
        log.debug("Request to get Tiket : {}", id);
        return tiketRepository.findById(id).map(tiketMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tiket : {}", id);
        tiketRepository.deleteById(id);
    }
}
