import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PersonSharedModule } from 'app/shared/shared.module';
import { CityComponent } from './city.component';
import { CityDetailComponent } from './city-detail.component';
import { CityUpdateComponent } from './city-update.component';
import { CityDeletePopupComponent, CityDeleteDialogComponent } from './city-delete-dialog.component';
import { cityRoute, cityPopupRoute } from './city.route';

const ENTITY_STATES = [...cityRoute, ...cityPopupRoute];

@NgModule({
  imports: [PersonSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [CityComponent, CityDetailComponent, CityUpdateComponent, CityDeleteDialogComponent, CityDeletePopupComponent],
  entryComponents: [CityDeleteDialogComponent]
})
export class PersonCityModule {}
