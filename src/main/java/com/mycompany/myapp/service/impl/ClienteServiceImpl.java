package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Cliente;
import com.mycompany.myapp.repository.ClienteRepository;
import com.mycompany.myapp.service.ClienteService;
import com.mycompany.myapp.service.dto.ClienteDTO;
import com.mycompany.myapp.service.mapper.ClienteMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cliente}.
 */
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final Logger log = LoggerFactory.getLogger(ClienteServiceImpl.class);

    private final ClienteRepository clienteRepository;

    private final ClienteMapper clienteMapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    public ClienteDTO save(ClienteDTO clienteDTO) {
        log.debug("Request to save Cliente : {}", clienteDTO);
        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toDto(cliente);
    }

    @Override
    public Optional<ClienteDTO> partialUpdate(ClienteDTO clienteDTO) {
        log.debug("Request to partially update Cliente : {}", clienteDTO);

        return clienteRepository
            .findById(clienteDTO.getId())
            .map(existingCliente -> {
                clienteMapper.partialUpdate(existingCliente, clienteDTO);

                return existingCliente;
            })
            .map(clienteRepository::save)
            .map(clienteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> findAll() {
        log.debug("Request to get all Clientes");
        return clienteRepository.findAll().stream().map(clienteMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteDTO> findOne(Long id) {
        log.debug("Request to get Cliente : {}", id);
        return clienteRepository.findById(id).map(clienteMapper::toDto);
    }

    @Override
    public List<ClienteDTO> buscarConFiltros(String nombreFiltro) {
        String filtroRecibido = "%" + nombreFiltro + "%";

        List<Cliente> clientesFiltrados = clienteRepository.clientesPorFiltro(filtroRecibido);

        return clienteMapper.toDto(clientesFiltrados);
    }

    public List<ClienteDTO> validarFiltro(String nombreFiltro) {
        if (nombreFiltro == null || nombreFiltro.isEmpty() || nombreFiltro.equalsIgnoreCase("undefined")) {
            return findAll();
        } else {
            return buscarConFiltros(nombreFiltro);
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cliente : {}", id);
        clienteRepository.deleteById(id);
    }

    @Override
    public Long cargarFotoCliente(byte[] foto) {
        Cliente cliente = new Cliente();
        cliente.setFoto(foto);
        clienteRepository.save(cliente);

        return cliente.getId();
    }

    @Override
    public byte[] actualizarFotoCliente(byte[] bytes, Long idCliente) {
        Cliente cliente = clienteRepository.buscarCliente(idCliente);
        cliente.setFoto(bytes);

        return bytes;
    }

    @Override
    public ClienteDTO actualizarCliente(ClienteDTO clienteDto) {
        Cliente cliente = clienteMapper.toEntity(clienteDto);
        Cliente clienteTemp = clienteRepository.buscarCliente(clienteDto.getId());

        cliente.setFoto(clienteTemp.getFoto());
        clienteRepository.save(cliente);

        return clienteMapper.toDto(cliente);
    }

    @Override
    public byte[] consultarFoto(Long idCliente) {
        byte[] foto = clienteRepository.consultarFotoCliente(idCliente);
        return foto;
    }
}
