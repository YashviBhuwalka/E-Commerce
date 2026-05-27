import axios from "axios";
import { UserResponseDTO, UserRequestDTO } from "../types/User";

const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";

export const getAllUsers = () => axios.get<UserResponseDTO[]>(`${API_URL}/admin/users`);
export const getUserById = (id: number) => axios.get<UserResponseDTO>(`${API_URL}/admin/users/${id}`);
export const updateUserByAdmin = (id: number, data: UserRequestDTO) =>
  axios.put<UserResponseDTO>(`${API_URL}/admin/users/${id}`, data);
export const deleteUserByAdmin = (id: number) => axios.delete(`${API_URL}/admin/users/${id}`);