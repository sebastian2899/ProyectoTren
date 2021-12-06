package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.config.EmailUtil;
import com.mycompany.myapp.domain.Cliente;
import com.mycompany.myapp.domain.Tiket;
import com.mycompany.myapp.domain.Tren;
import com.mycompany.myapp.repository.ClienteRepository;
import com.mycompany.myapp.repository.TiketRepository;
import com.mycompany.myapp.repository.TrenRepository;
import com.mycompany.myapp.service.TiketService;
import com.mycompany.myapp.service.dto.RegistroHistoricoTiketDTO;
import com.mycompany.myapp.service.dto.TiketDTO;
import com.mycompany.myapp.service.dto.TrenDTO;
import com.mycompany.myapp.service.mapper.TiketMapper;
import com.mycompany.myapp.service.mapper.TrenMapper;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
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

    private final ClienteRepository clienteRepository;

    public TiketServiceImpl(
        TrenMapper trenMapper,
        TiketRepository tiketRepository,
        TiketMapper tiketMapper,
        TrenRepository trenRepository,
        ClienteRepository clienteRepository
    ) {
        this.tiketRepository = tiketRepository;
        this.tiketMapper = tiketMapper;
        this.trenMapper = trenMapper;
        this.trenRepository = trenRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public TiketDTO save(TiketDTO tiketDTO) {
        log.debug("Request to save Tiket : {}", tiketDTO);
        Tiket tiket = tiketMapper.toEntity(tiketDTO);
        tiket.setNombreCliente(nombreCliente(tiket.getClienteId()));

        Cliente cliente = clienteRepository.buscarCliente(tiketDTO.getClienteId());
        BigDecimal precioTotal = tiket.getPrecioTotal();

        BigDecimal descPremiun = new BigDecimal(0.10);
        BigDecimal descPremiunPlus = new BigDecimal(0.2);

        if (cliente.getTipoCliente().equals("Premium")) {
            BigDecimal decs = tiket.getPrecioTotal().multiply(descPremiun);
            tiket.setPrecioTotal(precioTotal.subtract(decs));
        }

        if (cliente.getTipoCliente().equals("Premium Plus")) {
            BigDecimal desc = tiket.getPrecioTotal().multiply(descPremiunPlus);
            tiket.setPrecioTotal(precioTotal.subtract(desc));
        }

        tiket = tiketRepository.save(tiket);
        enviarCorreoConfirmacion(tiket);
        return tiketMapper.toDto(tiket);
    }

    private void enviarCorreoConfirmacion(Tiket tiket) {
        Cliente cliente = tiketRepository.buscarCliente(tiket.getClienteId());

        DecimalFormat df = new DecimalFormat();

        df.setMaximumFractionDigits(6);
        String format = df.format(tiket.getPrecioTotal());

        if (tiket.getEstado().equals("Activo") && cliente.getCorreo() != null && !cliente.getCorreo().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMMMM.dd hh:mm aaa");
            String mensaje =
                "Tu tiket esta reservado para la fecha: " +
                sdf.format(Date.from(tiket.getFecha())) +
                "--- Puesto del tren: " +
                tiket.getPuesto() +
                "--- Jornada: " +
                tiket.getJordana() +
                "--- Precio del tiket: " +
                format;
            try {
                log.debug("Enviando correo de confirmacion a: " + cliente.getCorreo());
                EmailUtil.sendArchivoTLS(null, cliente.getCorreo().trim(), "Confirmación Tiket", mensaje);
                log.debug("Notificacion enviada a: " + cliente.getCorreo());
            } catch (Exception e) {
                log.debug("Error enviando notificacion ", e);
                log.debug("Error enviando notificacion ", e.getMessage());
                e.printStackTrace();
            }
        }
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

        List<Tiket> clientesTikest = tiketRepository.findAll();

        for (Tiket tiket : clientesTikest) {
            tiket.setNombreCliente(nombreCliente(tiket.getClienteId()));
        }

        return tiketRepository.tiketsActivos().stream().map(tiketMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TiketDTO> listaAllTikets() {
        log.debug("Request to get all Tikets");

        List<Tiket> clientesTikest = tiketRepository.findAll();

        for (Tiket tiket : clientesTikest) {
            tiket.setNombreCliente(nombreCliente(tiket.getClienteId()));
        }

        return tiketRepository.tiketsProcesos().stream().map(tiketMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
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
    public TiketDTO findOne(Long id) {
        log.debug("Request to get Tiket : {}", id);

        Tiket tiket = tiketRepository.findOne(id);

        tiket.setNombreCliente(nombreCliente(tiket.getClienteId()));

        return tiketMapper.toDto(tiket);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tiket : {}", id);
        tiketRepository.deleteById(id);
    }

    @Override
    public List<RegistroHistoricoTiketDTO> consultarTiketFecha(Instant fechaInicio, Instant fechaFin) {
        List<Tiket> listaTiketFechas = tiketRepository.consultarTiketPorfecha(fechaInicio, fechaFin);
        RegistroHistoricoTiketDTO rhtd = null;
        List<RegistroHistoricoTiketDTO> lrhtd = new ArrayList<>();

        for (Tiket registroHistoricoTiketDTO : listaTiketFechas) {
            rhtd = new RegistroHistoricoTiketDTO();
            rhtd.setNombreCliente(nombreCliente(registroHistoricoTiketDTO.getClienteId()));
            rhtd.setPuesto(registroHistoricoTiketDTO.getPuesto());
            rhtd.setEstado(registroHistoricoTiketDTO.getEstado());
            rhtd.setJornada(registroHistoricoTiketDTO.getJordana());
            rhtd.setPrecioTotal(registroHistoricoTiketDTO.getPrecioTotal());
            lrhtd.add(rhtd);
        }
        return lrhtd;
    }
}
