import { ISubCategory } from './sub-category.model';

export const sampleWithRequiredData: ISubCategory = {
  id: 21045,
  catt_name: 'worthwhile',
  cat_id: 26634,
};

export const sampleWithPartialData: ISubCategory = {
  id: 7813,
  catt_name: 'utter ack',
  catt_description: 'duh elver meh',
  cat_id: 30211,
};

export const sampleWithFullData: ISubCategory = {
  id: 7271,
  catt_name: 'sunset',
  catt_description: 'thoughtfully boo instead',
  cat_id: 14190,
  catt_icon: 'exactly semester delightfully',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
