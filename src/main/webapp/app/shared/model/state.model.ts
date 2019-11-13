import { Moment } from 'moment';
import { ICountry } from 'app/shared/model/country.model';
import { ICity } from 'app/shared/model/city.model';

export interface IState {
  id?: number;
  name?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
  countries?: ICountry[];
  cities?: ICity;
}

export class State implements IState {
  constructor(
    public id?: number,
    public name?: string,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public countries?: ICountry[],
    public cities?: ICity
  ) {}
}
