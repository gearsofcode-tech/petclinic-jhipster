import { IPet } from 'app/shared/model/pet.model';

export interface IOwner {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  address?: string | null;
  city?: string | null;
  telephone?: string | null;
  pets?: IPet[] | null;
}

export const defaultValue: Readonly<IOwner> = {};
