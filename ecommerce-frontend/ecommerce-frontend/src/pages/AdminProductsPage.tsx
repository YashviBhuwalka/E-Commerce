import React, { useEffect, useState, ChangeEvent, FormEvent } from "react";
import {
  getAllProductsAdmin,
  addProduct,
  updateProduct,
  deleteProduct,
  getProductById,
} from "../services/productService";
import { ProductResponseDTO, ProductRequestDTO } from "../types/Product";

const AdminProductsPage: React.FC = () => {
  const [products, setProducts] = useState<ProductResponseDTO[]>([]);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [formData, setFormData] = useState<Partial<ProductRequestDTO>>({});
  const [message, setMessage] = useState<string>("");

  const [searchId, setSearchId] = useState<string>("");

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = () => {
    getAllProductsAdmin()
      .then((res) => setProducts(res.data))
      .catch(() => setMessage("Failed to fetch products"));
  };

  const handleGetProductById = async () => {
    if (!searchId) {
      setMessage("Please enter a product ID");
      return;
    }

    try {
      const res = await getProductById(Number(searchId));
      setProducts([res.data]);
      setMessage("");
    } catch {
      setMessage("Product not found!");
    }
  };

  const startEdit = (product: ProductResponseDTO) => {
    if (product.deleted) return;

    setEditingId(product.productId);
    setFormData({
      productName: product.productName,
      productCategory: product.productCategory,
      productPrice: product.productPrice,
    });
  };

  const cancelEdit = () => {
    setEditingId(null);
    setFormData({});
  };

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: name === "productPrice" ? parseFloat(value) : value,
    });
  };

  const handleUpdate = async (e: FormEvent) => {
    e.preventDefault();

    if (!editingId || !formData.productName || !formData.productCategory || !formData.productPrice)
      return;

    try {
      await updateProduct(editingId, formData as ProductRequestDTO);
      setMessage("Product updated successfully");
      setEditingId(null);
      setFormData({});
      fetchProducts();
    } catch {
      setMessage("Failed to update product");
    }
  };

  const handleAdd = async () => {
    if (!formData.productName || !formData.productCategory || !formData.productPrice) return;

    try {
      await addProduct(formData as ProductRequestDTO);
      setMessage("Product added successfully");
      setFormData({});
      fetchProducts();
    } catch {
      setMessage("Failed to add product");
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm("Delete this product?")) return;

    try {
      await deleteProduct(id);
      setMessage("Product deleted successfully");
      fetchProducts();
    } catch {
      setMessage("Failed to delete product");
    }
  };

  return (
    <div style={{ textAlign: "center", padding: "20px" }}>
      <h2>Admin - Manage Products</h2>

      {message && (
        <p style={{ color: message === "Product not found!" ? "green" : "red", fontWeight: "bold" }}>
      {message}
        </p>
      )}
      

      {/* Product Form */}
      <div style={{ marginBottom: "25px" }}>
        <input
          name="productName"
          placeholder="Name"
          value={formData.productName || ""}
          onChange={handleChange}
          style={{ display: "block", margin: "10px auto", padding: "5px" }}
        />

        <input
          name="productCategory"
          placeholder="Category"
          value={formData.productCategory || ""}
          onChange={handleChange}
          style={{ display: "block", margin: "10px auto", padding: "5px" }}
        />

        <input
          name="productPrice"
          type="number"
          placeholder="Price"
          value={formData.productPrice || ""}
          onChange={handleChange}
          style={{ display: "block", margin: "10px auto", padding: "5px" }}
        />

        {editingId ? (
          <>
            <button onClick={handleUpdate}>Save</button>
            <button onClick={cancelEdit} style={{ marginLeft: "10px" }}>
              Cancel
            </button>
          </>
        ) : (
          <button onClick={handleAdd}>Add Product</button>
        )}
      </div>

      {/* Search Product by ID */}
      <div style={{ marginBottom: "20px" }}>
        <input
          type="number"
          placeholder="Enter Product ID"
          value={searchId}
          onChange={(e) => setSearchId(e.target.value)}
          style={{ padding: "5px", marginRight: "10px" }}
        />

        <button onClick={handleGetProductById}>Search</button>

        <div style={{ marginTop: "10px" }}>
          <button onClick={fetchProducts}>View All Products</button>
        </div>
      </div>

      {/* Products Table */}
      <table
        border={1}
        cellPadding={5}
        cellSpacing={0}
        style={{ margin: "0 auto", minWidth: "700px" }}
      >
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {products.map((p) => (
            <tr key={p.productId} style={{ opacity: p.deleted ? 0.5 : 1 }}>
              <td>{p.productId}</td>
              <td>{p.productName}</td>
              <td>{p.productCategory}</td>
              <td>{p.productPrice}</td>

              <td>{p.deleted ? "Deleted" : "Active"}</td>

              <td>
                <button onClick={() => startEdit(p)} disabled={p.deleted}>
                  Edit
                </button>

                <button
                  onClick={() => handleDelete(p.productId)}
                  disabled={p.deleted}
                  style={{ marginLeft: "5px" }}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminProductsPage;