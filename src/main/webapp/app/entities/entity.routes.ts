import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'supermatechApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'category',
    data: { pageTitle: 'supermatechApp.category.home.title' },
    loadChildren: () => import('./category/category.routes'),
  },
  {
    path: 'sub-category',
    data: { pageTitle: 'supermatechApp.subCategory.home.title' },
    loadChildren: () => import('./sub-category/sub-category.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
