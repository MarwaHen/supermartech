import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [SharedModule, RouterModule],
})
export default class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null; // Remplace signal par une propriété classique
  products: IProduct[] = []; // Liste des produits

  private readonly destroy$ = new Subject<void>();
  private accountService = inject(AccountService);
  private productService = inject(ProductService); // Injecte le ProductService
  private router = inject(Router);

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.account = account; // Mise à jour de l'account sans signal
      });

    this.loadProducts(); // Charger la liste des produits à l'initialisation
  }

  loadProducts(): void {
    this.productService.query().subscribe(response => {
      this.products = response.body ?? []; // Utilise l'opérateur de coalescence nulle ici
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  login(): void {
    this.router.navigate(['/login']);
  }
}
