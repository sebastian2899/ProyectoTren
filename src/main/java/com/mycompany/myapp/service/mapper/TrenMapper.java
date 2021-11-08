package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.TrenDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tren} and its DTO {@link TrenDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TrenMapper extends EntityMapper<TrenDTO, Tren> {}
