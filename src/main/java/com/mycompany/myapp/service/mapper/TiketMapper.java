package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.TiketDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tiket} and its DTO {@link TiketDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TiketMapper extends EntityMapper<TiketDTO, Tiket> {}
