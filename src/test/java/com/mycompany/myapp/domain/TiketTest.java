package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TiketTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tiket.class);
        Tiket tiket1 = new Tiket();
        tiket1.setId(1L);
        Tiket tiket2 = new Tiket();
        tiket2.setId(tiket1.getId());
        assertThat(tiket1).isEqualTo(tiket2);
        tiket2.setId(2L);
        assertThat(tiket1).isNotEqualTo(tiket2);
        tiket1.setId(null);
        assertThat(tiket1).isNotEqualTo(tiket2);
    }
}
