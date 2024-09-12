import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IOrder {
  id: number;
  odr_adresse?: string | null;
  odr_phonenumber?: string | null;
  odr_price?: number | null;
  odr_date?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
