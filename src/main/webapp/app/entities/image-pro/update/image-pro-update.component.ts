import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IImagePro } from '../image-pro.model';
import { ImageProService } from '../service/image-pro.service';
import { ImageProFormGroup, ImageProFormService } from './image-pro-form.service';

@Component({
  standalone: true,
  selector: 'jhi-image-pro-update',
  templateUrl: './image-pro-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ImageProUpdateComponent implements OnInit {
  isSaving = false;
  imagePro: IImagePro | null = null;

  productsSharedCollection: IProduct[] = [];

  protected imageProService = inject(ImageProService);
  protected imageProFormService = inject(ImageProFormService);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ImageProFormGroup = this.imageProFormService.createImageProFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imagePro }) => {
      this.imagePro = imagePro;
      if (imagePro) {
        this.updateForm(imagePro);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const imagePro = this.imageProFormService.getImagePro(this.editForm);
    if (imagePro.id !== null) {
      this.subscribeToSaveResponse(this.imageProService.update(imagePro));
    } else {
      this.subscribeToSaveResponse(this.imageProService.create(imagePro));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImagePro>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(imagePro: IImagePro): void {
    this.imagePro = imagePro;
    this.imageProFormService.resetForm(this.editForm, imagePro);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      imagePro.product,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.imagePro?.product)))
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
