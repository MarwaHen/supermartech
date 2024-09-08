import { ICategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 11804,
  cat_name: 'oof',
};

export const sampleWithPartialData: ICategory = {
  id: 396,
  cat_name: 'scented',
};

export const sampleWithFullData: ICategory = {
  id: 2830,
  cat_name: 'seal',
  cat_description: 'minus upbeat noun',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
