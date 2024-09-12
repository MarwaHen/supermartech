import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderLine } from '../order-line.model';
import { OrderLineService } from '../service/order-line.service';
import { OrderLineFormService } from './order-line-form.service';

import { OrderLineUpdateComponent } from './order-line-update.component';

describe('OrderLine Management Update Component', () => {
  let comp: OrderLineUpdateComponent;
  let fixture: ComponentFixture<OrderLineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderLineFormService: OrderLineFormService;
  let orderLineService: OrderLineService;
  let productService: ProductService;
  let orderService: OrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OrderLineUpdateComponent],
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
      .overrideTemplate(OrderLineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderLineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderLineFormService = TestBed.inject(OrderLineFormService);
    orderLineService = TestBed.inject(OrderLineService);
    productService = TestBed.inject(ProductService);
    orderService = TestBed.inject(OrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const orderLine: IOrderLine = { id: 456 };
      const product: IProduct = { id: 7459 };
      orderLine.product = product;

      const productCollection: IProduct[] = [{ id: 17768 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderLine });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Order query and add missing value', () => {
      const orderLine: IOrderLine = { id: 456 };
      const order: IOrder = { id: 14965 };
      orderLine.order = order;

      const orderCollection: IOrder[] = [{ id: 11370 }];
      jest.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      jest.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderLine });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(expect.objectContaining),
      );
      expect(comp.ordersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orderLine: IOrderLine = { id: 456 };
      const product: IProduct = { id: 4214 };
      orderLine.product = product;
      const order: IOrder = { id: 18882 };
      orderLine.order = order;

      activatedRoute.data = of({ orderLine });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.ordersSharedCollection).toContain(order);
      expect(comp.orderLine).toEqual(orderLine);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderLine>>();
      const orderLine = { id: 123 };
      jest.spyOn(orderLineFormService, 'getOrderLine').mockReturnValue(orderLine);
      jest.spyOn(orderLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderLine }));
      saveSubject.complete();

      // THEN
      expect(orderLineFormService.getOrderLine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderLineService.update).toHaveBeenCalledWith(expect.objectContaining(orderLine));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderLine>>();
      const orderLine = { id: 123 };
      jest.spyOn(orderLineFormService, 'getOrderLine').mockReturnValue({ id: null });
      jest.spyOn(orderLineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderLine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderLine }));
      saveSubject.complete();

      // THEN
      expect(orderLineFormService.getOrderLine).toHaveBeenCalled();
      expect(orderLineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderLine>>();
      const orderLine = { id: 123 };
      jest.spyOn(orderLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderLineService.update).toHaveBeenCalled();
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

    describe('compareOrder', () => {
      it('Should forward to orderService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(orderService, 'compareOrder');
        comp.compareOrder(entity, entity2);
        expect(orderService.compareOrder).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
