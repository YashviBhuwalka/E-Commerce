import React, { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { CartContext } from "../context/CartContext";
import {
  addItemToCart,
  removeItemFromCart,
  clearCart,
} from "../services/cartService";
import { getAllProducts } from "../services/productService";
import { CartItemRequestDTO } from "../types/Cart";
import { ProductResponseDTO } from "../types/Product";

const CartPage: React.FC = () => {
  const { cart, refreshCart } = useContext(CartContext);
  const navigate = useNavigate();

  const [products, setProducts] = useState<ProductResponseDTO[]>([]);
  const [message, setMessage] = useState<string>("");
  const [newItem, setNewItem] = useState<CartItemRequestDTO>({
    productName: "",
    quantity: 1,
  });

  const userId = Number(localStorage.getItem("userId"));

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = () => {
    getAllProducts()
      .then((res) => setProducts(res.data))
      .catch(() => alert("Failed to fetch products"));
  };

  const handleAddItem = async () => {
    if (!newItem.productName || newItem.quantity < 1) return;

    try {
    await addItemToCart(userId, { items: [newItem] });
    setMessage("Product added to cart successfully");
    setNewItem({ productName: "", quantity: 1 });
    await refreshCart(); 
  } catch {
    setMessage("Failed to add item to cart");
    }
  };

  const handleRemoveItem = async (productName: string) => {
  try {
    await removeItemFromCart(userId, productName);
    setMessage("Item removed from cart");
    await refreshCart();
  } catch {
    setMessage("Failed to remove item");
    }
  };

  const handleClearCart = async () => {
  if (!window.confirm("Clear cart?")) return;
  try {
    await clearCart(userId);
    setMessage("Cart cleared successfully");
    await refreshCart();
  } catch {
    setMessage("Failed to clear cart");
    }
  };

  const handleProceedToCheckout = () => {
    navigate("/checkout"); 
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        minHeight: "100vh",
        textAlign: "center",
        padding: "20px",
      }}
    >
      <h2>Your Cart</h2>

      {message && (
        <p style={{ marginBottom: "15px", fontWeight: "bold" }}>{message}</p>
      )}

      {/* Add Product Section */}
      <div style={{ marginBottom: "20px" }}>
        <select
          value={newItem.productName}
          onChange={(e) =>
            setNewItem({ ...newItem, productName: e.target.value })
          }
          style={{ marginRight: "10px", padding: "6px" }}
        >
          <option value="">Select Product</option>
          {products.map((p) => (
            <option key={p.productId} value={p.productName}>
              {p.productName} - ₹{p.productPrice}
            </option>
          ))}
        </select>

        <input
          type="number"
          min={1}
          value={newItem.quantity}
          onChange={(e) =>
            setNewItem({ ...newItem, quantity: Number(e.target.value) })
          }
          style={{ marginRight: "10px", padding: "6px", width: "80px" }}
        />

        <button onClick={handleAddItem}>Add To Cart</button>
      </div>

      {/* Cart Details */}
      {cart && cart.items.length > 0 ? (
        <div>
          <h3>Cart Details</h3>

          <table border={1} cellPadding={6} cellSpacing={0}>
            <thead>
              <tr>
                <th>Product</th>
                <th>Quantity</th>
                <th>Total Price</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {cart.items.map((item) => (
                <tr key={item.productName}>
                  <td>{item.productName}</td>
                  <td>{item.quantity}</td>
                  <td>₹{item.totalPrice}</td>
                  <td>
                    <button onClick={() => handleRemoveItem(item.productName)}>
                      Remove
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <p style={{ marginTop: "10px", fontWeight: "bold" }}>
            Total Price: ₹{cart.totalPrice}
          </p>

          <div style={{ marginTop: "10px" }}>
            <button
              onClick={handleClearCart}
              style={{ marginRight: "10px", padding: "6px 12px" }}
            >
              Clear Cart
            </button>

            <button
              onClick={handleProceedToCheckout}
              style={{
                padding: "6px 12px",
                backgroundColor: "#4CAF50",
                color: "white",
                fontWeight: "bold",
                border: "none",
                cursor: "pointer",
              }}
            >
              Proceed to Checkout
            </button>
          </div>
        </div>
      ) : (
        <p>Your cart is empty</p>
      )}
    </div>
  );
};

export default CartPage;