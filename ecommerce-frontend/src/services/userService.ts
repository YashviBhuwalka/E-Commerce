import axios from "axios";
import { UserResponseDTO, UserRequestDTO } from "../types/User";

const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";

export const getMyProfile = (userId: number) => 
  axios.get<UserResponseDTO>(`${API_URL}/users/me?userId=${userId}`);

export const updateMyProfile = (userId: number, data: UserRequestDTO) =>
  axios.put<UserResponseDTO>(`${API_URL}/users/me?userId=${userId}`, data);

export const deleteMyAccount = (userId: number) =>
  axios.delete(`${API_URL}/users/me?userId=${userId}`);