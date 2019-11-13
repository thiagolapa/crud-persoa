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
import { IState, State } from 'app/shared/model/state.model';
import { StateService } from './state.service';
import { ICity } from 'app/shared/model/city.model';
import { CityService } from 'app/entities/city/city.service';

@Component({
  selector: 'jhi-state-update',
  templateUrl: './state-update.component.html'
})
export class StateUpdateComponent implements OnInit {
  isSaving: boolean;

  cities: ICity[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    createdAt: [],
    updatedAt: [],
    cities: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected stateService: StateService,
    protected cityService: CityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ state }) => {
      this.updateForm(state);
    });
    this.cityService
      .query()
      .subscribe((res: HttpResponse<ICity[]>) => (this.cities = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(state: IState) {
    this.editForm.patchValue({
      id: state.id,
      name: state.name,
      createdAt: state.createdAt != null ? state.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: state.updatedAt != null ? state.updatedAt.format(DATE_TIME_FORMAT) : null,
      cities: state.cities
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
      createdAt:
        this.editForm.get(['createdAt']).value != null ? moment(this.editForm.get(['createdAt']).value, DATE_TIME_FORMAT) : undefined,
      updatedAt:
        this.editForm.get(['updatedAt']).value != null ? moment(this.editForm.get(['updatedAt']).value, DATE_TIME_FORMAT) : undefined,
      cities: this.editForm.get(['cities']).value
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

  trackCityById(index: number, item: ICity) {
    return item.id;
  }
}
