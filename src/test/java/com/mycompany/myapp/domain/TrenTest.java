package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tren.class);
        Tren tren1 = new Tren();
        tren1.setId(1L);
        Tren tren2 = new Tren();
        tren2.setId(tren1.getId());
        assertThat(tren1).isEqualTo(tren2);
        tren2.setId(2L);
        assertThat(tren1).isNotEqualTo(tren2);
        tren1.setId(null);
        assertThat(tren1).isNotEqualTo(tren2);
    }
}
