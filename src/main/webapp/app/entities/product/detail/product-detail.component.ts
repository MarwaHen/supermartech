import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router'; // Correction de l'espace en trop
import { CommonModule } from '@angular/common'; // Correction de l'espace en trop
import { ProductService } from '../service/product.service';
import { IProduct } from '../product.model';
import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';

@Component({
  standalone: true,
  selector: 'jhi-product-detail',
  templateUrl: './product-detail.component.html',
  imports: [
    SharedModule, // Ajout d'une virgule pour respecter les règles de Prettier
    RouterModule, // Ajout d'une virgule pour respecter les règles de Prettier
    CommonModule, // Ajout d'une virgule pour respecter les règles de Prettier
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
  ],
})
export class ProductDetailComponent implements OnInit {
  product: IProduct | null = null;

  private route = inject(ActivatedRoute);
  private productService = inject(ProductService);

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

  previousState(): void {
    window.history.back();
  }
}
