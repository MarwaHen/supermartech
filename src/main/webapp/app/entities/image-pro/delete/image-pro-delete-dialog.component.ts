import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IImagePro } from '../image-pro.model';
import { ImageProService } from '../service/image-pro.service';

@Component({
  standalone: true,
  templateUrl: './image-pro-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ImageProDeleteDialogComponent {
  imagePro?: IImagePro;

  protected imageProService = inject(ImageProService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imageProService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
