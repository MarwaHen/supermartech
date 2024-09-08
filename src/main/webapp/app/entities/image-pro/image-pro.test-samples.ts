import { IImagePro, NewImagePro } from './image-pro.model';

export const sampleWithRequiredData: IImagePro = {
  id: 22696,
  imgP_Path: 'hence serious',
  pro_id: 18754,
};

export const sampleWithPartialData: IImagePro = {
  id: 478,
  imgP_Path: 'wildlife',
  pro_id: 13331,
};

export const sampleWithFullData: IImagePro = {
  id: 337,
  imgP_Path: 'barbecue search up',
  pro_id: 19699,
};

export const sampleWithNewData: NewImagePro = {
  imgP_Path: 'teapot whether',
  pro_id: 1474,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
