import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPerson } from 'app/shared/model/person.model';
import { IAddress } from 'app/shared/model/address.model';
import { PersonService } from './person.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-person-detail',
  templateUrl: './person-detail.component.html'
})
export class PersonDetailComponent implements OnInit {
  person: IPerson;
  addresses: IAddress[];

  constructor(protected activatedRoute: ActivatedRoute, protected personService: PersonService) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ person }) => {
      this.person = person;
      this.loadAddresses(this.person.id);
    });
  }

  loadAddresses(id) {
    this.personService.findAddressesByPersonId(id).subscribe((addressResponse: HttpResponse<IAddress[]>) => {
      this.addresses = addressResponse.body;
    });
  }

  previousState() {
    window.history.back();
  }

  trackId(index: number, item: IAddress) {
      return item.id;
  }

}
