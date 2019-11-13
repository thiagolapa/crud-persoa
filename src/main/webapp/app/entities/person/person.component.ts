import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IPerson } from 'app/shared/model/person.model';
import { PersonService } from './person.service';

@Component({
  selector: 'jhi-person',
  templateUrl: './person.component.html'
})
export class PersonComponent implements OnInit, OnDestroy {
  people: IPerson[];
  eventSubscriber: Subscription;

  constructor(protected personService: PersonService, protected eventManager: JhiEventManager) {}

  loadAll() {
    this.personService.query().subscribe((res: HttpResponse<IPerson[]>) => {
      this.people = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInPeople();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPerson) {
    return item.id;
  }

  registerChangeInPeople() {
    this.eventSubscriber = this.eventManager.subscribe('personListModification', () => this.loadAll());
  }
}
