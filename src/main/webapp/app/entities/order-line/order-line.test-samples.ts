import { IOrderLine, NewOrderLine } from './order-line.model';

export const sampleWithRequiredData: IOrderLine = {
  id: 4698,
  odl_itemquantity: 21728,
  odl_soldprice: 22264.41,
};

export const sampleWithPartialData: IOrderLine = {
  id: 31097,
  odl_itemquantity: 26210,
  odl_soldprice: 2387.22,
};

export const sampleWithFullData: IOrderLine = {
  id: 8908,
  odl_itemquantity: 11081,
  odl_soldprice: 7436.87,
};

export const sampleWithNewData: NewOrderLine = {
  odl_itemquantity: 15057,
  odl_soldprice: 28897.24,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
