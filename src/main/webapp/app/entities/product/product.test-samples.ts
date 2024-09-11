import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 28993,
  pro_name: 'iron',
  pro_price: 15410.4,
  pro_quantity: 23091,
  catt_id: 20255,
};

export const sampleWithPartialData: IProduct = {
  id: 14119,
  pro_name: 'although who thrive',
  pro_price: 23022.86,
  pro_quantity: 28775,
  catt_id: 8283,
  pro_date: dayjs('2024-09-07T17:22'),
  pro_promotion: 11813,
};

export const sampleWithFullData: IProduct = {
  id: 26134,
  pro_name: 'jaguar',
  pro_description: 'mouse ack',
  pro_price: 8757.02,
  pro_quantity: 17366,
  catt_id: 15981,
  pro_date: dayjs('2024-09-07T13:52'),
  pro_promotion: 4983,
  pro_mark: 'forfend why tune',
};

export const sampleWithNewData: NewProduct = {
  pro_name: 'for',
  pro_price: 20472.47,
  pro_quantity: 17422,
  catt_id: 19775,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
