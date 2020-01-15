package at.fhv.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class MembershipFeeMapperTest {

    private MembershipFeeMapper membershipFeeMapper;

    @BeforeEach
    public void setUp() {
        membershipFeeMapper = new MembershipFeeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(membershipFeeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(membershipFeeMapper.fromId(null)).isNull();
    }
}
