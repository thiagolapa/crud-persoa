import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ICity } from 'app/shared/model/city.model';
import { CityService } from './city.service';

@Component({
  selector: 'jhi-city',
  templateUrl: './city.component.html'
})
export class CityComponent implements OnInit, OnDestroy {
  cities: ICity[];
  eventSubscriber: Subscription;

  constructor(protected cityService: CityService, protected eventManager: JhiEventManager) {}

  loadAll() {
    this.cityService.query().subscribe((res: HttpResponse<ICity[]>) => {
      this.cities = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInCities();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICity) {
    return item.id;
  }

  registerChangeInCities() {
    this.eventSubscriber = this.eventManager.subscribe('cityListModification', () => this.loadAll());
  }
}
