import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { PersonSharedModule } from 'app/shared/shared.module';
import { PersonCoreModule } from 'app/core/core.module';
import { PersonAppRoutingModule } from './app-routing.module';
import { PersonHomeModule } from './home/home.module';
import { PersonEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    PersonSharedModule,
    PersonCoreModule,
    PersonHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    PersonEntityModule,
    PersonAppRoutingModule
  ],
  declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [JhiMainComponent]
})
export class PersonAppModule {}
