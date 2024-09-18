import { ISubCategory } from 'app/entities/sub-category/sub-category.model';

export interface ICategory {
  id: number;
  cat_name?: string | null;
  cat_description?: string | null;
  subCategories?: ISubCategory[];
}
