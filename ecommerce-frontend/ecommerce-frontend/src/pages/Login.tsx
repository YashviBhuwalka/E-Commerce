import React, { useState, FormEvent } from "react";
import { loginUser } from "../services/authService";
import { UserResponseDTO } from "../types/User";
import { useNavigate } from "react-router-dom";

const Login: React.FC = () => {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [message, setMessage] = useState<string>("");
  const [showRegister, setShowRegister] = useState<boolean>(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    try {
      const response = await loginUser(email, password);
      const user: UserResponseDTO = response.data;
      
      localStorage.setItem("userId", user.userId.toString());
      localStorage.setItem("username", user.username);
      localStorage.setItem("role", user.role); // "USER" or "ADMIN"

      setMessage(`Welcome ${user.username}!`);

      if (user.role === "USER") {
        navigate("/user/dashboard");
      } else if (user.role === "ADMIN") {
        navigate("/admin/dashboard");
      }
    } catch (err: any) {
      setMessage(err.response?.data?.message || "Login failed. If you don't have an account, please register.");
      setShowRegister(true); 
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
      <h2 style={{ marginBottom: "20px" }}>Login</h2>
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
          type="email"
          placeholder="Email"
          value={email}
          onChange={e => setEmail(e.target.value)}
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
          placeholder="Password"
          value={password}
          onChange={e => setPassword(e.target.value)}
          style={{
            padding: "10px",
            borderRadius: "4px",
            border: "1px solid #000",
            fontSize: "16px",
            color: "#000",
          }}
          required
        />
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
          Login
        </button>
      </form>
      {message && <p style={{ marginTop: "20px" }}>{message}</p>}

      {showRegister && (
  <button
    onClick={() => navigate("/register")}
    style={{
      marginTop: "10px",
      padding: "10px",
      borderRadius: "4px",
      border: "1px solid #000",
      backgroundColor: "#fff",
      cursor: "pointer"
    }}
  >
    Register
  </button>
)}
    </div>
  );
};

export default Login;