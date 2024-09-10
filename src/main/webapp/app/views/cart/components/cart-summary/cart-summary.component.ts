import { Component, Input } from '@angular/core';

@Component({
  selector: 'jhi-cart-summary',
  standalone: true,
  imports: [],
  templateUrl: './cart-summary.component.html',
  styleUrl: './cart-summary.component.scss',
})
export class CartSummaryComponent {
  @Input()
  totalPrice!: number;

  validateCart(): void {
    alert('Payment');
  }
}
