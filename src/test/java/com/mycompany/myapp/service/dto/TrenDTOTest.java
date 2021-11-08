package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrenDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrenDTO.class);
        TrenDTO trenDTO1 = new TrenDTO();
        trenDTO1.setId(1L);
        TrenDTO trenDTO2 = new TrenDTO();
        assertThat(trenDTO1).isNotEqualTo(trenDTO2);
        trenDTO2.setId(trenDTO1.getId());
        assertThat(trenDTO1).isEqualTo(trenDTO2);
        trenDTO2.setId(2L);
        assertThat(trenDTO1).isNotEqualTo(trenDTO2);
        trenDTO1.setId(null);
        assertThat(trenDTO1).isNotEqualTo(trenDTO2);
    }
}
