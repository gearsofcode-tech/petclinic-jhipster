package tech.gearsofcode.petclinic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Specialty.
 */
@Entity
@Table(name = "specialty")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Specialty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "specialties")
    @JsonIgnoreProperties(value = { "specialties" }, allowSetters = true)
    private Set<Vet> experts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Specialty id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Specialty name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Vet> getExperts() {
        return this.experts;
    }

    public void setExperts(Set<Vet> vets) {
        if (this.experts != null) {
            this.experts.forEach(i -> i.removeSpecialties(this));
        }
        if (vets != null) {
            vets.forEach(i -> i.addSpecialties(this));
        }
        this.experts = vets;
    }

    public Specialty experts(Set<Vet> vets) {
        this.setExperts(vets);
        return this;
    }

    public Specialty addExperts(Vet vet) {
        this.experts.add(vet);
        vet.getSpecialties().add(this);
        return this;
    }

    public Specialty removeExperts(Vet vet) {
        this.experts.remove(vet);
        vet.getSpecialties().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Specialty)) {
            return false;
        }
        return id != null && id.equals(((Specialty) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Specialty{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
