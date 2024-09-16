import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartListComponent } from './components/cart-list/cart-list.component';
import { CartSummaryComponent } from './components/cart-summary/cart-summary.component';
import { CartItem } from 'app/models/cart-item.model';
import { CartService } from 'app/services/cart.service';

@Component({
  selector: 'jhi-cart',
  standalone: true,
  imports: [CommonModule, CartListComponent, CartSummaryComponent],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss',
})
export class CartComponent implements OnInit {
  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): CartItem[] {
    const cart = this.cartService.getCart();
    if (cart.length > 0) {
      return cart;
    } else {
      return [];
    }
  }

  updateTotalPrice(): number {
    return this.cartService.getTotalPrice();
  }
}
