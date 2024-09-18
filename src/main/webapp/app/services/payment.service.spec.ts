import { TestBed } from '@angular/core/testing';

import { CartService } from './cart.service';
import { PaymentService } from './payment.service';

describe('CartService', () => {
  let service: PaymentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaymentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
