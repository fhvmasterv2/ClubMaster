package at.fhv.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Member.
 */
@Entity
@Table(name = "member")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fname", nullable = false)
    private String fname;

    @NotNull
    @Column(name = "lname", nullable = false)
    private String lname;

    @Column(name = "dob")
    private LocalDate dob;

    @OneToOne
    @JoinColumn(unique = true)
    private Address address;

    @OneToMany(mappedBy = "member")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MembershipFee> fees = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("members")
    private Club club;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public Member fname(String fname) {
        this.fname = fname;
        return this;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public Member lname(String lname) {
        this.lname = lname;
        return this;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public LocalDate getDob() {
        return dob;
    }

    public Member dob(LocalDate dob) {
        this.dob = dob;
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Address getAddress() {
        return address;
    }

    public Member address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<MembershipFee> getFees() {
        return fees;
    }

    public Member fees(Set<MembershipFee> membershipFees) {
        this.fees = membershipFees;
        return this;
    }

    public Member addFees(MembershipFee membershipFee) {
        this.fees.add(membershipFee);
        membershipFee.setMember(this);
        return this;
    }

    public Member removeFees(MembershipFee membershipFee) {
        this.fees.remove(membershipFee);
        membershipFee.setMember(null);
        return this;
    }

    public void setFees(Set<MembershipFee> membershipFees) {
        this.fees = membershipFees;
    }

    public Club getClub() {
        return club;
    }

    public Member club(Club club) {
        this.club = club;
        return this;
    }

    public void setClub(Club club) {
        this.club = club;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }
        return id != null && id.equals(((Member) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Member{" +
            "id=" + getId() +
            ", fname='" + getFname() + "'" +
            ", lname='" + getLname() + "'" +
            ", dob='" + getDob() + "'" +
            "}";
    }
}
