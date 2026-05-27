export interface CartItemRequestDTO {
  productName: string;
  quantity: number;
}

export interface CartItemResponseDTO {
  productName: string;
  quantity: number;
  totalPrice: number;
}

export interface CartRequestDTO {
  items: CartItemRequestDTO[];
}

export interface CartResponseDTO {
  userId: number;
  items: CartItemResponseDTO[];
  totalPrice: number;
}