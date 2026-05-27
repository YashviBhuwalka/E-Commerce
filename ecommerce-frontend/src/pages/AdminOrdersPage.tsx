import React, { useEffect, useState } from "react";
import { getAllOrders } from "../services/orderService";
import { OrderResponseDTO } from "../types/Order";

const AdminOrdersPage: React.FC = () => {
  const [orders, setOrders] = useState<OrderResponseDTO[]>([]);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = () => {
    getAllOrders()
      .then(res => setOrders(res.data))
      .catch(() => alert("Failed to fetch orders"));
  };

  return (
    <div style={{ textAlign: "center", padding: "20px" }}>
      <h2>All Orders</h2>

      {orders.length > 0 ? (
        orders.map(order => (
          <div
            key={order.orderId}
            style={{
              border: "1px solid gray",
              margin: "10px auto",
              padding: "10px",
              maxWidth: "600px",
              textAlign: "center",
            }}
          >
            <p>User ID: {order.userId}</p>
            <p>Order ID: {order.orderId}</p>
            <p>Date: {new Date(order.orderDate).toLocaleString()}</p>
            <p>Total: {order.totalAmount}</p>
            <table
              border={1}
              cellPadding={5}
              cellSpacing={0}
              style={{ margin: "0 auto" }}
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
                    <td>{item.totalPrice}</td>
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

export default AdminOrdersPage;