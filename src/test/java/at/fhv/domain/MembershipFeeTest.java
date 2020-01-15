package at.fhv.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import at.fhv.web.rest.TestUtil;

public class MembershipFeeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipFee.class);
        MembershipFee membershipFee1 = new MembershipFee();
        membershipFee1.setId(1L);
        MembershipFee membershipFee2 = new MembershipFee();
        membershipFee2.setId(membershipFee1.getId());
        assertThat(membershipFee1).isEqualTo(membershipFee2);
        membershipFee2.setId(2L);
        assertThat(membershipFee1).isNotEqualTo(membershipFee2);
        membershipFee1.setId(null);
        assertThat(membershipFee1).isNotEqualTo(membershipFee2);
    }
}
