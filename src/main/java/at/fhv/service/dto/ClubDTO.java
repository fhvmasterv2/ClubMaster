package at.fhv.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import at.fhv.domain.enumeration.ClubType;

/**
 * A DTO for the {@link at.fhv.domain.Club} entity.
 */
public class ClubDTO implements Serializable {

    private Long id;

    @NotNull
    private String clubName;

    private LocalDate creationDate;

    private ClubType type;


    private Long addressId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public ClubType getType() {
        return type;
    }

    public void setType(ClubType type) {
        this.type = type;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClubDTO clubDTO = (ClubDTO) o;
        if (clubDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), clubDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClubDTO{" +
            "id=" + getId() +
            ", clubName='" + getClubName() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", type='" + getType() + "'" +
            ", addressId=" + getAddressId() +
            "}";
    }
}
