import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { PaymentFormsComponent } from './components/payment-forms/payment-forms.component';
import { ReactiveFormsModule } from '@angular/forms';
import { CartItem } from 'app/models/cart-item.model';
import { CartService } from 'app/services/cart.service';
import { PaymentService } from 'app/services/payment.service';
import { Payment, PaymentProduct } from 'app/models/payment.model';
import { AccountService } from 'app/core/auth/account.service';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-payment',
  standalone: true,
  imports: [CommonModule, PaymentFormsComponent, ReactiveFormsModule],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.scss',
})
export class PaymentComponent implements OnInit {
  account = inject(AccountService).trackCurrentAccount();
  cartItems: CartItem[] = [];
  private readonly router = inject(Router);

  constructor(
    private cartService: CartService,
    private paymentService: PaymentService,
  ) {}

  ngOnInit(): void {
    this.cartItems = this.cartService.getCart();
    if (!(this.account()?.id && this.cartItems.length > 0)) {
      this.router.navigate(['/']);
    }
  }

  paymentProcess(paymentForms: any): void {
    const paymentProducts: PaymentProduct[] = this.cartItems.map(item => ({
      product_id: item.productId,
      quantity: item.quantity,
    }));

    const clientId = this.account()?.id;

    const paymentJson: Payment = {
      id_client: clientId,
      cart_list: paymentProducts,
      address: paymentForms.address,
      phone_number: paymentForms.phoneNumber,
    };

    this.paymentService.paymentProcess(paymentJson).subscribe({
      next: response => {
        if (response?.cart_list) {
          this.cartService.updateCartAfterStockCheck(response.cart_list);
          const namesList = this.getProductNames(response.cart_list);
          alert(`Some products are missing in stock: ${namesList.join(', ')}`);
          this.router.navigate(['/']);
        } else {
          this.paymentService.completePayment();

          this.router.navigate(['/payment/paymentConfirmation', response?.id]).then(() => {
            this.cartService.clearCart().then(() => {
              this.cartItems = this.cartService.getCart();
            });
          });
        }
      },
      error(error) {
        alert('Error processing payment. Please try again!');
      },
    });
  }

  getProductNames(items: any): string[] {
    const namesList: string[] = [];
    items.forEach((item: { product_name: string }) => {
      namesList.push(item.product_name);
    });
    return namesList;
  }
}
