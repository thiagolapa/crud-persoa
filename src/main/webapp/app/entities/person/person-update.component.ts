import { Component, OnInit, ViewChild } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IPerson, Person } from 'app/shared/model/person.model';
import { PersonService } from './person.service';
import { CpfValidator } from './cpfValidator.Class';
import { IAddress, Address } from 'app/shared/model/address.model';
import { NgForm } from '@angular/forms';
import { ICity } from 'app/shared/model/city.model';
import { filter, map } from 'rxjs/operators';
import { CityService } from 'app/entities/city/city.service';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html',
  styleUrls: ['person.scss']
})

export class PersonUpdateComponent implements OnInit {
  isSaving: boolean;
  validCPF: boolean;
  isErrorCpf: any;
  address: Address = new Address();
  person: Person = new Person();
  editable: boolean;
  cities: ICity[];
  loadingNewAddress: boolean;

  @ViewChild('editForm', { static: false }) public editForm: NgForm;

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected personService: PersonService,
    protected activatedRoute: ActivatedRoute,
    protected cityService: CityService
  ) {}

  ngOnInit() {
    this.validCPF = false;
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ person, editable }) => {
      this.person = person;
      this.editable = editable;
      this.loadingNewAddress = false;
      if (person.id !== undefined) {
        this.person.addresses = person.addresses;
      } else {
        this.person.addresses = new Array<Address>();
      }
      this.address = new Address();
    });

    this.cityService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICity[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICity[]>) => response.body)
      )
      .subscribe((res: ICity[]) => (this.cities = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    if (this.person.id !== undefined) {
      this.subscribeToSaveResponse(this.personService.update(this.person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(this.person));
    }
  }

  removeAddress(streetAddress: string) {
    this.person.addresses = this.person.addresses.filter(address => address.streetAddress !== streetAddress);
  }

  addAddress() {
    if (
      this.address.streetAddress !== undefined &&
      this.address.streetAddress !== '' &&
      this.address.postalCode !== undefined &&
      this.address.postalCode !== '' &&
      this.address.typeAddress !== undefined &&
      this.address.district !== undefined &&
      this.address.district !== '' &&
      this.address.cityId !== undefined
    ) {
      this.person.addresses.push(this.address);
      this.address = new Address();
      this.loadingNewAddress = false;
    }
  }

  newAddress() {
    this.address = new Address();
    this.loadingNewAddress = true;
  }

  verifyAddress() {
    return !this.person.addresses.some(address => address.typeAddress === this.address.typeAddress);
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

  checkCpf(cpf: string) {
    if (cpf !== undefined && cpf !== null && cpf.length > 0) {
      const cpfValidator = new CpfValidator();
      this.validCPF = cpfValidator.checkCPF(cpf);
    }
  }

  checkInput() {
    if (this.person.cpf !== undefined && this.person.cpf.length > 0) {
      if (this.person.cpf.length < 11) {
        this.isErrorCpf = true;
      } else {
        this.isErrorCpf = false;
      }
    }
  }

  formIsInvalid(editForm): boolean {
    if (!editForm) {
      return false;
    }

    return (
      editForm.form.invalid ||
      this.isErrorCpf ||
      this.person.addresses.length === 0 ||
      (!this.validCPF && editForm.controls.cpf && editForm.controls.cpf.dirty)
    );
  }

  trackId(index: number, item: IAddress) {
    return item.id;
  }

  trackCityById(index: number, item: ICity) {
    return item.id;
  }
}
