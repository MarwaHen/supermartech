import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ImageProService } from '../service/image-pro.service';
import { IImagePro } from '../image-pro.model';
import { ImageProFormService } from './image-pro-form.service';

import { ImageProUpdateComponent } from './image-pro-update.component';

describe('ImagePro Management Update Component', () => {
  let comp: ImageProUpdateComponent;
  let fixture: ComponentFixture<ImageProUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let imageProFormService: ImageProFormService;
  let imageProService: ImageProService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ImageProUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ImageProUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImageProUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    imageProFormService = TestBed.inject(ImageProFormService);
    imageProService = TestBed.inject(ImageProService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const imagePro: IImagePro = { id: 456 };
      const product: IProduct = { id: 19081 };
      imagePro.product = product;

      const productCollection: IProduct[] = [{ id: 21212 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ imagePro });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const imagePro: IImagePro = { id: 456 };
      const product: IProduct = { id: 13033 };
      imagePro.product = product;

      activatedRoute.data = of({ imagePro });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.imagePro).toEqual(imagePro);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImagePro>>();
      const imagePro = { id: 123 };
      jest.spyOn(imageProFormService, 'getImagePro').mockReturnValue(imagePro);
      jest.spyOn(imageProService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imagePro });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imagePro }));
      saveSubject.complete();

      // THEN
      expect(imageProFormService.getImagePro).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(imageProService.update).toHaveBeenCalledWith(expect.objectContaining(imagePro));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImagePro>>();
      const imagePro = { id: 123 };
      jest.spyOn(imageProFormService, 'getImagePro').mockReturnValue({ id: null });
      jest.spyOn(imageProService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imagePro: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imagePro }));
      saveSubject.complete();

      // THEN
      expect(imageProFormService.getImagePro).toHaveBeenCalled();
      expect(imageProService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImagePro>>();
      const imagePro = { id: 123 };
      jest.spyOn(imageProService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imagePro });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(imageProService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
