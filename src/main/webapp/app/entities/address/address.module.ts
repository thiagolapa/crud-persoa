import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PersonSharedModule } from 'app/shared/shared.module';
import { AddressComponent } from './address.component';
import { AddressDetailComponent } from './address-detail.component';
import { AddressUpdateComponent } from './address-update.component';
import { AddressDeletePopupComponent, AddressDeleteDialogComponent } from './address-delete-dialog.component';
import { addressRoute, addressPopupRoute } from './address.route';

const ENTITY_STATES = [...addressRoute, ...addressPopupRoute];

@NgModule({
  imports: [PersonSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    AddressComponent,
    AddressDetailComponent,
    AddressUpdateComponent,
    AddressDeleteDialogComponent,
    AddressDeletePopupComponent
  ],
  entryComponents: [AddressDeleteDialogComponent]
})
export class PersonAddressModule {}
