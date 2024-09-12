import { Routes } from '@angular/router';

import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';
import { CartComponent } from './views/cart/cart.component';
import { PageDetailComponent } from './views/page-detail/page-detail.component';

const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./home/home.component'),
    title: 'home.title',
  },
  {
    path: '',
    loadComponent: () => import('./layouts/navbar/navbar.component'),
    outlet: 'navbar',
  },
  {
    path: '',
    loadComponent: () => import('./layouts/header/header.component').then(m => m.HeaderComponent),
    outlet: 'header',
  },
  {
    path: 'admin',
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./admin/admin.routes'),
  },
  {
    path: 'account',
    loadChildren: () => import('./account/account.route'),
  },
  {
    path: 'login',
    loadComponent: () => import('./login/login.component'),
    title: 'login.title',
  },
  {
    path: '',
    loadChildren: () => import(`./entities/entity.routes`),
  },
  {
    path: 'view',
    loadChildren: () => import(`./view/product/product.routes`),
  },
  {
    path: 'cart',
    component: CartComponent,
  },
  {
    path: 'product/:id/:pro_name',
    loadComponent: () => import('./views/page-detail/page-detail.component').then(m => m.PageDetailComponent),
  },

  {
    path: 'product/:id/view',
    loadComponent: () => import('./views/page-detail/page-detail.component').then(m => m.PageDetailComponent),
  },

  ...errorRoute,
];

export default routes;
