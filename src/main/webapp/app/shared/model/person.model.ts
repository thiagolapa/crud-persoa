import { Moment } from 'moment';
import { IAddress } from 'app/shared/model/address.model';
import { Sex } from 'app/shared/model/enumerations/sex.model';

export interface IPerson {
  id?: number;
  name?: string;
  sex?: Sex;
  email?: string;
  dateOfBirth?: Moment;
  cpf?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
  naturalness?: string;
  nationality?: string;
  addresses?: IAddress[];
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public name?: string,
    public sex?: Sex,
    public email?: string,
    public dateOfBirth?: Moment,
    public cpf?: string,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public naturalness?: string,
    public nationality?: string,
    public addresses?: IAddress[]
  ) {}
}
