import { Moment } from 'moment';
import { IState } from 'app/shared/model/state.model';

export interface ICountry {
  id?: number;
  name?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
  states?: IState;
}

export class Country implements ICountry {
  constructor(public id?: number, public name?: string, public createdAt?: Moment, public updatedAt?: Moment, public states?: IState) {}
}
