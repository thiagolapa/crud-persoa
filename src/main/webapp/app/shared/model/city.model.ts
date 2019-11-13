import { Moment } from 'moment';
import { IState } from 'app/shared/model/state.model';
import { IAddress } from 'app/shared/model/address.model';

export interface ICity {
  id?: number;
  name?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
  states?: IState[];
  addresses?: IAddress;
}

export class City implements ICity {
  constructor(
    public id?: number,
    public name?: string,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public states?: IState[],
    public addresses?: IAddress
  ) {}
}
