export interface OrderItemResponseDTO {
  productName: string;
  quantity: number;
  totalPrice: number;
}

export interface OrderResponseDTO {
  orderId: number;
  userId: number;
  items: OrderItemResponseDTO[];
  totalAmount: number;
  orderDate: string; 
}

export interface OrderRequestDTO {
  userId: number;
  shippingAddress: string;
}