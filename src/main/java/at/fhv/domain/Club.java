package at.fhv.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import at.fhv.domain.enumeration.ClubType;

/**
 * A Club.
 */
@Entity
@Table(name = "club")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Club implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "club_name", nullable = false)
    private String clubName;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ClubType type;

    @OneToOne
    @JoinColumn(unique = true)
    private Address address;

    @OneToMany(mappedBy = "club")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Member> members = new HashSet<>();

    @OneToMany(mappedBy = "club")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> events = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClubName() {
        return clubName;
    }

    public Club clubName(String clubName) {
        this.clubName = clubName;
        return this;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Club creationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public ClubType getType() {
        return type;
    }

    public Club type(ClubType type) {
        this.type = type;
        return this;
    }

    public void setType(ClubType type) {
        this.type = type;
    }

    public Address getAddress() {
        return address;
    }

    public Club address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Member> getMembers() {
        return members;
    }

    public Club members(Set<Member> members) {
        this.members = members;
        return this;
    }

    public Club addMember(Member member) {
        this.members.add(member);
        member.setClub(this);
        return this;
    }

    public Club removeMember(Member member) {
        this.members.remove(member);
        member.setClub(null);
        return this;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Club events(Set<Event> events) {
        this.events = events;
        return this;
    }

    public Club addEvent(Event event) {
        this.events.add(event);
        event.setClub(this);
        return this;
    }

    public Club removeEvent(Event event) {
        this.events.remove(event);
        event.setClub(null);
        return this;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Club)) {
            return false;
        }
        return id != null && id.equals(((Club) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Club{" +
            "id=" + getId() +
            ", clubName='" + getClubName() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
