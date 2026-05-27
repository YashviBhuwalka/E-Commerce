import axios from "axios";
import { ProductRequestDTO, ProductResponseDTO } from "../types/Product";

const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";

//  User APIs 
export const getAllProducts = () => axios.get<ProductResponseDTO[]>(`${API_URL}/api/products`);
export const searchProductsByName = (name: string) =>
  axios.get<ProductResponseDTO[]>(`${API_URL}/api/products/search/name?name=${name}`);
export const searchProductsByCategory = (category: string) =>
  axios.get<ProductResponseDTO[]>(`${API_URL}/api/products/search/category?category=${category}`);
export const searchProductsByPriceRange = (minPrice: number, maxPrice: number) =>
  axios.get<ProductResponseDTO[]>(`${API_URL}/api/products/search/price?minPrice=${minPrice}&maxPrice=${maxPrice}`);

// Admin APIs
export const getAllProductsAdmin = () => axios.get<ProductResponseDTO[]>(`${API_URL}/api/admin/products`);
export const getProductById = (id: number) =>
  axios.get<ProductResponseDTO>(`${API_URL}/api/admin/products/${id}`);
export const addProduct = (product: ProductRequestDTO) =>
  axios.post<ProductResponseDTO>(`${API_URL}/api/admin/products`, product);
export const updateProduct = (id: number, product: ProductRequestDTO) =>
  axios.put<ProductResponseDTO>(`${API_URL}/api/admin/products/${id}`, product);
export const deleteProduct = (id: number) =>
  axios.delete(`${API_URL}/api/admin/products/${id}`);