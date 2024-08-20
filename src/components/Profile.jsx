import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../context/UserContext';
import '../css/Profile.css';

const Profile = () => {
  const { token } = useContext(UserContext);
  const [userData, setUserData] = useState({
    username: '',
    phoneNumber: '',
    email: ''
  });
  const [userTeams, setUserTeams] = useState([]);
  const [error, setError] = useState(null);
  const [triggerMessage, setTriggerMessage] = useState('');
  const [updateMessage, setUpdateMessage] = useState('');
  const [isEditing, setIsEditing] = useState(false);
  const [editData, setEditData] = useState({ ...userData });

  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/users/profile`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });

        if (!response.ok) {
          throw new Error('Failed to fetch user data');
        }

        const data = await response.json();
        setUserData(data);

        const teamsResponse = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/users/${data.id}/teams`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });

        if (!teamsResponse.ok) {
          throw new Error('Failed to fetch teams');
        }

        const teamsData = await teamsResponse.json();
        setUserTeams(teamsData);

      } catch (err) {
        setError(err.message);
      }
    };

    if (token) {
      fetchUserData();
    }
  }, [token]);

  const triggerRemindersForToday = () => {
    fetch(`${import.meta.env.VITE_API_BASE_URL}/api/reminders/trigger/today`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    })
      .then(response => response.text())
      .then(data => {
        setTriggerMessage(data);
      })
      .catch(error => {
        console.error('Error triggering reminders:', error);
        setTriggerMessage('Failed to trigger reminders.');
      });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditData({
      ...editData,
      [name]: value,
    });
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/users/${userData.id}`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(editData),
      });

      if (!response.ok) {
        throw new Error('Failed to update user data');
      }

      const updatedUser = await response.json();
      setUserData(updatedUser);
      setUpdateMessage('Profile updated successfully!');
      setIsEditing(false);
    } catch (err) {
      setError(err.message);
      setUpdateMessage('Failed to update profile.');
    }
  };

  const toggleEditForm = () => {
    setIsEditing(!isEditing);
    if (!isEditing) {
      setEditData(userData);
    }
  };

  if (error) return <p className="error-message">{error}</p>;
  if (!userData) return <p>Loading...</p>;

  return (
    <div className="profile-container">
      <h2 className="profile-title">Profile</h2>
      <div className="profile-info">
        <p><strong>Username:</strong> {userData.username}</p>
        <p><strong>Phone:</strong> {userData.phoneNumber}</p>
        <p><strong>Email:</strong> {userData.email}</p>
      </div>

      <button onClick={toggleEditForm} className="profile-button">
        {isEditing ? 'Cancel Edit' : 'Edit Profile'}
      </button>

      {isEditing && (
        <form onSubmit={handleFormSubmit} className="edit-form">
          <div className="form-group">
            <label htmlFor="username">Username:</label>
            <input
              type="text"
              id="username"
              name="username"
              value={editData.username}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="phoneNumber">Phone:</label>
            <input
              type="text"
              id="phoneNumber"
              name="phoneNumber"
              value={editData.phoneNumber}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="email">Email:</label>
            <input
              type="email"
              id="email"
              name="email"
              value={editData.email}
              onChange={handleInputChange}
              required
            />
          </div>
          <button type="submit" className="profile-button">Update Profile</button>
        </form>
      )}

      {updateMessage && <p className="success-message">{updateMessage}</p>}

      <h3>Your Teams:</h3>
      <ul className="teams-list">
        {userTeams.map((team) => (
          <li key={team.id} className="team-item">{team.teamName}</li>
        ))}
      </ul>

      <button onClick={() => navigate('/team-selection')} className="profile-button">
        Select More Teams
      </button>

      <button onClick={triggerRemindersForToday} className="profile-button">
        Trigger Today's Reminders
      </button>

      {triggerMessage && <p className="success-message">{triggerMessage}</p>}
    </div>
  );
};

export default Profile;
