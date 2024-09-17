import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProduct } from 'app/entities/product/product.model';
import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { CartItem } from 'app/models/cart-item.model';
import { FormsModule } from '@angular/forms'; // Import FormsModule for ngModel binding
import { CartService } from 'app/services/cart.service';

@Component({
  standalone: true,
  selector: 'jhi-page-detail',
  templateUrl: './page-detail.component.html',
  styleUrl: './page-detail.component.scss',
  imports: [SharedModule, RouterModule, CommonModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, FormsModule], // Add FormsModule to imports
})
export class PageDetailComponent implements OnInit {
  product: IProduct | null = null;
  quantity = 1;
  productImages: string[] = [];

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
      this.loadAllProductImages();
    });
  }

  addToCart(): void {
    if (this.product && this.quantity > 0 && (this.product.pro_quantity ?? 0) > 0) {
      // Calcul du prix promotionnel si promotion existante
      const finalPrice: number = this.product.pro_promotion
        ? parseFloat((this.product.pro_price! - this.product.pro_price! * (this.product.pro_promotion / 100)).toFixed(2))
        : parseFloat(this.product.pro_price!.toFixed(2));

      const cartItem: CartItem = {
        productId: this.product.id,
        productName: this.product.pro_name!,
        price: finalPrice,
        quantity: this.quantity,
      };

      this.cartService.addItem(cartItem);
      alert(`${cartItem.productName} (x${this.quantity}) a été ajouté au panier`);
    } else {
      alert('Quantité invalide ou produit en rupture de stock');
    }
  }

  loadAllProductImages(): void {
    if (this.product) {
      this.productService.loadImages(this.product.id).subscribe({
        next: response => {
          if (response.length > 0) {
            // eslint-disable-next-line @typescript-eslint/no-unsafe-return
            this.productImages = response.map((img: any) => img.image_path);
          }
        },
        error: () => console.error(`Erro ao carregar a imagem do produto ${this.product?.id}`),
      });
    }
  }
}
