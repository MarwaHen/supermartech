import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 14338,
  pro_name: 'while',
  pro_price: 12325.13,
  pro_quantity: 22823,
  catt_id: 1089,
};

export const sampleWithPartialData: IProduct = {
  id: 7328,
  pro_name: 'jealously whirring',
  pro_price: 7116.85,
  pro_quantity: 13935,
  catt_id: 10411,
  pro_promotion: 3632,
};

export const sampleWithFullData: IProduct = {
  id: 2797,
  pro_name: 'longingly um',
  pro_description: 'yippee',
  pro_price: 28222.26,
  pro_quantity: 12659,
  catt_id: 4173,
  pro_date: dayjs('2024-09-07T10:44'),
  pro_promotion: 29200,
  pro_mark: 'practice bah suburban',
};

export const sampleWithNewData: NewProduct = {
  pro_name: 'clank',
  pro_price: 27405.63,
  pro_quantity: 18524,
  catt_id: 21829,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
