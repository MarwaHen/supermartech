import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProduct, NewProduct } from '../product.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduct for edit and NewProductFormGroupInput for create.
 */
type ProductFormGroupInput = IProduct | PartialWithRequiredKeyOf<NewProduct>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProduct | NewProduct> = Omit<T, 'pro_date'> & {
  pro_date?: string | null;
};

type ProductFormRawValue = FormValueOf<IProduct>;

type NewProductFormRawValue = FormValueOf<NewProduct>;

type ProductFormDefaults = Pick<NewProduct, 'id' | 'pro_date'>;

type ProductFormGroupContent = {
  id: FormControl<ProductFormRawValue['id'] | NewProduct['id']>;
  pro_name: FormControl<ProductFormRawValue['pro_name']>;
  pro_description: FormControl<ProductFormRawValue['pro_description']>;
  pro_price: FormControl<ProductFormRawValue['pro_price']>;
  pro_quantity: FormControl<ProductFormRawValue['pro_quantity']>;
  catt_id: FormControl<ProductFormRawValue['catt_id']>;
  pro_date: FormControl<ProductFormRawValue['pro_date']>;
  pro_promotion: FormControl<ProductFormRawValue['pro_promotion']>;
  pro_mark: FormControl<ProductFormRawValue['pro_mark']>;
  subCategory: FormControl<ProductFormRawValue['subCategory']>;
};

export type ProductFormGroup = FormGroup<ProductFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductFormService {
  createProductFormGroup(product: ProductFormGroupInput = { id: null }): ProductFormGroup {
    const productRawValue = this.convertProductToProductRawValue({
      ...this.getFormDefaults(),
      ...product,
    });
    return new FormGroup<ProductFormGroupContent>({
      id: new FormControl(
        { value: productRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      pro_name: new FormControl(productRawValue.pro_name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      pro_description: new FormControl(productRawValue.pro_description, {
        validators: [Validators.maxLength(255)],
      }),
      pro_price: new FormControl(productRawValue.pro_price, {
        validators: [Validators.required],
      }),
      pro_quantity: new FormControl(productRawValue.pro_quantity, {
        validators: [Validators.required],
      }),
      catt_id: new FormControl(productRawValue.catt_id, {
        validators: [Validators.required],
      }),
      pro_date: new FormControl(productRawValue.pro_date),
      pro_promotion: new FormControl(productRawValue.pro_promotion),
      pro_mark: new FormControl(productRawValue.pro_mark, {
        validators: [Validators.maxLength(255)],
      }),
      subCategory: new FormControl(productRawValue.subCategory, {
        validators: [Validators.required],
      }),
    });
  }

  getProduct(form: ProductFormGroup): IProduct | NewProduct {
    return this.convertProductRawValueToProduct(form.getRawValue() as ProductFormRawValue | NewProductFormRawValue);
  }

  resetForm(form: ProductFormGroup, product: ProductFormGroupInput): void {
    const productRawValue = this.convertProductToProductRawValue({ ...this.getFormDefaults(), ...product });
    form.reset(
      {
        ...productRawValue,
        id: { value: productRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      pro_date: currentTime,
    };
  }

  private convertProductRawValueToProduct(rawProduct: ProductFormRawValue | NewProductFormRawValue): IProduct | NewProduct {
    return {
      ...rawProduct,
      pro_date: dayjs(rawProduct.pro_date, DATE_TIME_FORMAT),
    };
  }

  private convertProductToProductRawValue(
    product: IProduct | (Partial<NewProduct> & ProductFormDefaults),
  ): ProductFormRawValue | PartialWithRequiredKeyOf<NewProductFormRawValue> {
    return {
      ...product,
      pro_date: product.pro_date ? product.pro_date.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
