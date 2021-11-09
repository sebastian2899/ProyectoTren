package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Tren;
import com.mycompany.myapp.repository.TrenRepository;
import com.mycompany.myapp.service.TrenService;
import com.mycompany.myapp.service.dto.TrenDTO;
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
 * Service Implementation for managing {@link Tren}.
 */
@Service
@Transactional
public class TrenServiceImpl implements TrenService {

    private final Logger log = LoggerFactory.getLogger(TrenServiceImpl.class);

    private final TrenRepository trenRepository;

    private final TrenMapper trenMapper;

    public TrenServiceImpl(TrenRepository trenRepository, TrenMapper trenMapper) {
        this.trenRepository = trenRepository;
        this.trenMapper = trenMapper;
    }

    @Override
    public TrenDTO save(TrenDTO trenDTO) {
        log.debug("Request to save Tren : {}", trenDTO);
        Tren tren = trenMapper.toEntity(trenDTO);
        tren = trenRepository.save(tren);
        return trenMapper.toDto(tren);
    }

    @Override
    public Optional<TrenDTO> partialUpdate(TrenDTO trenDTO) {
        log.debug("Request to partially update Tren : {}", trenDTO);

        return trenRepository
            .findById(trenDTO.getId())
            .map(existingTren -> {
                trenMapper.partialUpdate(existingTren, trenDTO);

                return existingTren;
            })
            .map(trenRepository::save)
            .map(trenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrenDTO> findAll() {
        log.debug("Request to get all Trens");
        return trenRepository.findAll().stream().map(trenMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrenDTO> findOne(Long id) {
        log.debug("Request to get Tren : {}", id);
        return trenRepository.findById(id).map(trenMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tren : {}", id);
        trenRepository.deleteById(id);
    }
}
