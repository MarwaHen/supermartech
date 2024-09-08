import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IImagePro, NewImagePro } from '../image-pro.model';

export type PartialUpdateImagePro = Partial<IImagePro> & Pick<IImagePro, 'id'>;

export type EntityResponseType = HttpResponse<IImagePro>;
export type EntityArrayResponseType = HttpResponse<IImagePro[]>;

@Injectable({ providedIn: 'root' })
export class ImageProService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/image-pros');

  create(imagePro: NewImagePro): Observable<EntityResponseType> {
    return this.http.post<IImagePro>(this.resourceUrl, imagePro, { observe: 'response' });
  }

  update(imagePro: IImagePro): Observable<EntityResponseType> {
    return this.http.put<IImagePro>(`${this.resourceUrl}/${this.getImageProIdentifier(imagePro)}`, imagePro, { observe: 'response' });
  }

  partialUpdate(imagePro: PartialUpdateImagePro): Observable<EntityResponseType> {
    return this.http.patch<IImagePro>(`${this.resourceUrl}/${this.getImageProIdentifier(imagePro)}`, imagePro, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IImagePro>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IImagePro[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getImageProIdentifier(imagePro: Pick<IImagePro, 'id'>): number {
    return imagePro.id;
  }

  compareImagePro(o1: Pick<IImagePro, 'id'> | null, o2: Pick<IImagePro, 'id'> | null): boolean {
    return o1 && o2 ? this.getImageProIdentifier(o1) === this.getImageProIdentifier(o2) : o1 === o2;
  }

  addImageProToCollectionIfMissing<Type extends Pick<IImagePro, 'id'>>(
    imageProCollection: Type[],
    ...imageProsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const imagePros: Type[] = imageProsToCheck.filter(isPresent);
    if (imagePros.length > 0) {
      const imageProCollectionIdentifiers = imageProCollection.map(imageProItem => this.getImageProIdentifier(imageProItem));
      const imageProsToAdd = imagePros.filter(imageProItem => {
        const imageProIdentifier = this.getImageProIdentifier(imageProItem);
        if (imageProCollectionIdentifiers.includes(imageProIdentifier)) {
          return false;
        }
        imageProCollectionIdentifiers.push(imageProIdentifier);
        return true;
      });
      return [...imageProsToAdd, ...imageProCollection];
    }
    return imageProCollection;
  }
}
