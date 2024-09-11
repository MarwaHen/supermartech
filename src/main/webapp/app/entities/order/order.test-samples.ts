import dayjs from 'dayjs/esm';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 28318,
  odr_adresse: 'match rigidly',
  odr_phonenumber: 'boohoo knavishly',
  odr_price: 17213.16,
  odr_date: dayjs('2024-09-10T13:29'),
};

export const sampleWithPartialData: IOrder = {
  id: 904,
  odr_adresse: 'gosh impostor ick',
  odr_phonenumber: 'warm dill gosh',
  odr_price: 12395.95,
  odr_date: dayjs('2024-09-11T05:05'),
};

export const sampleWithFullData: IOrder = {
  id: 17417,
  odr_adresse: 'badly tango',
  odr_phonenumber: 'right',
  odr_price: 24984.72,
  odr_date: dayjs('2024-09-11T10:49'),
};

export const sampleWithNewData: NewOrder = {
  odr_adresse: 'excluding',
  odr_phonenumber: 'vaguely eek inside',
  odr_price: 631.43,
  odr_date: dayjs('2024-09-11T08:36'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
