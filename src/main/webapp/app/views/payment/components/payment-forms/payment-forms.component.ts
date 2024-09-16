import { CommonModule } from '@angular/common';
import { Component, Output, EventEmitter, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-payment-forms',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './payment-forms.component.html',
  styleUrl: './payment-forms.component.scss',
})
export class PaymentFormsComponent {
  @Output() paymentForms = new EventEmitter<any>();
  router = inject(Router);

  paymentForm = new FormGroup({
    address: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.maxLength(600)] }),
    phoneNumber: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.pattern('^[0-9]{10}$')] }),
    cartNumber: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.pattern('^[0-9]{16}$')] }),
    dateExpiration: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern('^(0[1-9]|1[0-2])/([0-9]{2})$')],
    }),
    nameCarte: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    codeCart: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.pattern('^[0-9]{3}$')] }),
  });

  sendForms(): void {
    if (this.paymentForm.valid) {
      this.paymentForms.emit(this.paymentForm.value);
    } else {
      this.paymentForm.markAllAsTouched();
    }
  }

  backToCart(): void {
    this.router.navigate(['/cart']);
  }
}
