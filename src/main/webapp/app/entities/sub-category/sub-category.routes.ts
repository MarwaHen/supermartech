import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SubCategoryResolve from './route/sub-category-routing-resolve.service';

const subCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/sub-category.component').then(m => m.SubCategoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/sub-category-detail.component').then(m => m.SubCategoryDetailComponent),
    resolve: {
      subCategory: SubCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default subCategoryRoute;
