import React, { useState, useEffect, useContext } from 'react';
import { UserContext } from '../context/UserContext';
import '../css/Schedule.css';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

const Schedule = () => {
  const { token } = useContext(UserContext);
  const [games, setGames] = useState([]);
  const [error, setError] = useState(null);

  const [filteredGames, setFilteredGames] = useState([]);
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTeam, setSelectedTeam] = useState('');

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
        setFilteredGames(data);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchAllGames();
  }, [token]);

  useEffect(() => {
    filterGames();
  }, [selectedDate, selectedTeam]);

  const filterGames = () => {
    let filtered = games;
  
    if (selectedDate) {
      filtered = filtered.filter(game => {
        const gameDate = new Date(game.gameDate);
        return (
          gameDate.getFullYear() === selectedDate.getFullYear() &&
          gameDate.getMonth() === selectedDate.getMonth() &&
          gameDate.getDate() === selectedDate.getDate()
        );
      });
    }
  
    if (selectedTeam) {
      filtered = filtered.filter(game =>
        game.homeTeam.teamName.toLowerCase().includes(selectedTeam.toLowerCase()) || 
        game.awayTeam.teamName.toLowerCase().includes(selectedTeam.toLowerCase())
      );
    }
  
    setFilteredGames(filtered);
  };

  if (error) return <p className="error-message">{error}</p>;
  if (!games.length) return <p>Loading...</p>;

  return (
    <div className="schedule-container">
      <h1>- 2024-25 Schedule -</h1>

      {/* Filter Section */}
      <div className="filter-section">
        <label>
          Date:
          <DatePicker
            selected={selectedDate}
            onChange={(date) => setSelectedDate(date)}
            placeholderText="Select a date"
            dateFormat="MMMM d, yyyy"
            isClearable
          />
        </label>
        <label>
          Team:
          <input
            type="text"
            value={selectedTeam}
            onChange={(e) => setSelectedTeam(e.target.value)}
            placeholder="Enter team name"
          />
        </label>
      </div>

      {/* Display Filtered Games */}
      {filteredGames.map((game, index) => {
        const homeTeam = game.homeTeam;
        const awayTeam = game.awayTeam;

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
            <div className="teams-container">
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
