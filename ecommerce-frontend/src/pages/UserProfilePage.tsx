import React, { useEffect, useState, ChangeEvent, FormEvent } from "react";
import { getMyProfile, updateMyProfile, deleteMyAccount } from "../services/userService";
import { UserResponseDTO, UserRequestDTO } from "../types/User";
import { useNavigate } from "react-router-dom";

const UserProfilePage: React.FC = () => {
  const navigate = useNavigate();

  const userId = Number(localStorage.getItem("userId"));

  const [user, setUser] = useState<UserResponseDTO | null>(null);
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (!userId) {
      navigate("/login");
      return;
    }

    getMyProfile(userId)
      .then((res) => {
        setUser(res.data);
        setUsername(res.data.username);
        setEmail(res.data.email);
      })
      .catch(() => setMessage("Failed to load profile"));
  }, [userId, navigate]);

  const handleUpdate = async (e: FormEvent) => {
    e.preventDefault();

    try {
      const data: UserRequestDTO = {
        username,
        email,
        password,
        role: user?.role || "USER",
      };

      const res = await updateMyProfile(userId, data);

      setUser(res.data);
      setMessage("Profile updated successfully");

      localStorage.setItem("username", res.data.username);

    } catch {
      setMessage("Failed to update profile");
    }
  };

  const handleDelete = async () => {
    const confirmDelete = window.confirm("Are you sure you want to delete your account?");
    if (!confirmDelete) return;

    try {
      await deleteMyAccount(userId);

      alert("Profile deleted successfully");

      localStorage.clear();
      navigate("/");

    } catch {
      setMessage("Failed to delete profile");
    }
  };

  if (!user) {
    return (
      <div style={{ textAlign: "center", marginTop: "100px" }}>
        <h2>Loading profile...</h2>
      </div>
    );
  }

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        height: "100vh",
        textAlign: "center",
      }}
    >
      <h2>My Profile</h2>

      {/* VIEW PROFILE */}
      <div style={{ marginBottom: "20px" }}>
        <p><b>Username:</b> {user.username}</p>
        <p><b>Email:</b> {user.email}</p>
        <p><b>Role:</b> {user.role}</p>
      </div>

      {/* UPDATE PROFILE */}
      <form
        onSubmit={handleUpdate}
        style={{
          display: "flex",
          flexDirection: "column",
          gap: "10px",
          width: "250px",
        }}
      >
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e: ChangeEvent<HTMLInputElement>) => setUsername(e.target.value)}
        />

        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e: ChangeEvent<HTMLInputElement>) => setEmail(e.target.value)}
        />

        <input
          type="password"
          placeholder="New Password"
          value={password}
          onChange={(e: ChangeEvent<HTMLInputElement>) => setPassword(e.target.value)}
        />

        <button type="submit">Update Profile</button>

        <button
          type="button"
          onClick={handleDelete}
        >
          Delete Profile
        </button>
      </form>

      {message && <p style={{ marginTop: "15px" }}>{message}</p>}
    </div>
  );
};

export default UserProfilePage;