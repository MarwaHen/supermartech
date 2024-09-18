import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubCategory } from '../sub-category.model';
import { SubCategoryService } from '../service/sub-category.service';

const subCategoryResolve = (route: ActivatedRouteSnapshot): Observable<null | ISubCategory> => {
  const id = route.params.id;
  if (id) {
    return inject(SubCategoryService)
      .find(id)
      .pipe(
        mergeMap((subCategory: HttpResponse<ISubCategory>) => {
          if (subCategory.body) {
            return of(subCategory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default subCategoryResolve;
