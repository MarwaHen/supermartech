import { ICategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 21141,
  cat_name: 'till maternity scarcely',
};

export const sampleWithPartialData: ICategory = {
  id: 21341,
  cat_name: 'staid',
  cat_description: 'detailed as what',
};

export const sampleWithFullData: ICategory = {
  id: 2568,
  cat_name: 'mama forenenst',
  cat_description: 'that armor often',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
