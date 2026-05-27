import React, { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { CartContext } from "../context/CartContext";
import { placeOrder } from "../services/orderService";
import { OrderRequestDTO } from "../types/Order";

const CheckoutPage: React.FC = () => {
  const { cart, refreshCart } = useContext(CartContext);
  const [shippingAddress, setShippingAddress] = useState<string>("");
  const [message, setMessage] = useState<string>("");
  const [error, setError] = useState<string>("");

  
  const userId = Number(localStorage.getItem("userId"));
  const navigate = useNavigate();

  const handlePlaceOrder = async () => {
    setMessage("");
    setError("");

    
    if (!shippingAddress.trim()) {
      setError("Please enter a shipping address");
      return;
    }

    if (!cart || cart.items.length === 0) {
      setError("Your cart is empty");
      return;
    }

  
    const orderRequest: OrderRequestDTO = {
      userId,
      shippingAddress: shippingAddress.trim(),
    };

    try {
      await placeOrder(orderRequest);
      setMessage("Order placed successfully!");
      await refreshCart(); 
      setShippingAddress(""); 
    } catch (err: any) {
      console.error("Order placement error:", err);

      if (err.response?.data?.message) {
        setError(`${err.response.data.message}`);
      } else {
        setError("Failed to place order. Try again later.");
      }
    }
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        minHeight: "100vh",
        padding: "20px",
      }}
    >
      <h2>Checkout</h2>

      {/* Success or error messages */}
      {message && (
        <p style={{ color: "green", fontWeight: "bold", marginBottom: "15px" }}>
          {message}
        </p>
      )}
      {error && (
        <p style={{ color: "red", fontWeight: "bold", marginBottom: "15px" }}>
          {error}
        </p>
      )}

      {/* Cart Summary */}
      {cart && cart.items.length > 0 ? (
        <div style={{ marginBottom: "20px", width: "100%", maxWidth: "600px" }}>
          <h3>Cart Summary</h3>
          <table border={1} cellPadding={6} cellSpacing={0} width="100%">
            <thead>
              <tr>
                <th>Product</th>
                <th>Quantity</th>
                <th>Total Price</th>
              </tr>
            </thead>
            <tbody>
              {cart.items.map((item) => (
                <tr key={item.productName}>
                  <td>{item.productName}</td>
                  <td>{item.quantity}</td>
                  <td>₹{item.totalPrice}</td>
                </tr>
              ))}
            </tbody>
          </table>
          <p style={{ marginTop: "10px", fontWeight: "bold" }}>
            Total Price: ₹{cart.totalPrice}
          </p>
        </div>
      ) : (
        <p>Your cart is empty</p>
      )}

      {/* Shipping Details & Place Order */}
      {cart && cart.items.length > 0 && (
        <div style={{ marginBottom: "20px" }}>
          <h3>Shipping Details</h3>
          <input
            type="text"
            placeholder="Enter Shipping Address"
            value={shippingAddress}
            onChange={(e) => setShippingAddress(e.target.value)}
            style={{ width: "300px", padding: "6px", marginRight: "10px" }}
          />
          <button
            onClick={handlePlaceOrder}
            style={{
              padding: "6px 12px",
              backgroundColor: "green",
              color: "white",
              fontWeight: "bold",
              border: "none",
              cursor: "pointer",
            }}
          >
            Place Order
          </button>
        </div>
      )}

      {/* Navigation Buttons */}
      <div>
        <button
          onClick={() => navigate("/cart")}
          style={{
            padding: "6px 12px",
            marginRight: "10px",
            cursor: "pointer",
          }}
        >
          Back to Cart
        </button>

        <button
          onClick={() => navigate("/dashboard")}
          style={{
            padding: "6px 12px",
            cursor: "pointer",
          }}
        >
          Back to Dashboard
        </button>
      </div>
    </div>
  );
};

export default CheckoutPage;