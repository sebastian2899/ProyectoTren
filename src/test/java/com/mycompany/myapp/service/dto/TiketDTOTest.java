package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TiketDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TiketDTO.class);
        TiketDTO tiketDTO1 = new TiketDTO();
        tiketDTO1.setId(1L);
        TiketDTO tiketDTO2 = new TiketDTO();
        assertThat(tiketDTO1).isNotEqualTo(tiketDTO2);
        tiketDTO2.setId(tiketDTO1.getId());
        assertThat(tiketDTO1).isEqualTo(tiketDTO2);
        tiketDTO2.setId(2L);
        assertThat(tiketDTO1).isNotEqualTo(tiketDTO2);
        tiketDTO1.setId(null);
        assertThat(tiketDTO1).isNotEqualTo(tiketDTO2);
    }
}
