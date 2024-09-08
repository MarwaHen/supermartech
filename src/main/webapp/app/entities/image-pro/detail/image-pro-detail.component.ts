import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IImagePro } from '../image-pro.model';

@Component({
  standalone: true,
  selector: 'jhi-image-pro-detail',
  templateUrl: './image-pro-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ImageProDetailComponent {
  imagePro = input<IImagePro | null>(null);

  previousState(): void {
    window.history.back();
  }
}
