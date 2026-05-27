import axios from "axios";
import {
  CartRequestDTO,
  CartResponseDTO,
} from "../types/Cart";

const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080/api/carts";

/**
 * View cart for a user
 */
export const viewCart = (userId: number) =>
  axios.get<CartResponseDTO>(`${API_URL}/${userId}`);

/**
 * Add item(s) to cart
 */
export const addItemToCart = (userId: number, cartRequest: CartRequestDTO) =>
  axios.post<CartResponseDTO>(`${API_URL}/${userId}/items`, cartRequest);

/**
 * Remove a single item from cart
 */
export const removeItemFromCart = (userId: number, productName: string) =>
  axios.delete<CartResponseDTO>(`${API_URL}/${userId}/items/${productName}`);

/**
 * Clear the cart
 */
export const clearCart = (userId: number) =>
  axios.delete<void>(`${API_URL}/${userId}/clear`);