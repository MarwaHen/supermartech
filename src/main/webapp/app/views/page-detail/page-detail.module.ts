import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PageDetailRoutingModule } from './page-detail-routing.module';
import { PageDetailComponent } from './page-detail.component'; // Import du composant standalone

@NgModule({
  imports: [
    CommonModule,
    PageDetailRoutingModule,
    PageDetailComponent, // On l'importe ici à la place de le déclarer
  ],
})
export class PageDetailModule {}
