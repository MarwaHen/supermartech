import { ISubCategory } from './sub-category.model';

export const sampleWithRequiredData: ISubCategory = {
  id: 4839,
  catt_name: 'but woot after',
  cat_id: 2821,
};

export const sampleWithPartialData: ISubCategory = {
  id: 4649,
  catt_name: 'remortgage anxiously',
  cat_id: 19917,
};

export const sampleWithFullData: ISubCategory = {
  id: 10179,
  catt_name: 'cleat hemorrhage overdue',
  catt_description: 'past expensive',
  cat_id: 11022,
  catt_icon: 'huzzah',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
