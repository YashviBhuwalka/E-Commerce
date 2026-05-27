import axios from "axios";
import { OrderRequestDTO, OrderResponseDTO } from "../types/Order";

const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080/api/orders";

/**
 * Place an order from cart
 */
export const placeOrder = (orderRequest: OrderRequestDTO) =>
  axios.post<OrderResponseDTO>(`${API_URL}/place`, orderRequest);

/**
 * Get user order history
 */
export const getUserOrders = (userId: number) =>
  axios.get<OrderResponseDTO[]>(`${API_URL}/user/${userId}`);

/**
 * Get all orders (admin)
 */
export const getAllOrders = () =>
  axios.get<OrderResponseDTO[]>(`${API_URL}`);