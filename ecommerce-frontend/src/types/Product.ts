export interface ProductRequestDTO {
  productName: string;
  productCategory: string;
  productPrice: number;
}

export interface ProductResponseDTO {
  productId: number;
  productName: string;
  productCategory: string;
  productPrice: number;
  deleted:boolean;
}