import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Person } from 'app/shared/model/person.model';
import { PersonService } from './person.service';
import { PersonComponent } from './person.component';
import { PersonDetailComponent } from './person-detail.component';
import { PersonUpdateComponent } from './person-update.component';
import { PersonDeletePopupComponent } from './person-delete-dialog.component';
import { IPerson } from 'app/shared/model/person.model';

@Injectable({ providedIn: 'root' })
export class PersonResolve implements Resolve<IPerson> {
  constructor(private service: PersonService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPerson> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((person: HttpResponse<Person>) => person.body));
    }
    return of(new Person());
  }
}

export const personRoute: Routes = [
  {
    path: '',
    component: PersonComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'personApp.person.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PersonDetailComponent,
    resolve: {
      person: PersonResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'personApp.person.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PersonUpdateComponent,
    resolve: {
      person: PersonResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'personApp.person.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PersonUpdateComponent,
    resolve: {
      person: PersonResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'personApp.person.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const personPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PersonDeletePopupComponent,
    resolve: {
      person: PersonResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'personApp.person.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
