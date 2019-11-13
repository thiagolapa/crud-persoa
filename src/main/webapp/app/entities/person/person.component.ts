import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IPerson } from 'app/shared/model/person.model';
import { PersonService } from './person.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import * as moment from 'moment';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiParseLinks, JhiAlertService } from 'ng-jhipster';
// import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-person',
  templateUrl: './person.component.html',
  styleUrls: ['person.scss']
})

export class PersonComponent implements OnInit, OnDestroy {
  private readonly TO_DATE_KEY = 'to_date';
  private readonly FROM_DATE_KEY = 'from_date';
  people: IPerson[];
  eventSubscriber: Subscription;
  search = '';
  fromDate: any;
  toDate: any;
  dateFilter = {
    [this.FROM_DATE_KEY]: null,
    [this.TO_DATE_KEY]: null
  };
  totalItems: any;
  selectedSex: string;
  page: any;
  itemsPerPage: any;
  predicate: any;
  reverse: any;
  previousPage: any;
  routeData: any;
  links: any;
  loading = false;
//   currentAccount: any;

  constructor(
  protected personService: PersonService,
  protected eventManager: JhiEventManager,
  protected router: Router,
  protected activatedRoute: ActivatedRoute,
  protected jhiAlertService: JhiAlertService,
//   protected accountService: AccountService,
  protected parseLinks: JhiParseLinks) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.fromDate = data.pagingParams.fromDate;
      this.toDate = data.pagingParams.toDate;
    });
  }

  loadAll() {
    this.loading = true;
    const filters = this.getFilters();
    this.personService
    .query({
      ...filters,
      page: this.page -1,
      size: this.itemsPerPage,
      q: this.search,
      sex: this.selectedSex ? this.selectedSex : '',
      sort: this.sort()
    })
    .subscribe(
      (res: HttpResponse<IPerson[]>) => this.paginatePeople(res.body, res.headers),
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  ngOnInit() {
    this.selectedSex = null;
    this.loadAll();
//     this.accountService.identity().then(account => {
//       this.currentAccount = account;
//     });
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

  doSearch() {
    this.page = 1;
    this.transition();
  }

  transition() {
    this.router.navigate(['/person'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  onSearchUpdate(term: string) {
    this.search = term;
    this.page = 0;
    this.loadAll();
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '/person',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  protected paginatePeople(data: IPerson[], headers: HttpHeaders) {
    this.loading = false;
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.people = data;
  }

  changeTab(status: string) {
    this.selectedSex = status;
    this.loadAll();
  }

  onChangeDate(dateToChange: string, filterKey: string) {
    if (dateToChange) {
      this.dateFilter[filterKey] = this.formatToServer(dateToChange, filterKey);
    } else {
      this.dateFilter[filterKey] = null;
    }
    this.loadFilters();
  }

  formatToServer(date: string, filterKey: string) {
    if (date === '' || date === null) {
      return null;
    }
    const dateFormatted = moment(date).utc();

    if (filterKey === this.TO_DATE_KEY) {
      dateFormatted
        .hour(23)
        .minutes(59)
        .seconds(59)
        .milliseconds(99);
    }

    return dateFormatted.format();
  }

  private loadFilters(): void {
    this.page = 0;
    this.loadAll();
  }

  registerFromDateChange(fromDate) {
    this.fromDate = fromDate;
    this.onChangeDate(fromDate, this.FROM_DATE_KEY);
  }

  registerToDateChange(toDate) {
    this.toDate = toDate;
    this.onChangeDate(toDate, this.TO_DATE_KEY);
  }

  private getAvailableFilters(formGroup): object {
    const filters = formGroup;
    Object.keys(filters).forEach(key => filters[key] == null && delete filters[key]);
    return filters;
  }

  private getFilters() {
    const dateFilters = { ...this.getAvailableFilters(this.dateFilter) };
    return { ...dateFilters };
  }

  protected onError(errorMessage: string) {
    this.loading = false;
    this.jhiAlertService.error(errorMessage, null, null);
  }

}
