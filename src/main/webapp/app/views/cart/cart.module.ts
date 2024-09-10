import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartComponent } from './cart.component';
import { CartListComponent } from './components/cart-list/cart-list.component';
import { CartSummaryComponent } from './components/cart-summary/cart-summary.component';
import { CookieService } from 'ngx-cookie-service';

@NgModule({
  providers: [CookieService],
  declarations: [],
  imports: [CommonModule, CartComponent, CartListComponent, CartSummaryComponent],
  exports: [CartComponent, CartListComponent, CartSummaryComponent],
})
export class CartModule {}
