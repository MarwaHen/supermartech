import { Component, NgZone, OnInit, inject } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { FilterComponent, FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { EntityArrayResponseType } from 'app/entities/category/service/category.service';
import { CartItem } from 'app/models/cart-item.model';
import { CartService } from 'app/services/cart.service';
import { Filter, FilterService } from 'app/services/filter.service';
import { BrandService } from 'app/services/brand.service';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: [
    './home.component.scss',
    '../../content/hope/css/animate.css',
    '../../content/hope/css/bootstrap.min.css',
    '../../content/hope/css/font-awesome.min.css',
    '../../content/hope/css/jquery-ui.css',
    '../../content/hope/css/main.css',
    '../../content/hope/css/meanmenu.min.css',
    '../../content/hope/css/nivo-slider.css',
    '../../content/hope/css/normalize.css',
    '../../content/hope/css/owl.carousel.css',
    '../../content/hope/css/owl.my_theme.css',
    '../../content/hope/css/owl.theme.css',
    '../../content/hope/css/owl.transitions.css',
    '../../content/hope/css/responsive.css',
    '../../content/hope/fancy-box/jquery.fancybox.css',
    '../../content/hope/style.css',
  ],
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    FilterComponent,
    ItemCountComponent,
  ],
})
export default class HomeComponent implements OnInit {
  subscription: Subscription | null = null;
  products?: IProduct[];
  filteredProducts: IProduct[] = [];
  isLoading = false;

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;
  allBrands: string[] = [];
  selectedBrands: string[] = [];
  currentFilter!: Filter;
  minPrice = 0;
  maxPrice = -1;
  promoFilter = false;
  imagesByProduct: Record<number, string> = {};

  public router = inject(Router);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected filterService = inject(FilterService);
  protected brandService = inject(BrandService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  private cartService = inject(CartService);

  trackId = (_index: number, item: IProduct): number => this.productService.getProductIdentifier(item);

  ngOnInit(): void {
    this.currentFilter = this.filterService.getFilter();
    this.applyFilters();
    this.getAllBrands();
    this.filterService.filter$.subscribe(() => {
      this.applyFilters();
    });
  }

  // Helper function to get full stars (rounded down)
  getFullStars(pro_mark: string | number | null | undefined): number {
    return Math.floor(Number(pro_mark) || 0);
  }

  // Helper function to check for fractional part
  hasHalfStar(pro_mark: string | number | null | undefined): boolean {
    const mark = Number(pro_mark) || 0;
    return mark % 1 > 0;
  }

  addToCart(product: IProduct): void {
    const finalPrice: number = product.pro_promotion
      ? parseFloat((product.pro_price! - product.pro_price! * (product.pro_promotion / 100)).toFixed(2))
      : parseFloat(product.pro_price!.toFixed(2));
    const cartItem: CartItem = {
      productId: product.id,
      productName: product.pro_name!,
      price: finalPrice,
      quantity: 1,
    };

    this.cartService.addItem(cartItem);
    alert(`${cartItem.productName} a été ajouté au panier`);
  }

  getAllBrands(): void {
    this.brandService.getAllBrands().subscribe(reponse => {
      this.allBrands = reponse?.brand ?? [];
    });
  }

  updateBrandFilter(event: Event, brand: string): void {
    const checkbox = event.target as HTMLInputElement;

    if (checkbox.checked) {
      this.selectedBrands.push(brand);
    } else {
      this.selectedBrands = this.selectedBrands.filter(selectedBrand => selectedBrand !== brand);
    }
  }

  updateBrandFilterByChanges(brands?: string[]): void {
    this.selectedBrands = this.selectedBrands.filter(selectedBrand => brands?.includes(selectedBrand));
  }

  updateMinPrice(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.value) {
      this.minPrice = 0;
    } else {
      this.minPrice = Number(input.value);
    }
  }

  updateMaxPrice(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.value) {
      this.maxPrice = -1;
    } else {
      this.maxPrice = Number(input.value);
    }
  }

  applyFilters(): void {
    this.filterService.filterProducts().subscribe(products => {
      this.filteredProducts = products?.res_list;
      this.totalItems = products?.total;
      this.loadAllProductImages();
    });
    this.currentFilter = this.filterService.getFilter();
    this.updateBrandFilterByChanges(this.currentFilter.brand);
    this.promoFilter = this.currentFilter.promo ?? this.promoFilter;
  }

  onSearchFiltersClicked(): void {
    this.filterService.updateFilter({
      min_price: this.minPrice,
      max_price: this.maxPrice,
      brand: this.selectedBrands,
      promo: this.promoFilter,
      page: 0,
    });
    this.page = 1;
    this.applyFilters();
  }

  updatePromoFilter(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    this.promoFilter = checkbox.checked;
  }

  clearFilters(): void {
    this.minPrice = 0;
    this.maxPrice = -1;
    this.selectedBrands = [];
    this.promoFilter = false;
    this.page = 1;
    (document.getElementById('min_price') as HTMLInputElement).value = '';
    (document.getElementById('max_price') as HTMLInputElement).value = '';

    this.filterService.resetFilter();

    this.applyFilters();
  }

  getImageByProductId(productId: number): string {
    return this.imagesByProduct[productId] || '';
  }

  loadAllProductImages(): void {
    if (this.filteredProducts.length > 0) {
      this.filteredProducts.forEach(product => {
        this.productService.loadImages(product.id).subscribe({
          next: response => {
            if (response.length > 0) {
              this.imagesByProduct[product.id] = response[0].image_path;
            }
          },
          error: () => console.error(`Erro ao carregar a imagem do produto ${product.id}`),
        });
      });
    }
  }

  navigateToPage(newPage: number): void {
    this.page = newPage;
    this.filterService.updateFilter({
      page: newPage - 1,
      size: this.itemsPerPage,
    });
    this.applyFilters();
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
    this.filters.initializeFromParams(params);
  }
}
