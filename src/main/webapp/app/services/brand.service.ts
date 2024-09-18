import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class BrandService {
  protected applicationConfigService = inject(ApplicationConfigService);
  protected resourceUrl = this.applicationConfigService.getEndpointFor('/api/brand');

  constructor(private http: HttpClient) {}

  getAllBrands(): Observable<any> {
    return this.http.get<any>(this.resourceUrl);
  }
}
