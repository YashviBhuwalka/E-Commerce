import axios from "axios";
import { UserRequestDTO, UserResponseDTO } from "../types/User";

const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080/api/auth";

export const registerUser = (userData: UserRequestDTO) => {
  return axios.post<UserResponseDTO>(`${API_URL}/register`, userData);
};

export const loginUser = (email: string, password: string) => {
  return axios.post<UserResponseDTO>(`${API_URL}/login`, null, { params: { email, password } });
};