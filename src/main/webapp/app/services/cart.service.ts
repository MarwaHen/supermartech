import { Injectable, inject } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { CartItem } from '../models/cart-item.model';
import { IProduct } from 'app/view/product/product.model';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private cartCookieName = 'cartItems';
  constructor(private cookieService: CookieService) {}

  getCart(): CartItem[] {
    const cart = this.cookieService.get(this.cartCookieName);
    if (cart) {
      // eslint-disable-next-line @typescript-eslint/no-unsafe-return
      return JSON.parse(cart);
    } else {
      return [];
    }
  }

  addItem(newItem: CartItem): void {
    const cart = this.getCart();

    const existingItem = cart.find((item: any) => item.productId === newItem.productId);
    if (existingItem) {
      existingItem.quantity += newItem.quantity;
    } else {
      cart.push(newItem);
    }

    this.saveCart(cart);
  }

  saveCart(cart: CartItem[]): void {
    const cartJson = JSON.stringify(cart);
    this.cookieService.set(this.cartCookieName, cartJson, 7, '/');
  }

  removeItem(productId: number): void {
    let cart = this.getCart();
    cart = cart.filter((item: CartItem) => item.productId !== productId);
    this.saveCart(cart);
  }

  clearCart(): void {
    this.cookieService.delete(this.cartCookieName);
  }

  updateItem(updatedItem: CartItem): void {
    const items: CartItem[] = this.getCart();
    const index = items.findIndex(item => item.productId === updatedItem.productId);

    if (index !== -1) {
      items[index] = updatedItem;
      this.saveCart(items);
    }
  }

  getTotalPrice(): number {
    const cartItems = this.getCart();
    return cartItems.reduce((total, item) => total + item.price * item.quantity, 0);
  }

  updateCartAfterStockCheck(missingItems: { product_id: number; product_name: string; quantity: number }[]): void {
    const cart = this.getCart();

    missingItems.forEach(missingItem => {
      const cartItem = cart.find(item => item.productId === missingItem.product_id);
      if (cartItem) {
        if (cartItem.quantity > missingItem.quantity) {
          cartItem.quantity -= missingItem.quantity;
        } else {
          const index = cart.indexOf(cartItem);
          if (index !== -1) {
            cart.splice(index, 1);
          }
        }
      }
    });

    this.saveCart(cart);
  }
}
