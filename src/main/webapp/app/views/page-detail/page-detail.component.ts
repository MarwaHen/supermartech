import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProduct } from 'app/entities/product/product.model';
import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';

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
