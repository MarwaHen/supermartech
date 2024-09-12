import { IImagePro, NewImagePro } from './image-pro.model';

export const sampleWithRequiredData: IImagePro = {
  id: 10602,
  imgP_Path: 'blah only',
  pro_id: 26906,
};

export const sampleWithPartialData: IImagePro = {
  id: 21525,
  imgP_Path: 'rate gigantic',
  pro_id: 3327,
};

export const sampleWithFullData: IImagePro = {
  id: 20326,
  imgP_Path: 'blah hm but',
  pro_id: 11027,
};

export const sampleWithNewData: NewImagePro = {
  imgP_Path: 'charbroil pale before',
  pro_id: 28167,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
