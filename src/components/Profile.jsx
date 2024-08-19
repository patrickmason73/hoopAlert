import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../context/UserContext';

const Profile = () => {
  const { token } = useContext(UserContext);
  const [userData, setUserData] = useState(null);
  const [userTeams, setUserTeams] = useState([]);
  const [error, setError] = useState(null);
  const [triggerMessage, setTriggerMessage] = useState(''); // New state for trigger message
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

  if (error) return <p style={{ color: 'red' }}>{error}</p>;
  if (!userData) return <p>Loading...</p>;

  return (
    <div className="profile">
      <h2>Profile</h2>
      <p>Username: {userData.username}</p>
      <p>Phone: {userData.phoneNumber}</p>

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

      {/* Display the trigger message */}
      {triggerMessage && <p>{triggerMessage}</p>}
    </div>
  );
};

export default Profile;