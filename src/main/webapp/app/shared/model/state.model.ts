import { Moment } from 'moment';

export interface IState {
  id?: number;
  name?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
  countryId?: number;
  countryName?: string;
}

export class State implements IState {
  constructor(
    public id?: number,
    public name?: string,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public countryId?: number,
    public countryName?: string
  ) {}
}
