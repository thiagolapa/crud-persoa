import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPerson } from 'app/shared/model/person.model';
import { IAddress } from 'app/shared/model/address.model';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

type EntityResponseType = HttpResponse<IPerson>;
type EntityArrayResponseType = HttpResponse<IPerson[]>;
type EntityAddressArrayResponseType = HttpResponse<IAddress[]>;

@Injectable({ providedIn: 'root' })
export class PersonService {
  public resourceUrl = SERVER_API_URL + 'api/people';
  public resourceUrlAddress = SERVER_API_URL + 'api/addresses';

  constructor(protected http: HttpClient) {}

  create(person: IPerson): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(person);
    return this.http
      .post<IPerson>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(person: IPerson): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(person);
    return this.http
      .put<IPerson>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPerson>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPerson[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findAddressesByPersonId(id: number): Observable<EntityAddressArrayResponseType> {
    return this.http
      .get<IAddress[]>(`${this.resourceUrlAddress}/${id}/addresses`, { observe: 'response' })
      .pipe(map((res: EntityAddressArrayResponseType) => this.convertResponseAddress(res)));
  }

  protected convertDateFromClient(person: IPerson): IPerson {
    const copy: IPerson = Object.assign({}, person, {
      dateOfBirth: person.dateOfBirth != null ? moment(person.dateOfBirth, DATE_TIME_FORMAT).toJSON() : null,
      createdAt: person.createdAt != null ? moment(person.createdAt, DATE_TIME_FORMAT).toJSON() : null,
      updatedAt: person.updatedAt != null ? moment(person.updatedAt, DATE_TIME_FORMAT).toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth != null ? moment(res.body.dateOfBirth) : null;
      res.body.createdAt = res.body.createdAt != null ? moment(res.body.createdAt) : null;
      res.body.updatedAt = res.body.updatedAt != null ? moment(res.body.updatedAt) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((person: IPerson) => {
        person.dateOfBirth = person.dateOfBirth != null ? moment(person.dateOfBirth) : null;
        person.createdAt = person.createdAt != null ? moment(person.createdAt) : null;
        person.updatedAt = person.updatedAt != null ? moment(person.updatedAt) : null;
      });
    }
    return res;
  }

  protected convertResponseAddress(res: EntityAddressArrayResponseType): EntityAddressArrayResponseType {
    const body: IAddress[] = res.body;
    return res.clone({ body });
  }

}
