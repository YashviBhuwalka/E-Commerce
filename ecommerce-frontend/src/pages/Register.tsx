import React, { useState, ChangeEvent, FormEvent } from "react";
import { registerUser } from "../services/authService";
import { UserRequestDTO } from "../types/User";
import { useNavigate } from "react-router-dom";

const Register: React.FC = () => {
  const [formData, setFormData] = useState<UserRequestDTO>({
    username: "",
    email: "",
    password: "",
    role: "USER",
  });
  const [message, setMessage] = useState<string>("");
  const [registrationSuccess, setRegistrationSuccess] = useState<boolean>(false);
  const navigate = useNavigate();

  const handleChange = (
    e: ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    const updatedValue = name === "role" ? (value as "USER" | "ADMIN") : value;
    setFormData({ ...formData, [name]: updatedValue });
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (!formData.username || !formData.email || !formData.password) {
      setMessage("Please fill in all fields.");
      return;
    }

    try {
      const response = await registerUser(formData);
      setMessage(`User "${response.data.username}" registered successfully!`);
      setRegistrationSuccess(true);

      // Clear form
      setFormData({
        username: "",
        email: "",
        password: "",
        role: "USER",
      });
    } catch (err: any) {
      console.error(err);
      setMessage(
        err.response?.data?.message || "Registration failed. Please try again."
      );
      setRegistrationSuccess(false);
    }
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        minHeight: "100vh",
        backgroundColor: "#fff",
        fontFamily: "Arial, sans-serif",
        color: "#000",
        textAlign: "center",
        padding: "20px",
      }}
    >
      <h2 style={{ marginBottom: "20px" }}>Register</h2>

      <form
        onSubmit={handleSubmit}
        style={{
          display: "flex",
          flexDirection: "column",
          gap: "15px",
          width: "300px",
          padding: "20px",
          borderRadius: "8px",
          border: "1px solid #000",
        }}
      >
        <input
          type="text"
          name="username"
          placeholder="Username"
          value={formData.username}
          onChange={handleChange}
          style={{
            padding: "10px",
            borderRadius: "4px",
            border: "1px solid #000",
            fontSize: "16px",
            color: "#000",
          }}
          required
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleChange}
          style={{
            padding: "10px",
            borderRadius: "4px",
            border: "1px solid #000",
            fontSize: "16px",
            color: "#000",
          }}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          value={formData.password}
          onChange={handleChange}
          style={{
            padding: "10px",
            borderRadius: "4px",
            border: "1px solid #000",
            fontSize: "16px",
            color: "#000",
          }}
          required
        />
        <select
          name="role"
          value={formData.role}
          onChange={handleChange}
          style={{
            padding: "10px",
            borderRadius: "4px",
            border: "1px solid #000",
            fontSize: "16px",
            color: "#000",
          }}
        >
          <option value="USER">User</option>
          <option value="ADMIN">Admin</option>
        </select>

        <button
          type="submit"
          style={{
            padding: "10px",
            borderRadius: "4px",
            border: "1px solid #000",
            backgroundColor: "#000",
            color: "#fff",
            fontWeight: "bold",
            cursor: "pointer",
          }}
        >
          Register
        </button>
      </form>

      {message && (
        <p
          style={{
            marginTop: "20px",
            color: registrationSuccess ? "green" : "red",
          }}
        >
          {message}
        </p>
      )}

      {/* Show login button if registration was successful */}
      {registrationSuccess && (
        <button
          onClick={() => navigate("/login")}
          style={{
            marginTop: "15px",
            padding: "10px 20px",
            borderRadius: "4px",
            border: "1px solid #000",
            backgroundColor: "#000",
            color: "#fff",
            fontWeight: "bold",
            cursor: "pointer",
          }}
        >
          Go to Login
        </button>
      )}
    </div>
  );
};

export default Register;