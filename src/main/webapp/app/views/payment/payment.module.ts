import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PaymentRoutingModule } from './payment-routing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { PaymentFormsComponent } from './components/payment-forms/payment-forms.component';
import { PaymentComponent } from './payment.component';

@NgModule({
  declarations: [PaymentFormsComponent, PaymentComponent],
  imports: [CommonModule, PaymentRoutingModule, ReactiveFormsModule, PaymentFormsComponent, PaymentComponent],
  exports: [PaymentComponent, PaymentFormsComponent],
})
export class PaymentModule {}
