import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../context/UserContext';
import '../css/SignUp.css';

const SignUp = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        email: '',
        phoneNumber: ''
    });
    const navigate = useNavigate();
    const { login } = useContext(UserContext);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/create_account', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            if (!response.ok) {
                const errorData = await response.json();
                console.error('Full error response:', errorData); // Log the full error response
                alert(`Failed to create account: ${errorData.messages.join(', ') || 'Unknown error'}`);
            } else {
                alert('Account created successfully!');

                // Automatically log the user in
                const loginResponse = await fetch('http://localhost:8080/authenticate', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        username: formData.username,
                        password: formData.password
                    })
                });

                if (loginResponse.ok) {
                    const loginData = await loginResponse.json();
                    login(loginData.jwt_token); // Use data.jwt_token
                    navigate('/profile'); // Redirect to the profile page
                } else {
                    alert('Account created, but login failed. Please log in manually.');
                }
            }
        } catch (error) {
            alert('An unexpected error occurred.');
            console.error('Sign-up error:', error); // Log unexpected errors
        }
    };

    return (
        <div className="signup-container">
      <h2 className="signup-title">Sign Up</h2>
      <form onSubmit={handleSubmit}>
        <div className="signup-form-group">
          <label className="signup-form-label">Username:</label>
          <input
            type="text"
            name="username"
            value={formData.username}
            onChange={handleChange}
            required
            className="signup-form-input"
          />
        </div>
        <div className="signup-form-group">
          <label className="signup-form-label">Password:</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
            className="signup-form-input"
          />
        </div>
        <div className="signup-form-group">
          <label className="signup-form-label">Email:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
            className="signup-form-input"
          />
        </div>
        <div className="signup-form-group">
          <label className="signup-form-label">Phone Number:</label>
          <input
            type="text"
            name="phoneNumber"
            value={formData.phoneNumber}
            onChange={handleChange}
            required
            className="signup-form-input"
          />
        </div>
        <button type="submit" className="signup-button">Sign Up</button>
      </form>
    </div>
    );
};

export default SignUp;