import { ISubCategory } from './sub-category.model';

export const sampleWithRequiredData: ISubCategory = {
  id: 2135,
  catt_name: 'when',
  cat_id: 6099,
};

export const sampleWithPartialData: ISubCategory = {
  id: 4745,
  catt_name: 'kale hefty quickly',
  catt_description: 'onto bah boo',
  cat_id: 22780,
  catt_icon: 'flush stiffen',
};

export const sampleWithFullData: ISubCategory = {
  id: 7364,
  catt_name: 'forge',
  catt_description: 'drat',
  cat_id: 1306,
  catt_icon: 'evoke gracefully',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
