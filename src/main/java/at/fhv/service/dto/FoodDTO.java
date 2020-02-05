package at.fhv.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link at.fhv.domain.Food} entity.
 */
public class FoodDTO implements Serializable {

    private Long id;

    private Double amount;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FoodDTO foodDTO = (FoodDTO) o;
        if (foodDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), foodDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FoodDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            "}";
    }
}
