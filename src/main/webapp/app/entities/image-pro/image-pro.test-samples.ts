import { IImagePro, NewImagePro } from './image-pro.model';

export const sampleWithRequiredData: IImagePro = {
  id: 4881,
  imgP_Path: 'afterwards mmm',
  pro_id: 15657,
};

export const sampleWithPartialData: IImagePro = {
  id: 8460,
  imgP_Path: 'insignificant as',
  pro_id: 9497,
};

export const sampleWithFullData: IImagePro = {
  id: 3430,
  imgP_Path: 'incompetence',
  pro_id: 20922,
};

export const sampleWithNewData: NewImagePro = {
  imgP_Path: 'enfeeble along hospitable',
  pro_id: 6007,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
