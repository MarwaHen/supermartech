import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { BehaviorSubject, Observable } from 'rxjs';

export interface Filter {
  name?: string;
  sub_cat?: number;
  brand?: string[];
  min_price?: number;
  max_price?: number;
  promo?: boolean;
  added_after?: Date;
  page?: number;
  size?: number;
}

@Injectable({
  providedIn: 'root',
})
export class FilterService {
  public filter$: Observable<Filter>;
  protected applicationConfigService = inject(ApplicationConfigService);
  protected resourceUrl = this.applicationConfigService.getEndpointFor('/api/filter');
  private filterSubject = new BehaviorSubject<Filter>({
    name: '',
    sub_cat: -1,
    min_price: 0,
    max_price: -1,
    promo: false,
    brand: [],
    added_after: new Date('1999-12-30'),
    page: 0,
    size: 6,
  });

  constructor(private http: HttpClient) {
    this.filter$ = this.filterSubject.asObservable();
  }

  getFilter(): Filter {
    return this.filterSubject.getValue();
  }

  filterProducts(): Observable<any> {
    return this.http.post<any>(this.resourceUrl, this.getFilter());
  }

  updateFilter(partialFilter: Partial<Filter>): void {
    const current = this.filterSubject.getValue();
    const newFilter = { ...current, ...partialFilter };
    this.filterSubject.next(newFilter);
  }

  resetFilter(): void {
    this.updateFilter({
      name: '',
      min_price: 0,
      max_price: -1,
      promo: false,
      brand: [],
      added_after: new Date('1999-12-30'),
      page: 0,
      size: 6,
    });
  }
}
