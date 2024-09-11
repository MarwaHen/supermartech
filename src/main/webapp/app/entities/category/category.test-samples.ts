import { ICategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 19119,
  cat_name: 'blah amongst',
};

export const sampleWithPartialData: ICategory = {
  id: 26010,
  cat_name: 'naturally often',
  cat_description: 'what',
};

export const sampleWithFullData: ICategory = {
  id: 31366,
  cat_name: 'till',
  cat_description: 'ha vulture blindly',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
