import { Moment } from 'moment';
import { IPerson } from 'app/shared/model/person.model';
import { ICity } from 'app/shared/model/city.model';
import { TypeAddress } from 'app/shared/model/enumerations/type-address.model';

export interface IAddress {
  id?: number;
  streetAddress?: string;
  postalCode?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
  typeAddress?: TypeAddress;
  number?: number;
  complement?: string;
  district?: string;
  people?: IPerson[];
  cities?: ICity[];
}

export class Address implements IAddress {
  constructor(
    public id?: number,
    public streetAddress?: string,
    public postalCode?: string,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public typeAddress?: TypeAddress,
    public number?: number,
    public complement?: string,
    public district?: string,
    public people?: IPerson[],
    public cities?: ICity[]
  ) {}
}
