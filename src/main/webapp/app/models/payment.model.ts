export interface Payment {
  id_client: number | undefined;
  cart_list: PaymentProduct[];
  address: string;
  phone_number: string;
}

export interface PaymentProduct {
  product_id: number;
  quantity: number;
}
