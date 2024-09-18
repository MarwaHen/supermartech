import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IImagePro, NewImagePro } from '../image-pro.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImagePro for edit and NewImageProFormGroupInput for create.
 */
type ImageProFormGroupInput = IImagePro | PartialWithRequiredKeyOf<NewImagePro>;

type ImageProFormDefaults = Pick<NewImagePro, 'id'>;

type ImageProFormGroupContent = {
  id: FormControl<IImagePro['id'] | NewImagePro['id']>;
  imgP_Path: FormControl<IImagePro['imgP_Path']>;
  pro_id: FormControl<IImagePro['pro_id']>;
  product: FormControl<IImagePro['product']>;
};

export type ImageProFormGroup = FormGroup<ImageProFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImageProFormService {
  createImageProFormGroup(imagePro: ImageProFormGroupInput = { id: null }): ImageProFormGroup {
    const imageProRawValue = {
      ...this.getFormDefaults(),
      ...imagePro,
    };
    return new FormGroup<ImageProFormGroupContent>({
      id: new FormControl(
        { value: imageProRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      imgP_Path: new FormControl(imageProRawValue.imgP_Path, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      pro_id: new FormControl(imageProRawValue.pro_id, {
        validators: [Validators.required],
      }),
      product: new FormControl(imageProRawValue.product, {
        validators: [Validators.required],
      }),
    });
  }

  getImagePro(form: ImageProFormGroup): IImagePro | NewImagePro {
    return form.getRawValue() as IImagePro | NewImagePro;
  }

  resetForm(form: ImageProFormGroup, imagePro: ImageProFormGroupInput): void {
    const imageProRawValue = { ...this.getFormDefaults(), ...imagePro };
    form.reset(
      {
        ...imageProRawValue,
        id: { value: imageProRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ImageProFormDefaults {
    return {
      id: null,
    };
  }
}
