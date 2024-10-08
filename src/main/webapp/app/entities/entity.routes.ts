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
  {
    path: 'product',
    data: { pageTitle: 'supermatechApp.product.home.title' },
    loadChildren: () => import('./product/product.routes'),
  },
  {
    path: 'image-pro',
    data: { pageTitle: 'supermatechApp.imagePro.home.title' },
    loadChildren: () => import('./image-pro/image-pro.routes'),
  },
  {
    path: 'order',
    data: { pageTitle: 'supermatechApp.order.home.title' },
    loadChildren: () => import('./order/order.routes'),
  },
  {
    path: 'order-line',
    data: { pageTitle: 'supermatechApp.orderLine.home.title' },
    loadChildren: () => import('./order-line/order-line.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
