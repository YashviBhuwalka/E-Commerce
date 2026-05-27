import React, { useEffect, useState } from "react";
import {
  getAllProducts,
  searchProductsByName,
  searchProductsByCategory,
  searchProductsByPriceRange,
} from "../services/productService";
import { ProductResponseDTO } from "../types/Product";

const ProductsPage: React.FC = () => {
  const [products, setProducts] = useState<ProductResponseDTO[]>([]);
  const [searchName, setSearchName] = useState<string>("");
  const [searchCategory, setSearchCategory] = useState<string>("");
  const [minPrice, setMinPrice] = useState<number | "">("");
  const [maxPrice, setMaxPrice] = useState<number | "">("");

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = () => {
    getAllProducts()
      .then((res) => setProducts(res.data))
      .catch(() => alert("Failed to fetch products"));
  };

  const handleSearchByName = () => {
    if (!searchName) return fetchProducts();

    searchProductsByName(searchName)
      .then((res) => setProducts(res.data))
      .catch(() => alert("Search failed"));
  };

  const handleSearchByCategory = () => {
    if (!searchCategory) return fetchProducts();

    searchProductsByCategory(searchCategory)
      .then((res) => setProducts(res.data))
      .catch(() => alert("Search failed"));
  };

  const handleSearchByPrice = () => {
    if (minPrice === "" || maxPrice === "") return fetchProducts();

    searchProductsByPriceRange(Number(minPrice), Number(maxPrice))
      .then((res) => setProducts(res.data))
      .catch(() => alert("Search failed"));
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
      }}
    >
      <h2>Products</h2>

      {/* View All Products */}
      <button
        onClick={fetchProducts}
        style={{
          marginBottom: "20px",
          padding: "10px",
          borderRadius: "4px",
          border: "1px solid black",
          cursor: "pointer",
        }}
      >
        View All Products
      </button>

      {/* Search by Name */}
      <div style={{ marginBottom: "10px" }}>
        <input
          type="text"
          placeholder="Search by name"
          value={searchName}
          onChange={(e) => setSearchName(e.target.value)}
          style={{ marginRight: "10px", padding: "5px" }}
        />
        <button onClick={handleSearchByName}>Search</button>
      </div>

      {/* Search by Category */}
      <div style={{ marginBottom: "10px" }}>
        <input
          type="text"
          placeholder="Search by category"
          value={searchCategory}
          onChange={(e) => setSearchCategory(e.target.value)}
          style={{ marginRight: "10px", padding: "5px" }}
        />
        <button onClick={handleSearchByCategory}>Search</button>
      </div>

      {/* Search by Price */}
      <div style={{ marginBottom: "20px" }}>
        <input
          type="number"
          placeholder="Min Price"
          value={minPrice}
          onChange={(e) =>
            setMinPrice(e.target.value === "" ? "" : Number(e.target.value))
          }
          style={{ marginRight: "10px", padding: "5px", width: "120px" }}
        />

        <input
          type="number"
          placeholder="Max Price"
          value={maxPrice}
          onChange={(e) =>
            setMaxPrice(e.target.value === "" ? "" : Number(e.target.value))
          }
          style={{ marginRight: "10px", padding: "5px", width: "120px" }}
        />

        <button onClick={handleSearchByPrice}>Search</button>
      </div>

      {/* Products Table */}
      <table
        border={1}
        cellPadding={5}
        cellSpacing={0}
        style={{ minWidth: "600px" }}
      >
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
          </tr>
        </thead>

        <tbody>
          {products.map((p) => (
            <tr key={p.productId}>
              <td>{p.productId}</td>
              <td>{p.productName}</td>
              <td>{p.productCategory}</td>
              <td>{p.productPrice}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ProductsPage;