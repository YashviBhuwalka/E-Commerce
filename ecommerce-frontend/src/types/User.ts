export type Role = "USER" | "ADMIN";

export interface UserRequestDTO {
  username: string;
  email: string;
  password: string;
  role: Role;
}

export interface UserResponseDTO {
  userId: number;
  username: string;
  email: string;
  role: Role;
  deleted: boolean;
}