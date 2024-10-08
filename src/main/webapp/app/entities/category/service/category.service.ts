import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICategory } from '../category.model';

export type EntityResponseType = HttpResponse<ICategory>;
export type EntityArrayResponseType = HttpResponse<ICategory[]>;

@Injectable({ providedIn: 'root' })
export class CategoryService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/categories');

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getCategoryIdentifier(category: Pick<ICategory, 'id'>): number {
    return category.id;
  }

  compareCategory(o1: Pick<ICategory, 'id'> | null, o2: Pick<ICategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getCategoryIdentifier(o1) === this.getCategoryIdentifier(o2) : o1 === o2;
  }

  addCategoryToCollectionIfMissing<Type extends Pick<ICategory, 'id'>>(
    categoryCollection: Type[],
    ...categoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const categories: Type[] = categoriesToCheck.filter(isPresent);
    if (categories.length > 0) {
      const categoryCollectionIdentifiers = categoryCollection.map(categoryItem => this.getCategoryIdentifier(categoryItem));
      const categoriesToAdd = categories.filter(categoryItem => {
        const categoryIdentifier = this.getCategoryIdentifier(categoryItem);
        if (categoryCollectionIdentifiers.includes(categoryIdentifier)) {
          return false;
        }
        categoryCollectionIdentifiers.push(categoryIdentifier);
        return true;
      });
      return [...categoriesToAdd, ...categoryCollection];
    }
    return categoryCollection;
  }
}
