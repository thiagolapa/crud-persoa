import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'country',
        loadChildren: () => import('./country/country.module').then(m => m.PersonCountryModule)
      },
      {
        path: 'state',
        loadChildren: () => import('./state/state.module').then(m => m.PersonStateModule)
      },
      {
        path: 'city',
        loadChildren: () => import('./city/city.module').then(m => m.PersonCityModule)
      },
      {
        path: 'address',
        loadChildren: () => import('./address/address.module').then(m => m.PersonAddressModule)
      },
      {
        path: 'person',
        loadChildren: () => import('./person/person.module').then(m => m.PersonPersonModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PersonEntityModule {}
