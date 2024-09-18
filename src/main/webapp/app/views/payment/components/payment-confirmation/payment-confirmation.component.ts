import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { PaymentService } from 'app/services/payment.service';

@Component({
  selector: 'jhi-payment-confirmation',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './payment-confirmation.component.html',
  styleUrl: './payment-confirmation.component.scss',
})
export class PaymentConfirmationComponent implements OnInit {
  orderId?: number;
  constructor(
    private paymentService: PaymentService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    if (!this.paymentService.isPaymentCompleted()) {
      this.router.navigate(['/payment']);
    } else {
      this.orderId = +this.route.snapshot.paramMap.get('id')!;
    }
  }
}
