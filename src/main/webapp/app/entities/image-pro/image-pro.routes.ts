import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ImageProResolve from './route/image-pro-routing-resolve.service';

const imageProRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/image-pro.component').then(m => m.ImageProComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/image-pro-detail.component').then(m => m.ImageProDetailComponent),
    resolve: {
      imagePro: ImageProResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/image-pro-update.component').then(m => m.ImageProUpdateComponent),
    resolve: {
      imagePro: ImageProResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/image-pro-update.component').then(m => m.ImageProUpdateComponent),
    resolve: {
      imagePro: ImageProResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default imageProRoute;
