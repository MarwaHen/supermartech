export interface CartItem {
  productId: number;
  productName: string;
  price: number;
  quantity: number;
  promotion?: number;
}

export const mockCartItems: CartItem[] = [
  {
    productId: 1,
    productName: 'Produto A',
    price: 100.0,
    quantity: 2,
  },
  {
    productId: 2,
    productName: 'Produto B',
    price: 50.0,
    quantity: 1,
  },
  {
    productId: 3,
    productName: 'Produto C',
    price: 25.0,
    quantity: 3,
  },
];
