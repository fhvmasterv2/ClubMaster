package at.fhv.service.dto;
import io.swagger.annotations.ApiModel;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link at.fhv.domain.MembershipFee} entity.
 */
@ApiModel(description = "optional")
public class MembershipFeeDTO implements Serializable {

    private Long id;

    private Double amount;

    private LocalDate dueDate;

    private Boolean isPaid;


    private Long memberId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean isIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MembershipFeeDTO membershipFeeDTO = (MembershipFeeDTO) o;
        if (membershipFeeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), membershipFeeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MembershipFeeDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", dueDate='" + getDueDate() + "'" +
            ", isPaid='" + isIsPaid() + "'" +
            ", memberId=" + getMemberId() +
            "}";
    }
}
