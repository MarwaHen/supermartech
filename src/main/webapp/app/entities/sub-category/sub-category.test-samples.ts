import { ISubCategory } from './sub-category.model';

export const sampleWithRequiredData: ISubCategory = {
  id: 16579,
  catt_name: 'finally foolish earmuffs',
  cat_id: 23257,
};

export const sampleWithPartialData: ISubCategory = {
  id: 19155,
  catt_name: 'ew hence meanwhile',
  catt_description: 'zowie',
  cat_id: 8327,
  catt_icon: 'when unpleasant boo',
};

export const sampleWithFullData: ISubCategory = {
  id: 15973,
  catt_name: 'rewind gemsbok',
  catt_description: 'fatally',
  cat_id: 3999,
  catt_icon: 'even prosecute admit',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
