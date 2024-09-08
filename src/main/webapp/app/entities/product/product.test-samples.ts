import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 4928,
  pro_name: 'notable',
  pro_price: 3924.84,
  pro_quantity: 27506,
  catt_id: 3986,
};

export const sampleWithPartialData: IProduct = {
  id: 29295,
  pro_name: 'by until',
  pro_price: 16184.54,
  pro_quantity: 28175,
  catt_id: 7814,
  pro_promotion: 21071,
};

export const sampleWithFullData: IProduct = {
  id: 16403,
  pro_name: 'inasmuch entirety absent',
  pro_description: 'apropos',
  pro_price: 24630.1,
  pro_quantity: 25396,
  catt_id: 22779,
  pro_date: dayjs('2024-09-07T11:59'),
  pro_promotion: 17242,
  pro_mark: 'yum fluid',
};

export const sampleWithNewData: NewProduct = {
  pro_name: 'in',
  pro_price: 16808.07,
  pro_quantity: 13604,
  catt_id: 26926,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
