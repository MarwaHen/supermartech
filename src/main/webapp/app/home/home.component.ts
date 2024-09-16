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
  distinctProMarks: string[] = [];
  selectedMark = '';

  public router = inject(Router);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  private cartService = inject(CartService);

  trackId = (_index: number, item: IProduct): number => this.productService.getProductIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.sortState(), filterOptions));
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
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
  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event, this.filters.filterOptions);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState(), this.filters.filterOptions);
  }

  addToCart(product: IProduct): void {
    const finalPrice: number = product.pro_promotion
      ? parseFloat((product.pro_price! - product.pro_price! * (product.pro_promotion / 100)).toFixed(2))
      : parseFloat(product.pro_price!.toFixed(2));
    // Créer un objet CartItem avec les informations du produit et la quantité sélectionnée
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
    this.distinctProMarks = Array.from(new Set(this.products?.map(product => product.pro_mark))).filter(
      (mark): mark is string => mark != null,
    );
  }

  filterProductsByMark(): void {
    if (this.selectedMark) {
      this.filteredProducts = this.products?.filter(product => product.pro_mark === this.selectedMark) ?? [];
    } else {
      // Se nenhuma marca for selecionada, mostrar todos os produtos
      this.filteredProducts = this.products ?? [];
    }
  }
  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
    this.filters.initializeFromParams(params);
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.products = dataFromBody;
    this.getAllBrands();
    this.filterProductsByMark();
  }

  protected fillComponentAttributesFromResponseBody(data: IProduct[] | null): IProduct[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    const { page, filters } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    filters.filterOptions.forEach(filterOption => {
      queryObject[filterOption.name] = filterOption.values;
    });
    return this.productService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page: number, sortState: SortState, filterOptions?: IFilterOption[]): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    filterOptions?.forEach(filterOption => {
      queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;
    });

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }
}
