import { IVet } from 'app/shared/model/vet.model';

export interface ISpecialty {
  id?: number;
  name?: string | null;
  experts?: IVet[] | null;
}

export const defaultValue: Readonly<ISpecialty> = {};
