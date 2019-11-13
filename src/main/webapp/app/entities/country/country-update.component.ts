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
import { ICountry, Country } from 'app/shared/model/country.model';
import { CountryService } from './country.service';
import { IState } from 'app/shared/model/state.model';
import { StateService } from 'app/entities/state/state.service';

@Component({
  selector: 'jhi-country-update',
  templateUrl: './country-update.component.html'
})
export class CountryUpdateComponent implements OnInit {
  isSaving: boolean;

  states: IState[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    createdAt: [],
    updatedAt: [],
    states: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected countryService: CountryService,
    protected stateService: StateService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ country }) => {
      this.updateForm(country);
    });
    this.stateService
      .query()
      .subscribe((res: HttpResponse<IState[]>) => (this.states = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(country: ICountry) {
    this.editForm.patchValue({
      id: country.id,
      name: country.name,
      createdAt: country.createdAt != null ? country.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: country.updatedAt != null ? country.updatedAt.format(DATE_TIME_FORMAT) : null,
      states: country.states
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const country = this.createFromForm();
    if (country.id !== undefined) {
      this.subscribeToSaveResponse(this.countryService.update(country));
    } else {
      this.subscribeToSaveResponse(this.countryService.create(country));
    }
  }

  private createFromForm(): ICountry {
    return {
      ...new Country(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      createdAt:
        this.editForm.get(['createdAt']).value != null ? moment(this.editForm.get(['createdAt']).value, DATE_TIME_FORMAT) : undefined,
      updatedAt:
        this.editForm.get(['updatedAt']).value != null ? moment(this.editForm.get(['updatedAt']).value, DATE_TIME_FORMAT) : undefined,
      states: this.editForm.get(['states']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICountry>>) {
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

  trackStateById(index: number, item: IState) {
    return item.id;
  }
}
