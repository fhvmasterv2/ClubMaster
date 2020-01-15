package at.fhv.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ClubMapperTest {

    private ClubMapper clubMapper;

    @BeforeEach
    public void setUp() {
        clubMapper = new ClubMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(clubMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(clubMapper.fromId(null)).isNull();
    }
}
