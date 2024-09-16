import { CommonModule } from '@angular/common';
import { Component, inject, Input } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-cart-summary',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart-summary.component.html',
  styleUrl: './cart-summary.component.scss',
})
export class CartSummaryComponent {
  @Input()
  totalPrice!: number;
  account = inject(AccountService).trackCurrentAccount();

  constructor(private router: Router) {}

  validateCart(): void {
    if (!this.account()) {
      this.router.navigate(['/login']);
    } else {
      this.router.navigate(['/payment']);
    }
  }
}
