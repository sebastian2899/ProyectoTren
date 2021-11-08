package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TiketMapperTest {

    private TiketMapper tiketMapper;

    @BeforeEach
    public void setUp() {
        tiketMapper = new TiketMapperImpl();
    }
}
