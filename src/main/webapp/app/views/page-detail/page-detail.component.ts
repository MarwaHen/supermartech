import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProduct } from 'app/entities/product/product.model';
import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { CartItem } from 'app/models/cart-item.model';
import { CartService } from 'app/services/cart.service';

@Component({
  standalone: true,
  selector: 'jhi-page-detail',
  templateUrl: './page-detail.component.html',
  imports: [SharedModule, RouterModule, CommonModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PageDetailComponent implements OnInit {
  product: IProduct | null = null;

  private route = inject(ActivatedRoute);
  private productService = inject(ProductService);
  private cartService = inject(CartService);

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const id = params['id'];
      this.loadProduct(id);
    });
  }

  loadProduct(id: number): void {
    this.productService.find(id).subscribe(response => {
      this.product = response.body;
    });
  }

  addToCart(): void {
    if (this.product) {
      // Calcul du prix promotionnel si promotion existante
      const finalPrice = this.product.pro_promotion
        ? this.product.pro_price! - this.product.pro_price! * (this.product.pro_promotion / 100)
        : this.product.pro_price!;

      // Créer un objet CartItem avec les informations du produit
      const cartItem: CartItem = {
        productId: this.product.id,
        productName: this.product.pro_name!,
        price: finalPrice,
        quantity: 1,
      };

      this.cartService.addItem(cartItem);
      alert(`${cartItem.productName} a été ajouté au panier`);
    }
  }

  previousState(): void {
    window.history.back();
  }
}
