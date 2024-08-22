import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserContext } from "../context/UserContext";
import '../css/Login.css';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const { login } = useContext(UserContext);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
  
    try {
      const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/authenticate`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });
  
      if (!response.ok) {
        const errorData = await response.json();
        setError(errorData.message || 'Login failed');
        return;
      }
  
      const data = await response.json();
      console.log('Login response:', data);
      
      login(data.jwt_token);  // Use data.jwt_token since that's what the backend is returning
      navigate('/profile'); // Redirect to the profile page
    } catch (err) {
      console.error('Login error:', err);
      setError(err.message);
    }
  };

  return (
    <div className="login-container">
    <h2 className="login-title">Login</h2>
    <form onSubmit={handleLogin}>
      <div className="login-form-group">
        <label className="login-form-label">Username</label>
        <input
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
          className="login-form-input"
        />
      </div>
      <div className="login-form-group">
        <label className="login-form-label">Password</label>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          className="login-form-input"
        />
      </div>
      <button type="submit" className="login-button">Login</button>
      {error && <p className="error-message">{error}</p>}
    </form>
  </div>
  );
};

export default Login;