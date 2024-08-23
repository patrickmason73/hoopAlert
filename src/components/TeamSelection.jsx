import React, { useEffect, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../context/UserContext';
import '../css/TeamSelection.css'; // Add this line to import the new CSS file

const TeamSelection = () => {
  const { token } = useContext(UserContext);
  const [teams, setTeams] = useState([]);
  const [userTeams, setUserTeams] = useState([]);
  const [userId, setUserId] = useState(null); // Store userId from profile
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch all teams
    const fetchTeams = async () => {
      try {
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/teams`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });

        if (!response.ok) {
          throw new Error('Failed to fetch teams');
        }

        const data = await response.json();
        setTeams(data);
      } catch (error) {
        console.error('Error fetching teams:', error);
      }
    };

    fetchTeams();

    // Fetch the user's current selected teams and their userId
    const fetchUserTeams = async () => {
      try {
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/users/profile`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });

        if (!response.ok) {
          throw new Error('Failed to fetch user teams');
        }

        const userData = await response.json();
        setUserId(userData.id); // Set userId from the profile data
        setUserTeams(userData.teams.map(team => team.id)); // Assume teams are returned with `id`
      } catch (error) {
        console.error('Error fetching user teams:', error);
      }
    };

    fetchUserTeams();

  }, [token]);

  const handleCheckboxChange = async (teamId, isChecked) => {
    if (!userId) {
      console.error('User data not loaded yet');
      return;
  }
  
    try {
      const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/users/${userId}/teams/${teamId}`, {
        method: isChecked ? 'POST' : 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error('Failed to update team selection');
      }

      if (isChecked) {
        setUserTeams(prevTeams => [...prevTeams, teamId]);
      } else {
        setUserTeams(prevTeams => prevTeams.filter(id => id !== teamId));
      }
    } catch (error) {
      console.error('Error updating team selection:', error);
    }
  };

  return (
    <div className="team-selection">
    <h2>Select Teams for Reminders</h2>
    <div className="team-checkboxes">
      {teams.map((team) => (
        <div 
          key={team.id} 
          className="team-checkbox" 
          onClick={() => handleCheckboxChange(team.id, !userTeams.includes(team.id))}
        >
          <input
            type="checkbox"
            id={`team-${team.id}`}
            checked={userTeams.includes(team.id)}
            onChange={(e) => handleCheckboxChange(team.id, e.target.checked)}
            onClick={(e) => e.stopPropagation()} // Prevents the click from bubbling up to the div
          />
          <label htmlFor={`team-${team.id}`}>
            {team.teamName} ({team.teamAbbreviation})
          </label>
        </div>
      ))}
    </div>
    <button className="back-to-profile-button" onClick={() => navigate('/profile')}>
      Back to Profile
    </button>
  </div>
);
};

export default TeamSelection;