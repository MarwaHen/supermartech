import dayjs from 'dayjs/esm';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 138,
  odr_adresse: 'antagonize',
  odr_phonenumber: 'cup fiercely',
  odr_price: 20633.31,
  odr_date: dayjs('2024-09-11T11:43'),
};

export const sampleWithPartialData: IOrder = {
  id: 23088,
  odr_adresse: 'jangle hence blah',
  odr_phonenumber: 'patronise',
  odr_price: 21367.54,
  odr_date: dayjs('2024-09-11T03:41'),
};

export const sampleWithFullData: IOrder = {
  id: 30679,
  odr_adresse: 'meh massage',
  odr_phonenumber: 'aha however scaly',
  odr_price: 19004.95,
  odr_date: dayjs('2024-09-11T02:17'),
};

export const sampleWithNewData: NewOrder = {
  odr_adresse: 'ugh coalition',
  odr_phonenumber: 'tradition',
  odr_price: 27814.83,
  odr_date: dayjs('2024-09-11T05:08'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
