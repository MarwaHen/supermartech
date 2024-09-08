import { ICategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 10437,
  cat_name: 'modernize',
};

export const sampleWithPartialData: ICategory = {
  id: 8245,
  cat_name: 'moralize',
};

export const sampleWithFullData: ICategory = {
  id: 9804,
  cat_name: 'self-esteem whether view',
  cat_description: 'atop as nor',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
