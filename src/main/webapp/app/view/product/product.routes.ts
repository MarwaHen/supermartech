import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProductResolve from './route/product-routing-resolve.service';

const productRoute: Routes = [
  {
    path: 'all',
    loadComponent: () => import('./list/product.component').then(m => m.ProductComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
  },
  {
    path: '1',
    loadComponent: () => import('./list_desktop/product.component').then(m => m.ProductComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
  },
  {
    path: '4',
    loadComponent: () => import('./list_laptop/product.component').then(m => m.ProductComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
  },
  {
    path: '8',
    loadComponent: () => import('./list_gpu/product.component').then(m => m.ProductComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
  },
  {
    path: '5',
    loadComponent: () => import('./list_headset/product.component').then(m => m.ProductComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
  },
  {
    path: '6',
    loadComponent: () => import('./list_keyboard/product.component').then(m => m.ProductComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
  },
  {
    path: '7',
    loadComponent: () => import('./list_ram/product.component').then(m => m.ProductComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
  },
];

export default productRoute;
