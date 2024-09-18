import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISubCategory } from 'app/entities/sub-category/sub-category.model';
import { SubCategoryService } from 'app/entities/sub-category/service/sub-category.service';
import { IProduct } from '../product.model';
import { ProductService } from '../service/product.service';
import { ProductFormGroup, ProductFormService } from './product-form.service';

@Component({
  standalone: true,
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;
  product: IProduct | null = null;

  subCategoriesSharedCollection: ISubCategory[] = [];

  protected productService = inject(ProductService);
  protected productFormService = inject(ProductFormService);
  protected subCategoryService = inject(SubCategoryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFormGroup = this.productFormService.createProductFormGroup();

  compareSubCategory = (o1: ISubCategory | null, o2: ISubCategory | null): boolean => this.subCategoryService.compareSubCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      if (product) {
        this.updateForm(product);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.productFormService.getProduct(this.editForm);
    if (product.id !== null) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.product = product;
    this.productFormService.resetForm(this.editForm, product);

    this.subCategoriesSharedCollection = this.subCategoryService.addSubCategoryToCollectionIfMissing<ISubCategory>(
      this.subCategoriesSharedCollection,
      product.subCategory,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.subCategoryService
      .query()
      .pipe(map((res: HttpResponse<ISubCategory[]>) => res.body ?? []))
      .pipe(
        map((subCategories: ISubCategory[]) =>
          this.subCategoryService.addSubCategoryToCollectionIfMissing<ISubCategory>(subCategories, this.product?.subCategory),
        ),
      )
      .subscribe((subCategories: ISubCategory[]) => (this.subCategoriesSharedCollection = subCategories));
  }
}
