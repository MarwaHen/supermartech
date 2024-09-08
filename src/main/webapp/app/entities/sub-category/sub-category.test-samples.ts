import { ISubCategory } from './sub-category.model';

export const sampleWithRequiredData: ISubCategory = {
  id: 19401,
  catt_name: 'arrogantly infatuated majestically',
  cat_id: 23747,
};

export const sampleWithPartialData: ISubCategory = {
  id: 22790,
  catt_name: 'flame er',
  catt_description: 'pessimistic',
  cat_id: 16983,
  catt_icon: 'psst vice',
};

export const sampleWithFullData: ISubCategory = {
  id: 19205,
  catt_name: 'fooey frolic',
  catt_description: 'circa spur yet',
  cat_id: 19582,
  catt_icon: 'pantology hospitable',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
