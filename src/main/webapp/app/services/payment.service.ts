import { Injectable, inject } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { CartItem } from '../models/cart-item.model';
import { IProduct } from 'app/view/product/product.model';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Payment, PaymentProduct } from 'app/models/payment.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payment');
  private paymentCompleted = false;

  paymentProcess(payment: Payment): Observable<any> {
    return this.http.post<any>(this.resourceUrl, payment);
  }

  completePayment(): void {
    this.paymentCompleted = true;
  }

  isPaymentCompleted(): boolean {
    return this.paymentCompleted;
  }
}
