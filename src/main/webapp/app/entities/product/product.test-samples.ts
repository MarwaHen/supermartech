import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 10520,
  pro_name: 'yet',
  pro_price: 13333.57,
  pro_quantity: 20476,
  catt_id: 20487,
};

export const sampleWithPartialData: IProduct = {
  id: 23890,
  pro_name: 'queasy numb',
  pro_description: 'professionalize tremendously',
  pro_price: 16671.11,
  pro_quantity: 16387,
  catt_id: 32358,
  pro_date: dayjs('2024-09-08T04:28'),
};

export const sampleWithFullData: IProduct = {
  id: 29997,
  pro_name: 'supposing',
  pro_description: 'within whose hmph',
  pro_price: 10028.07,
  pro_quantity: 1924,
  catt_id: 12687,
  pro_date: dayjs('2024-09-07T15:58'),
  pro_promotion: 10158,
  pro_mark: 'whoa briefly',
};

export const sampleWithNewData: NewProduct = {
  pro_name: 'up',
  pro_price: 12691.09,
  pro_quantity: 22427,
  catt_id: 31988,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
