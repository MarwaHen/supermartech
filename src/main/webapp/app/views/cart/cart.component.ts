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
  cartItems: CartItem[] = [];
  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.cartItems = this.cartService.getCart();
  }

  updateTotalPrice(): number {
    return this.cartService.getTotalPrice();
  }
}
