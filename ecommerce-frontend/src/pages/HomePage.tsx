import React, { useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";

const HomePage: React.FC = () => {
  const role = localStorage.getItem("role");
  const navigate = useNavigate();

  
  useEffect(() => {
    if (role === "USER") {
      navigate("/user/dashboard");
    } else if (role === "ADMIN") {
      navigate("/admin/dashboard");
    }
  }, [role, navigate]);

  const loginButtonStyle: React.CSSProperties = {
    padding: "10px 25px",
    backgroundColor: "#4caf50",
    color: "#fff",
    borderRadius: "8px",
    textDecoration: "none",
    fontWeight: "bold",
  };

  const registerButtonStyle: React.CSSProperties = {
    padding: "10px 25px",
    backgroundColor: "#2196f3",
    color: "#fff",
    borderRadius: "8px",
    textDecoration: "none",
    fontWeight: "bold",
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        minHeight: "100vh",
        backgroundColor: "#f5f5f5",
        fontFamily: "Arial, sans-serif",
        textAlign: "center",
      }}
    >
      <h1 style={{ marginBottom: "20px" }}>Welcome to E-Commerce App</h1>

      <div style={{ display: "flex", gap: "20px" }}>
        <Link to="/login" style={loginButtonStyle}>
          Login
        </Link>

        <Link to="/register" style={registerButtonStyle}>
          Register
        </Link>
      </div>

      <p style={{ marginTop: "30px", color: "#555", fontSize: "18px" }}>
        Please login or register to start shopping.
      </p>
    </div>
  );
};

export default HomePage;