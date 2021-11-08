package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrenMapperTest {

    private TrenMapper trenMapper;

    @BeforeEach
    public void setUp() {
        trenMapper = new TrenMapperImpl();
    }
}
