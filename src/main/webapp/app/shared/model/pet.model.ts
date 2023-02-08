import dayjs from 'dayjs';
import { IVisit } from 'app/shared/model/visit.model';
import { IPetType } from 'app/shared/model/pet-type.model';
import { IOwner } from 'app/shared/model/owner.model';

export interface IPet {
  id?: number;
  name?: string | null;
  birthdate?: string | null;
  visits?: IVisit[] | null;
  type?: IPetType | null;
  owner?: IOwner | null;
}

export const defaultValue: Readonly<IPet> = {};
