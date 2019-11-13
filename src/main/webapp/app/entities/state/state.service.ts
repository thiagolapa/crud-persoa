import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IState } from 'app/shared/model/state.model';

type EntityResponseType = HttpResponse<IState>;
type EntityArrayResponseType = HttpResponse<IState[]>;

@Injectable({ providedIn: 'root' })
export class StateService {
  public resourceUrl = SERVER_API_URL + 'api/states';

  constructor(protected http: HttpClient) {}

  create(state: IState): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(state);
    return this.http
      .post<IState>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(state: IState): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(state);
    return this.http
      .put<IState>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IState>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IState[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(state: IState): IState {
    const copy: IState = Object.assign({}, state, {
      createdAt: state.createdAt != null && state.createdAt.isValid() ? state.createdAt.toJSON() : null,
      updatedAt: state.updatedAt != null && state.updatedAt.isValid() ? state.updatedAt.toJSON() : null
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
      res.body.forEach((state: IState) => {
        state.createdAt = state.createdAt != null ? moment(state.createdAt) : null;
        state.updatedAt = state.updatedAt != null ? moment(state.updatedAt) : null;
      });
    }
    return res;
  }
}
