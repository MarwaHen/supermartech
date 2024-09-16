import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartItem, mockCartItems } from 'app/models/cart-item.model';
import { CartService } from 'app/views/cart/cart.service';
import { Router } from '@angular/router';

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

  constructor(
    private cartService: CartService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadCart();
  }

  // Charger le panier et mettre à jour le prix total
  loadCart(): void {
    this.cartItems = this.cartService.getCart();
    this.totalPrice = this.updateTotalPrice();
  }

  // Ajouter des articles fictifs dans le panier pour le test
  addMockCartToCookies(): void {
    this.cartService.saveCart(mockCartItems);
    this.loadCart(); // Recharger le panier après ajout
  }

  // Mettre à jour le prix total
  updateTotalPrice(): number {
    return this.cartItems.reduce((acc, item) => acc + item.price * item.quantity, 0);
  }

  // Valider le panier
  validateCart(): void {
    alert('Panier validé');
  }

  // Vider le panier
  clearCart(): void {
    this.cartService.clearCart();
    this.loadCart(); // Recharger le panier après suppression
  }

  // Supprimer un article du panier
  removeItem(productId: number): void {
    this.cartService.removeItem(productId);
    this.loadCart(); // Recharger le panier après suppression
  }

  // Augmenter la quantité d'un produit
  increaseQuantity(item: CartItem): void {
    item.quantity++;
    this.cartService.updateItem(item);
    this.loadCart(); // Recharger le panier après modification
  }

  // Diminuer la quantité d'un produit
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
