import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Owner from './owner';
import Pet from './pet';
import PetType from './pet-type';
import Visit from './visit';
import Vet from './vet';
import Specialty from './specialty';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="owner/*" element={<Owner />} />
        <Route path="pet/*" element={<Pet />} />
        <Route path="pet-type/*" element={<PetType />} />
        <Route path="visit/*" element={<Visit />} />
        <Route path="vet/*" element={<Vet />} />
        <Route path="specialty/*" element={<Specialty />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
