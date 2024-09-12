import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { PaymentFormsComponent } from './components/payment-forms/payment-forms.component';
import { ReactiveFormsModule } from '@angular/forms';
import { CartItem } from 'app/models/cart-item.model';
import { CartService } from 'app/services/cart.service';
import { PaymentService } from 'app/services/payment.service';
import { Payment, PaymentProduct } from 'app/models/payment.model';
import { AccountService } from 'app/core/auth/account.service';

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

  constructor(
    private cartService: CartService,
    private paymentService: PaymentService,
  ) {}

  ngOnInit(): void {
    this.cartItems = this.cartService.getCart();
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
        if (response?.cartList) {
          alert('There is no product in stock');
          this.cartService.updateCartAfterStockCheck(response.cartList);
        } else {
          alert('Payment done!');
        }
      },
      error(error) {
        alert('Error to process');
      },
    });
  }
}
