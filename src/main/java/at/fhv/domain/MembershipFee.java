package at.fhv.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * optional
 */
@Entity
@Table(name = "membership_fee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MembershipFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @ManyToOne
    @JsonIgnoreProperties("fees")
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public MembershipFee amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public MembershipFee dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean isIsPaid() {
        return isPaid;
    }

    public MembershipFee isPaid(Boolean isPaid) {
        this.isPaid = isPaid;
        return this;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Member getMember() {
        return member;
    }

    public MembershipFee member(Member member) {
        this.member = member;
        return this;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipFee)) {
            return false;
        }
        return id != null && id.equals(((MembershipFee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MembershipFee{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", dueDate='" + getDueDate() + "'" +
            ", isPaid='" + isIsPaid() + "'" +
            "}";
    }
}
