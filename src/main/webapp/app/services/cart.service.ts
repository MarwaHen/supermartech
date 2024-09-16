import { Injectable, inject } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { CartItem } from '../models/cart-item.model';
import { IProduct } from 'app/view/product/product.model';
import { AccountService } from 'app/core/auth/account.service';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  account = inject(AccountService).trackCurrentAccount();
  private cartCookieName = 'cartItems';

  constructor(private cookieService: CookieService) {}

  getCartCookieName(): string {
    const userId = this.account()?.id;
    return userId ? `${this.cartCookieName}_${userId}` : this.cartCookieName;
  }

  getAnonymousCart(): CartItem[] {
    const anonymousCart = this.cookieService.get(this.cartCookieName);
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return anonymousCart ? JSON.parse(anonymousCart) : [];
  }

  getCart(): CartItem[] {
    const cookieName = this.getCartCookieName();
    const cart = this.cookieService.get(cookieName);
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
    const cookieName = this.getCartCookieName();
    const cartJson = JSON.stringify(cart);
    this.cookieService.set(cookieName, cartJson, 7, '/');
  }

  removeItem(productId: number): void {
    let cart = this.getCart();
    cart = cart.filter((item: CartItem) => item.productId !== productId);
    this.saveCart(cart);
  }

  clearCart(): void {
    const cookieName = this.getCartCookieName();

    this.cookieService.delete(cookieName);
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

  mergeCarts(anonymousCart: CartItem[], userCart: CartItem[]): CartItem[] {
    const mergedCart = [...userCart];
    anonymousCart.forEach(anonItem => {
      const existingItem = mergedCart.find(item => item.productId === anonItem.productId);
      if (existingItem) {
        existingItem.quantity += anonItem.quantity;
      } else {
        mergedCart.push(anonItem);
      }
    });
    return mergedCart;
  }

  loadCartForUser(): void {
    const userId = this.account()?.id;
    if (!userId) {
      return;
    }

    const userCartCookie = `${this.cartCookieName}_${userId}`;
    const userCart = this.cookieService.get(userCartCookie) ? JSON.parse(this.cookieService.get(userCartCookie)) : [];

    const anonymousCart = this.getAnonymousCart();
    if (anonymousCart.length > 0) {
      if (userCart.length > 0) {
        const mergedCart = this.mergeCarts(anonymousCart, userCart);
        this.saveCartForUser(mergedCart, userId);
      } else {
        this.saveCartForUser(anonymousCart, userId);
      }
      this.clearAnonymousCart();
    }
  }

  saveCartForUser(cart: CartItem[], userId: number): void {
    const userCartCookie = `${this.cartCookieName}_${userId}`;
    const cartJson = JSON.stringify(cart);
    this.cookieService.set(userCartCookie, cartJson, 7, '/');
  }

  clearAnonymousCart(): void {
    this.cookieService.delete(this.cartCookieName, '/');
  }
}
