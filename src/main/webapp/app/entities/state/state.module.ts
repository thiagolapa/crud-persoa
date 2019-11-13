import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PersonSharedModule } from 'app/shared/shared.module';
import { StateComponent } from './state.component';
import { StateDetailComponent } from './state-detail.component';
import { StateUpdateComponent } from './state-update.component';
import { StateDeletePopupComponent, StateDeleteDialogComponent } from './state-delete-dialog.component';
import { stateRoute, statePopupRoute } from './state.route';

const ENTITY_STATES = [...stateRoute, ...statePopupRoute];

@NgModule({
  imports: [PersonSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [StateComponent, StateDetailComponent, StateUpdateComponent, StateDeleteDialogComponent, StateDeletePopupComponent],
  entryComponents: [StateDeleteDialogComponent]
})
export class PersonStateModule {}
