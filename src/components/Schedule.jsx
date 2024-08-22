import React, { useState, useEffect, useContext } from 'react';
import { UserContext } from '../context/UserContext';
import '../css/Schedule.css';

const Schedule = () => {
  const { token } = useContext(UserContext);
  const [games, setGames] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchAllGames = async () => {
      try {
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/schedule/all`, {
          method: 'GET',
          headers: {
            // 'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });

        if (!response.ok) {
          throw new Error('Failed to fetch games');
        }

        const data = await response.json();
        setGames(data);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchAllGames();
  }, [token]);

  if (error) return <p className="error-message">{error}</p>;
  if (!games.length) return <p>Loading...</p>;

  return (
    <div className="schedule-container">
        <h1>2024-25 Schedule</h1>
      {games.map((game, index) => {
        const homeTeam = game.homeTeam;
        const awayTeam = game.awayTeam;

        // Log team IDs if either team is undefined
        if (!homeTeam || !awayTeam) {
          console.error(`Missing team data for game:`, game);
          return null; // Skip rendering this game
        }

        return (
          <div key={index} className="game-item">
            <div className="game-date">
              <span>{new Date(game.gameDate).toLocaleDateString('en-US', { month: 'short', day: 'numeric' })}</span>
              <span>{new Date(game.gameDate).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
            </div>
            <div className="teams-container"> {/* New container */}
        <div className="teams">
            <div className="team">
                <img src={homeTeam.teamLogoUrl} alt={homeTeam.teamName} />
                <span>{homeTeam.teamName}</span>
            </div>
            <div className="vs">vs</div>
            <div className="team">
                <img src={awayTeam.teamLogoUrl} alt={awayTeam.teamName} />
                <span>{awayTeam.teamName}</span>
            </div>
        </div>
    </div>
    <div className="location">
        <span>{game.location}</span>
    </div>
          </div>
        );
      })}
    </div>
  );
};

export default Schedule;
