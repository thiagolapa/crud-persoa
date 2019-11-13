import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IState } from 'app/shared/model/state.model';
import { StateService } from './state.service';

@Component({
  selector: 'jhi-state',
  templateUrl: './state.component.html'
})
export class StateComponent implements OnInit, OnDestroy {
  states: IState[];
  eventSubscriber: Subscription;

  constructor(protected stateService: StateService, protected eventManager: JhiEventManager) {}

  loadAll() {
    this.stateService.query().subscribe((res: HttpResponse<IState[]>) => {
      this.states = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInStates();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IState) {
    return item.id;
  }

  registerChangeInStates() {
    this.eventSubscriber = this.eventManager.subscribe('stateListModification', () => this.loadAll());
  }
}
