import { Moment } from 'moment';

export interface ICity {
  id?: number;
  name?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
  stateId?: number;
  stateName?: string;
  countryId?: number;
  countryName?: string;
}

export class City implements ICity {
  constructor(
    public id?: number,
    public name?: string,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public stateId?: number,
    public stateName?: string,
    public countryId?: number,
    public countryName?: string
  ) {}
}
