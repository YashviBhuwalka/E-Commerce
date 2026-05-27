import React, { createContext, useState, useEffect, ReactNode } from "react";
import { CartResponseDTO } from "../types/Cart";
import { viewCart } from "../services/cartService";

interface CartContextType {
  cart: CartResponseDTO | null;
  setCart: React.Dispatch<React.SetStateAction<CartResponseDTO | null>>;
  refreshCart: () => Promise<CartResponseDTO | null>; 
}

export const CartContext = createContext<CartContextType>({
  cart: null,
  setCart: () => {},
  refreshCart: async () => null, 
});

export const CartProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [cart, setCart] = useState<CartResponseDTO | null>(null);
  const userId = Number(localStorage.getItem("userId"));
  const role = localStorage.getItem("role");

  const refreshCart = async (): Promise<CartResponseDTO | null> => {
    if (userId && role === "USER") {
      try {
        const res = await viewCart(userId);
        setCart(res.data);
        return res.data; 
      } catch {
        setCart(null);
        return null;
      }
    }
    return null;
  };

  useEffect(() => {
    refreshCart();
  }, [userId, role]);

  return (
    <CartContext.Provider value={{ cart, setCart, refreshCart }}>
      {children}
    </CartContext.Provider>
  );
};