import owner from 'app/entities/owner/owner.reducer';
import pet from 'app/entities/pet/pet.reducer';
import petType from 'app/entities/pet-type/pet-type.reducer';
import visit from 'app/entities/visit/visit.reducer';
import vet from 'app/entities/vet/vet.reducer';
import specialty from 'app/entities/specialty/specialty.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  owner,
  pet,
  petType,
  visit,
  vet,
  specialty,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
