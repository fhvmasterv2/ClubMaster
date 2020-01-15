package at.fhv.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import at.fhv.web.rest.TestUtil;

public class MembershipFeeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipFeeDTO.class);
        MembershipFeeDTO membershipFeeDTO1 = new MembershipFeeDTO();
        membershipFeeDTO1.setId(1L);
        MembershipFeeDTO membershipFeeDTO2 = new MembershipFeeDTO();
        assertThat(membershipFeeDTO1).isNotEqualTo(membershipFeeDTO2);
        membershipFeeDTO2.setId(membershipFeeDTO1.getId());
        assertThat(membershipFeeDTO1).isEqualTo(membershipFeeDTO2);
        membershipFeeDTO2.setId(2L);
        assertThat(membershipFeeDTO1).isNotEqualTo(membershipFeeDTO2);
        membershipFeeDTO1.setId(null);
        assertThat(membershipFeeDTO1).isNotEqualTo(membershipFeeDTO2);
    }
}
