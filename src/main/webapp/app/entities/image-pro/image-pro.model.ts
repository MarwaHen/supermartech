import { IProduct } from 'app/entities/product/product.model';

export interface IImagePro {
  id: number;
  imgP_Path?: string | null;
  pro_id?: number | null;
  product?: IProduct | null;
}

export type NewImagePro = Omit<IImagePro, 'id'> & { id: null };
