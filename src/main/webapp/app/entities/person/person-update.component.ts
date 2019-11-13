import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IPerson, Person } from 'app/shared/model/person.model';
import { PersonService } from './person.service';
import { IAddress } from 'app/shared/model/address.model';
import { AddressService } from 'app/entities/address/address.service';

@Component({
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html'
})
export class PersonUpdateComponent implements OnInit {
  isSaving: boolean;

  addresses: IAddress[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    sex: [],
    email: [null, [Validators.maxLength(100)]],
    dateOfBirth: [null, [Validators.required]],
    cpf: [null, [Validators.required, Validators.maxLength(20)]],
    createdAt: [],
    updatedAt: [],
    naturalness: [],
    nationality: [],
    addresses: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected personService: PersonService,
    protected addressService: AddressService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ person }) => {
      this.updateForm(person);
    });
    this.addressService
      .query()
      .subscribe((res: HttpResponse<IAddress[]>) => (this.addresses = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(person: IPerson) {
    this.editForm.patchValue({
      id: person.id,
      name: person.name,
      sex: person.sex,
      email: person.email,
      dateOfBirth: person.dateOfBirth != null ? person.dateOfBirth.format(DATE_TIME_FORMAT) : null,
      cpf: person.cpf,
      createdAt: person.createdAt != null ? person.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: person.updatedAt != null ? person.updatedAt.format(DATE_TIME_FORMAT) : null,
      naturalness: person.naturalness,
      nationality: person.nationality,
      addresses: person.addresses
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const person = this.createFromForm();
    if (person.id !== undefined) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  private createFromForm(): IPerson {
    return {
      ...new Person(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      sex: this.editForm.get(['sex']).value,
      email: this.editForm.get(['email']).value,
      dateOfBirth:
        this.editForm.get(['dateOfBirth']).value != null ? moment(this.editForm.get(['dateOfBirth']).value, DATE_TIME_FORMAT) : undefined,
      cpf: this.editForm.get(['cpf']).value,
      createdAt:
        this.editForm.get(['createdAt']).value != null ? moment(this.editForm.get(['createdAt']).value, DATE_TIME_FORMAT) : undefined,
      updatedAt:
        this.editForm.get(['updatedAt']).value != null ? moment(this.editForm.get(['updatedAt']).value, DATE_TIME_FORMAT) : undefined,
      naturalness: this.editForm.get(['naturalness']).value,
      nationality: this.editForm.get(['nationality']).value,
      addresses: this.editForm.get(['addresses']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackAddressById(index: number, item: IAddress) {
    return item.id;
  }
}
