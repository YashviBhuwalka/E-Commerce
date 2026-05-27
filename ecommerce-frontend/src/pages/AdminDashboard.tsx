import React from "react";
import { useNavigate } from "react-router-dom";

const AdminDashboard: React.FC = () => {
  const navigate = useNavigate();
  const username = localStorage.getItem("username") || "";

  const handleLogout = () => {
    localStorage.clear();
    navigate("/homepage");
  };

  const buttonStyle: React.CSSProperties = {
    padding: "12px 25px",      
    fontSize: "16px",
    fontWeight: "bold",
    borderRadius: "6px",
    border: "none",
    cursor: "pointer",
    backgroundColor: "black",  
    color: "white",            
    transition: "0.2s",
  };

  const hoverStyle = {
    filter: "brightness(85%)", 
  };

  return (
    <div style={{ textAlign: "center", padding: 50 }}>
      <h1>Welcome {username}</h1>
      <div
        style={{
          marginTop: 30,
          display: "flex",
          justifyContent: "center",
          gap: 15,
          flexWrap: "wrap",
        }}
      >
        <button
          style={buttonStyle}
          onMouseOver={(e) => Object.assign(e.currentTarget.style, hoverStyle)}
          onMouseOut={(e) => Object.assign(e.currentTarget.style, buttonStyle)}
          onClick={() => navigate("/admin/users")}
        >
          Manage Users
        </button>
        <button
          style={buttonStyle}
          onMouseOver={(e) => Object.assign(e.currentTarget.style, hoverStyle)}
          onMouseOut={(e) => Object.assign(e.currentTarget.style, buttonStyle)}
          onClick={() => navigate("/admin/products")}
        >
          Manage Products
        </button>
        <button
          style={buttonStyle}
          onMouseOver={(e) => Object.assign(e.currentTarget.style, hoverStyle)}
          onMouseOut={(e) => Object.assign(e.currentTarget.style, buttonStyle)}
          onClick={() => navigate("/admin/orders")}
        >
          View Orders
        </button>
        <button
          style={buttonStyle}
          onMouseOver={(e) => Object.assign(e.currentTarget.style, hoverStyle)}
          onMouseOut={(e) => Object.assign(e.currentTarget.style, buttonStyle)}
          onClick={handleLogout}
        >
          Logout
        </button>
      </div>
    </div>
  );
};

export default AdminDashboard;