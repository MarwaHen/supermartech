import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 13244,
  login: 'EM',
};

export const sampleWithPartialData: IUser = {
  id: 1118,
  login: 'kz.d6',
};

export const sampleWithFullData: IUser = {
  id: 18229,
  login: 'dy@P\\pHvAZ\\KNS3c\\8Iow0\\s6za',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
