package tech.gearsofcode.petclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.gearsofcode.petclinic.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
