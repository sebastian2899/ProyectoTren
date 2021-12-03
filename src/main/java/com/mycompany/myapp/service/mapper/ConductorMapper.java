package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Conductor;
import com.mycompany.myapp.service.dto.ConductorDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ConductorMapper extends EntityMapper<ConductorDTO, Conductor> {}
