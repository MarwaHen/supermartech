import { IProduct } from 'app/entities/product/product.model';
import { IOrder } from 'app/entities/order/order.model';

export interface IOrderLine {
  id: number;
  odl_itemquantity?: number | null;
  odl_soldprice?: number | null;
  product?: IProduct | null;
  order?: IOrder | null;
}

export type NewOrderLine = Omit<IOrderLine, 'id'> & { id: null };
