import React, { useState } from 'react';

const SignUp = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        email: '',
        phoneNumber: ''
    });
    const [message, setMessage] = useState('');
    const [errors, setErrors] = useState([]);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrors([]);
        setMessage('');

        try {
            const response = await fetch('http://localhost:8080/create_account', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            console.log(response);

            if (!response.ok) {
                const errorData = await response.json();
                setErrors(errorData);
            } else {
                const data = await response.json();
                setMessage('Account created successfully! Your user ID is ' + data.appUserId);
            }
        } catch (error) {
            setMessage('An unexpected error occurred.');
        }
    };

    return (
        <div>
            <h2>Sign Up</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Username:</label>
                    <input
                        type="text"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>Email:</label>
                    <input
                        type="text"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>Phone Number:</label>
                    <input
                        type="text"
                        name="phoneNumber"
                        value={formData.phoneNumber}
                        onChange={handleChange}
                        required
                    />
                </div>
                <button type="submit">Sign Up</button>
            </form>
            {errors.length > 0 && (
                <ul>
                    {errors.map((error, index) => (
                        <li key={index} style={{ color: 'red' }}>{error}</li>
                    ))}
                </ul>
            )}
            {message && <p>{message}</p>}
        </div>
    );
};


export default SignUp;