import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISubCategory } from '../sub-category.model';

@Component({
  standalone: true,
  selector: 'jhi-sub-category-detail',
  templateUrl: './sub-category-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SubCategoryDetailComponent {
  subCategory = input<ISubCategory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
