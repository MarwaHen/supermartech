import { ICategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 1466,
  cat_name: 'quarrelsomely attentive but',
};

export const sampleWithPartialData: ICategory = {
  id: 2231,
  cat_name: 'kitchen',
  cat_description: 'suspiciously if merry',
};

export const sampleWithFullData: ICategory = {
  id: 7165,
  cat_name: 'dearly carelessly',
  cat_description: 'handlebar times vivid',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
