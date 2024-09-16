import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map, mergeMap } from 'rxjs/operators';

import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { Login } from './login.model';
import { CartService } from 'app/services/cart.service';

@Injectable({ providedIn: 'root' })
export class LoginService {
  private accountService = inject(AccountService);
  private authServerProvider = inject(AuthServerProvider);
  private cartService = inject(CartService);

  login(credentials: Login): Observable<Account | null> {
    return this.authServerProvider.login(credentials).pipe(
      mergeMap(() =>
        this.accountService.identity(true).pipe(
          map(account => {
            if (account?.id) {
              this.cartService.loadCartForUser();
            }
            return account;
          }),
        ),
      ),
    );
  }

  logout(): void {
    this.authServerProvider.logout().subscribe({ complete: () => this.accountService.authenticate(null) });
  }
}
