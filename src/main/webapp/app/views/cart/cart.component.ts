import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartItem, mockCartItems } from 'app/models/cart-item.model';
import { Router } from '@angular/router';
import { CartService } from 'app/services/cart.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-cart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart.component.html',
  styleUrls: [
    '../../../content/hope/css/animate.css',
    '../../../content/hope/css/bootstrap.min.css',
    '../../../content/hope/css/font-awesome.min.css',
    '../../../content/hope/css/jquery-ui.css',
    '../../../content/hope/css/main.css',
    '../../../content/hope/css/meanmenu.min.css',
    '../../../content/hope/css/nivo-slider.css',
    '../../../content/hope/css/normalize.css',
    '../../../content/hope/css/owl.carousel.css',
    '../../../content/hope/css/owl.my_theme.css',
    '../../../content/hope/css/owl.theme.css',
    '../../../content/hope/css/owl.transitions.css',
    '../../../content/hope/css/responsive.css',
    '../../../content/hope/fancy-box/jquery.fancybox.css',
    '../../../content/hope/style.css',
  ],
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = []; // Explicitement défini comme CartItem[]
  totalPrice = 0; // Suppression de l'annotation explicite ": number"
  account = inject(AccountService).trackCurrentAccount();

  constructor(
    private cartService: CartService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {
    const cart = this.cartService.getCart();
    if (cart.length > 0) {
      this.cartItems = cart;
    } else {
      this.cartItems = [];
    }
  }

  updateTotalPrice(): number {
    return this.cartService.getTotalPrice();
  }

  validateCart(): void {
    if (!this.account()) {
      this.router.navigate(['/login']);
    } else {
      this.router.navigate(['/payment']);
    }
  }

  async clearCart(): Promise<void> {
    await this.cartService.clearCart();
    this.loadCart();
    this.updateTotalPrice();
  }
  removeItem(productId: number): void {
    this.cartService.removeItem(productId);
    this.loadCart();
  }

  increaseQuantity(item: CartItem): void {
    item.quantity++;
    this.cartService.updateItem(item);
    this.loadCart();
  }

  decreaseQuantity(item: CartItem): void {
    if (item.quantity > 1) {
      item.quantity--;
      this.cartService.updateItem(item);
    } else {
      this.removeItem(item.productId); // Supprime l'article si la quantité est 1
    }
    this.loadCart(); // Recharger le panier après modification
  }

  // Continuer le shopping, rediriger vers la page d'accueil
  continueShopping(): void {
    this.router.navigate(['/']); // Redirige vers la route de la page d'accueil
  }
}
