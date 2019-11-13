import { Moment } from 'moment';

export interface ICountry {
  id?: number;
  name?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
}

export class Country implements ICountry {
  constructor(public id?: number, public name?: string, public createdAt?: Moment, public updatedAt?: Moment) {}
}
