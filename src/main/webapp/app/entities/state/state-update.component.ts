import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IState, State } from 'app/shared/model/state.model';
import { StateService } from './state.service';
import { ICountry } from 'app/shared/model/country.model';
import { CountryService } from 'app/entities/country/country.service';

@Component({
  selector: 'jhi-state-update',
  templateUrl: './state-update.component.html'
})
export class StateUpdateComponent implements OnInit {
  isSaving: boolean;

  countries: ICountry[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    countryId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected stateService: StateService,
    protected countryService: CountryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ state }) => {
      this.updateForm(state);
    });

    this.countryService
    .query()
    .subscribe((res: HttpResponse<ICountry[]>) => (this.countries = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(state: IState) {
    this.editForm.patchValue({
      id: state.id,
      name: state.name,
      countryId: state.countryId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const state = this.createFromForm();
    if (state.id !== undefined) {
      this.subscribeToSaveResponse(this.stateService.update(state));
    } else {
      this.subscribeToSaveResponse(this.stateService.create(state));
    }
  }

  private createFromForm(): IState {
    return {
      ...new State(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      countryId: this.editForm.get(['countryId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IState>>) {
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

  trackCountryById(index: number, item: ICountry) {
    return item.id;
  }
}
