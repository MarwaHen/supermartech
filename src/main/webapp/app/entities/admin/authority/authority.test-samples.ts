import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'cce28ab1-7e7f-4cc5-a820-3e8a45a8db7f',
};

export const sampleWithPartialData: IAuthority = {
  name: '0d96d066-1083-4918-a22c-a39aa8429f94',
};

export const sampleWithFullData: IAuthority = {
  name: 'af65d5d7-1c34-4967-82d5-53a3ea8d0be6',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
