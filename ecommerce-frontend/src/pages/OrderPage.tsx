import React, { useContext, useEffect, useState } from "react";
import { CartContext } from "../context/CartContext";
import { getUserOrders } from "../services/orderService";
import { OrderResponseDTO } from "../types/Order";

const OrderPage: React.FC = () => {
  const { cart, refreshCart } = useContext(CartContext); 
  const [orders, setOrders] = useState<OrderResponseDTO[]>([]);
  const [message, setMessage] = useState<string>("");

  const userId = Number(localStorage.getItem("userId"));

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = () => {
    getUserOrders(userId)
      .then(res => setOrders(res.data))
      .catch(() => setMessage("Failed to fetch orders"));
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        minHeight: "100vh",
        padding: "20px",
        textAlign: "center",
      }}
    >
      <h2>Your Orders</h2>
      {message && <p style={{ fontWeight: "bold", color: "red" }}>{message}</p>}

      <h3>Order History</h3>
      {orders.length > 0 ? (
        orders.map(order => (
          <div
            key={order.orderId}
            style={{
              border: "1px solid gray",
              margin: "10px 0",
              padding: "15px",
              borderRadius: "8px",
              width: "100%",
              maxWidth: "600px",
            }}
          >
            <p>Order ID: {order.orderId}</p>
            <p>Date: {new Date(order.orderDate).toLocaleString()}</p>
            <p>Total: ₹{order.totalAmount}</p>
            <table
              border={1}
              cellPadding={5}
              cellSpacing={0}
              style={{ width: "100%", marginTop: "10px", textAlign: "center" }}
            >
              <thead>
                <tr>
                  <th>Product</th>
                  <th>Quantity</th>
                  <th>Total Price</th>
                </tr>
              </thead>
              <tbody>
                {order.items.map(item => (
                  <tr key={item.productName}>
                    <td>{item.productName}</td>
                    <td>{item.quantity}</td>
                    <td>₹{item.totalPrice}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ))
      ) : (
        <p>No orders yet</p>
      )}
    </div>
  );
};

export default OrderPage;