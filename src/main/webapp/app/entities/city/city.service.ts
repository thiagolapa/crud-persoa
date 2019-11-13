import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICity } from 'app/shared/model/city.model';

type EntityResponseType = HttpResponse<ICity>;
type EntityArrayResponseType = HttpResponse<ICity[]>;

@Injectable({ providedIn: 'root' })
export class CityService {
  public resourceUrl = SERVER_API_URL + 'api/cities';

  constructor(protected http: HttpClient) {}

  create(city: ICity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(city);
    return this.http
      .post<ICity>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(city: ICity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(city);
    return this.http
      .put<ICity>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICity>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICity[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(city: ICity): ICity {
    const copy: ICity = Object.assign({}, city, {
      createdAt: city.createdAt != null && city.createdAt.isValid() ? city.createdAt.toJSON() : null,
      updatedAt: city.updatedAt != null && city.updatedAt.isValid() ? city.updatedAt.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt != null ? moment(res.body.createdAt) : null;
      res.body.updatedAt = res.body.updatedAt != null ? moment(res.body.updatedAt) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((city: ICity) => {
        city.createdAt = city.createdAt != null ? moment(city.createdAt) : null;
        city.updatedAt = city.updatedAt != null ? moment(city.updatedAt) : null;
      });
    }
    return res;
  }
}
