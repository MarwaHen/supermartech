import { ICategory } from 'app/entities/category/category.model';

export interface ISubCategory {
  id: number;
  catt_name?: string | null;
  catt_description?: string | null;
  cat_id?: number | null;
  catt_icon?: string | null;
  category?: ICategory | null;
}
