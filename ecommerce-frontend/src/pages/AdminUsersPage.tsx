import React, { useEffect, useState, ChangeEvent, FormEvent } from "react";
import { getAllUsers, updateUserByAdmin, deleteUserByAdmin } from "../services/adminService";
import { UserResponseDTO, UserRequestDTO, Role } from "../types/User";
import axios from "axios";

const AdminUsersPage: React.FC = () => {
  const [users, setUsers] = useState<UserResponseDTO[]>([]);
  const [editingUserId, setEditingUserId] = useState<number | null>(null);
  const [formData, setFormData] = useState<Partial<UserRequestDTO>>({});
  const [message, setMessage] = useState<string>("");
  const [searchId, setSearchId] = useState<string>("");

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = () => {
    getAllUsers()
      .then((res) => setUsers(res.data))
      .catch(() => setMessage("Failed to load users"));
  };

  const getUserById = async () => {
    if (!searchId) {
      setMessage("Please enter a user ID");
      return;
    }

    try {
      const res = await axios.get<UserResponseDTO>(
        `http://localhost:8080/admin/users/${searchId}`
      );
      setUsers([res.data]);
      setMessage("");
    } catch {
      setMessage("User not found");
    }
  };

  const startEdit = (user: UserResponseDTO) => {
    if (user.deleted) return;

    setEditingUserId(user.userId);
    setFormData({
      username: user.username,
      email: user.email,
      password: "",
      role: user.role,
    });
  };

  const cancelEdit = () => {
    setEditingUserId(null);
    setFormData({});
  };

  const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleUpdate = async (e: FormEvent) => {
    e.preventDefault();
    if (!editingUserId) return;

    try {
      const data: UserRequestDTO = {
        username: formData.username || "",
        email: formData.email || "",
        password: formData.password || "default123",
        role: (formData.role as Role) || "USER",
      };

      await updateUserByAdmin(editingUserId, data);
      setMessage("User updated successfully");
      setEditingUserId(null);
      fetchUsers();
    } catch {
      setMessage("Failed to update user");
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm("Are you sure you want to delete this user?")) return;

    try {
      await deleteUserByAdmin(id);
      setMessage("User deleted successfully");
      fetchUsers();
    } catch {
      setMessage("Failed to delete user");
    }
  };

  return (
    <div style={{ textAlign: "center", padding: "20px" }}>
      <h2>Admin - Manage Users</h2>

      {message && <p>{message}</p>}

      {/* Search User by ID */}
      <div style={{ marginBottom: "20px" }}>
        <input
          type="number"
          placeholder="Enter User ID"
          value={searchId}
          onChange={(e) => setSearchId(e.target.value)}
          style={{ padding: "5px", marginRight: "10px" }}
        />

        <button onClick={getUserById}>Search</button>

        {/* Move button to next line */}
        <div style={{ marginTop: "10px" }}>
          <button onClick={fetchUsers}>View All Users</button>
        </div>
      </div>

      <table
        border={1}
        cellPadding={5}
        cellSpacing={0}
        style={{ margin: "0 auto", minWidth: "700px" }}
      >
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Role</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {users.map((user) => (
            <tr key={user.userId} style={{ opacity: user.deleted ? 0.5 : 1 }}>
              {editingUserId === user.userId ? (
                <>
                  <td>{user.userId}</td>

                  <td>
                    <input
                      name="username"
                      value={formData.username}
                      onChange={handleChange}
                    />
                  </td>

                  <td>
                    <input
                      name="email"
                      value={formData.email}
                      onChange={handleChange}
                    />
                  </td>

                  <td>
                    <select
                      name="role"
                      value={formData.role}
                      onChange={handleChange}
                    >
                      <option value="USER">USER</option>
                      <option value="ADMIN">ADMIN</option>
                    </select>
                  </td>

                  <td>{user.deleted ? "Deleted" : "Active"}</td>

                  <td>
                    <button onClick={handleUpdate}>Save</button>
                    <button onClick={cancelEdit}>Cancel</button>
                  </td>
                </>
              ) : (
                <>
                  <td>{user.userId}</td>
                  <td>{user.username}</td>
                  <td>{user.email}</td>
                  <td>{user.role}</td>

                  <td>{user.deleted ? "Deleted" : "Active"}</td>

                  <td>
                    <button
                      onClick={() => startEdit(user)}
                      disabled={user.deleted}
                    >
                      Edit
                    </button>

                    <button
                      onClick={() => handleDelete(user.userId)}
                      disabled={user.deleted}
                      style={{ marginLeft: "5px" }}
                    >
                      Delete
                    </button>
                  </td>
                </>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminUsersPage;