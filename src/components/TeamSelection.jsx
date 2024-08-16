import React, { useEffect, useState } from 'react';
import { useUser } from '../context/UserContext'; // Adjust import path as needed

const TeamSelection = ({ userTeams, setUserTeams }) => {
  const { user } = useUser();
  const [teams, setTeams] = useState([]);

  useEffect(() => {
    // Fetch all teams to display
    const fetchTeams = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/teams`, {
          method: 'GET',
          headers: {
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
  }, []);

  const handleCheckboxChange = async (teamId, isChecked) => {
    try {
      const response = await fetch(`http://localhost:8080/api/users/${user.id}/teams/${teamId}`, {
        method: isChecked ? 'POST' : 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${user.token}`, // Assuming token is used for authorization
        },
      });
      if (!response.ok) {
        throw new Error('Failed to update team selection');
      }
      // Update userTeams state based on checkbox status
      if (isChecked) {
        setUserTeams((prevTeams) => [...prevTeams, teamId]);
      } else {
        setUserTeams((prevTeams) => prevTeams.filter((id) => id !== teamId));
      }
    } catch (error) {
      console.error('Error updating team selection:', error);
    }
  };

  return (
    <div className="team-selection">
      <h2>Select Teams for Reminders</h2>
      {teams.map((team) => (
        <div key={team.id} className="team-checkbox">
          <input
            type="checkbox"
            id={`team-${team.id}`}
            checked={userTeams.includes(team.id)}
            onChange={(e) => handleCheckboxChange(team.id, e.target.checked)}
          />
          <label htmlFor={`team-${team.id}`}>
            {team.teamName} ({team.teamAbbreviation})
          </label>
        </div>
      ))}
    </div>
  );
};

export default TeamSelection;