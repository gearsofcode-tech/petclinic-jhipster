import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPet } from 'app/shared/model/pet.model';
import { getEntities } from './pet.reducer';

export const Pet = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const petList = useAppSelector(state => state.pet.entities);
  const loading = useAppSelector(state => state.pet.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="pet-heading" data-cy="PetHeading">
        Pets
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/pet/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Pet
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {petList && petList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Birthdate</th>
                <th>Type</th>
                <th>Owner</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {petList.map((pet, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/pet/${pet.id}`} color="link" size="sm">
                      {pet.id}
                    </Button>
                  </td>
                  <td>{pet.name}</td>
                  <td>{pet.birthdate ? <TextFormat type="date" value={pet.birthdate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{pet.type ? <Link to={`/pet-type/${pet.type.id}`}>{pet.type.id}</Link> : ''}</td>
                  <td>{pet.owner ? <Link to={`/owner/${pet.owner.id}`}>{pet.owner.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/pet/${pet.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/pet/${pet.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/pet/${pet.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Pets found</div>
        )}
      </div>
    </div>
  );
};

export default Pet;
