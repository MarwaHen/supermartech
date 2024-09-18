import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImagePro } from '../image-pro.model';
import { ImageProService } from '../service/image-pro.service';

const imageProResolve = (route: ActivatedRouteSnapshot): Observable<null | IImagePro> => {
  const id = route.params.id;
  if (id) {
    return inject(ImageProService)
      .find(id)
      .pipe(
        mergeMap((imagePro: HttpResponse<IImagePro>) => {
          if (imagePro.body) {
            return of(imagePro.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default imageProResolve;
