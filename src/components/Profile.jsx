import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../context/UserContext';

const Profile = () => {
  const { token } = useContext(UserContext);
  const [userData, setUserData] = useState({
    username: '',
    phoneNumber: '',
    email: ''
  });
  const [userTeams, setUserTeams] = useState([]);
  const [error, setError] = useState(null);
  const [triggerMessage, setTriggerMessage] = useState(''); // New state for trigger message
  const [updateMessage, setUpdateMessage] = useState(''); // State to show success/error messages for updates
  const [isEditing, setIsEditing] = useState(false); // State to toggle edit form visibility
  const [editData, setEditData] = useState({ ...userData }); // Separate state for form input


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

        // Fetch teams associated with the user
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

    console.log('Token in Profile:', token);


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
      setUserData(updatedUser); // Update main state after successful update
      setUpdateMessage('Profile updated successfully!');
      setIsEditing(false); // Hide the form after successful update
    } catch (err) {
      setError(err.message);
      setUpdateMessage('Failed to update profile.');
    }
  };

  const toggleEditForm = () => {
    setIsEditing(!isEditing);
    if (!isEditing) {
      setEditData(userData); // Reset edit form data when opening the form
    }
  };


  if (error) return <p style={{ color: 'red' }}>{error}</p>;
  if (!userData) return <p>Loading...</p>;

  return (
    <div className="profile">
      <h2>Profile</h2>
      <p>Username: {userData.username}</p>
      <p>Phone: {userData.phoneNumber}</p>
      <p>Email: {userData.email}</p>

      <button onClick={toggleEditForm}>
        {isEditing ? 'Cancel Edit' : 'Edit Profile'}
      </button>

      {isEditing && (
        <form onSubmit={handleFormSubmit}>
          <div>
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
          <div>
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
          <div>
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
          <button type="submit">Update Profile</button>
        </form>
      )}

      {updateMessage && <p>{updateMessage}</p>}

      <h3>Your Teams:</h3>
      <ul>
        {userTeams.map((team) => (
          <li key={team.id}>{team.teamName}</li>
        ))}
      </ul>

      <button onClick={() => navigate('/team-selection')}>
        Select More Teams
      </button>

      <button onClick={triggerRemindersForToday}>
        Trigger Today's Reminders
      </button>

      {triggerMessage && <p>{triggerMessage}</p>}
    </div>
  );
};

export default Profile;