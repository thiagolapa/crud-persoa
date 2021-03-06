import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { ICity, City } from 'app/shared/model/city.model';
import { CityService } from './city.service';
import { IState } from 'app/shared/model/state.model';
import { StateService } from 'app/entities/state/state.service';
import { ICountry } from 'app/shared/model/country.model';
import { CountryService } from 'app/entities/country/country.service';


@Component({
  selector: 'jhi-city-update',
  templateUrl: './city-update.component.html'
})
export class CityUpdateComponent implements OnInit {
  isSaving: boolean;

  state: IState[];
  countries: ICountry[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    stateId: [null, Validators.required],
    countryId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected cityService: CityService,
    protected stateService: StateService,
    protected countryService: CountryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ city }) => {
      this.updateForm(city);
    });
    this.countryService
    .query()
    .subscribe((res: HttpResponse<ICountry[]>) => (this.countries = res.body), (res: HttpErrorResponse) => this.onError(res.message));

    this.stateService
      .query()
      .subscribe((res: HttpResponse<IState[]>) => (this.state = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(city: ICity) {
    this.editForm.patchValue({
      id: city.id,
      name: city.name,
      stateId: city.stateId,
      countryId: city.countryId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const city = this.createFromForm();
    if (city.id !== undefined) {
      this.subscribeToSaveResponse(this.cityService.update(city));
    } else {
      this.subscribeToSaveResponse(this.cityService.create(city));
    }
  }

  private createFromForm(): ICity {
    return {
      ...new City(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      stateId: this.editForm.get(['stateId']).value,
      countryId: this.editForm.get(['countryId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICity>>) {
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

  trackStateById(index: number, item: IState) {
    return item.id;
  }
}
