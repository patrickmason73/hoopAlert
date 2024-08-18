import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../context/UserContext';

const Profile = () => {
  const { token } = useContext(UserContext);
  const [userData, setUserData] = useState(null);
  const [userTeams, setUserTeams] = useState([]);
  const [error, setError] = useState(null);
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

  if (error) return <p style={{ color: 'red' }}>{error}</p>;
  if (!userData) return <p>Loading...</p>;

  return (
    <div className="profile">
      <h2>Profile</h2>
      <p>Username: {userData.username}</p>
      <p>Email: {userData.email}</p>

      <h3>Your Teams:</h3>
      <ul>
        {userTeams.map((team) => (
          <li key={team.id}>{team.teamName}</li>
        ))}
      </ul>

      <button onClick={() => navigate('/team-selection')}>
        Select More Teams
      </button>
    </div>
  );
};

export default Profile;