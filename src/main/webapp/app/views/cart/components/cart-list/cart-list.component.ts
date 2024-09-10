import { Component, Input } from '@angular/core';
import { CartItem } from 'app/models/cart-item.model';
import { CartService } from 'app/views/cart/cart.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'jhi-cart-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart-list.component.html',
  styleUrl: './cart-list.component.scss',
})
export class CartListComponent {
  @Input()
  cartItems: CartItem[] = [];
  constructor(private cartService: CartService) {}

  removeItem(productId: number): void {
    this.cartService.removeItem(productId);
    this.cartItems = this.cartService.getCart();
  }

  increaseQuantity(item: CartItem): void {
    item.quantity++;
    this.cartService.updateItem(item);
  }

  decreaseQuantity(item: CartItem): void {
    if (item.quantity > 1) {
      item.quantity--;
      this.cartService.updateItem(item);
    } else {
      this.removeItem(item.productId);
    }
  }
  clearCart(): void {
    this.cartService.clearCart();
    this.cartItems = [];
  }
}
