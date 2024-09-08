import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../image-pro.test-samples';

import { ImageProFormService } from './image-pro-form.service';

describe('ImagePro Form Service', () => {
  let service: ImageProFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImageProFormService);
  });

  describe('Service methods', () => {
    describe('createImageProFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImageProFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            imgP_Path: expect.any(Object),
            pro_id: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });

      it('passing IImagePro should create a new form with FormGroup', () => {
        const formGroup = service.createImageProFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            imgP_Path: expect.any(Object),
            pro_id: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });
    });

    describe('getImagePro', () => {
      it('should return NewImagePro for default ImagePro initial value', () => {
        const formGroup = service.createImageProFormGroup(sampleWithNewData);

        const imagePro = service.getImagePro(formGroup) as any;

        expect(imagePro).toMatchObject(sampleWithNewData);
      });

      it('should return NewImagePro for empty ImagePro initial value', () => {
        const formGroup = service.createImageProFormGroup();

        const imagePro = service.getImagePro(formGroup) as any;

        expect(imagePro).toMatchObject({});
      });

      it('should return IImagePro', () => {
        const formGroup = service.createImageProFormGroup(sampleWithRequiredData);

        const imagePro = service.getImagePro(formGroup) as any;

        expect(imagePro).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImagePro should not enable id FormControl', () => {
        const formGroup = service.createImageProFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImagePro should disable id FormControl', () => {
        const formGroup = service.createImageProFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
