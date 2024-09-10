import dayjs from 'dayjs/esm';
import { ISubCategory } from 'app/entities/sub-category/sub-category.model';

export interface IProduct {
  id: number;
  pro_name?: string | null;
  pro_description?: string | null;
  pro_price?: number;
  pro_quantity?: number | null;
  catt_id?: number | null;
  pro_date?: dayjs.Dayjs | null;
  pro_promotion?: number;
  pro_mark?: string | null;
  subCategory?: ISubCategory | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
