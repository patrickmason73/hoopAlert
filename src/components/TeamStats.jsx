import React, { useState, useEffect } from 'react';

const TeamStats = () => {
    const [teams, setTeams] = useState([]);
    const [selectedTeam, setSelectedTeam] = useState('');
    const [season, setSeason] = useState('');
    const [stats, setStats] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        // Fetch the list of teams from your backend
        fetch(`${import.meta.env.VITE_API_BASE_URL}/api/teams`)
            .then(response => response.json())
            .then(data => {
                setTeams(data);
            })
            .catch(error => {
                console.error('There was an error fetching the teams!', error);
            });
    }, []);

    const handleTeamChange = (event) => {
        setSelectedTeam(event.target.value);
    };

    const handleSeasonChange = (event) => {
        setSeason(event.target.value);
    };

    const fetchStats = () => {
        if (!selectedTeam || !season) {
            setError('Please select both a team and a season');
            return;
        }

        const selectedTeamData = teams.find(team => team.nbaTeamId === selectedTeam);
        const firstSeason = parseInt(selectedTeamData.firstSeason, 10);
        const selectedSeasonYear = parseInt(season.split('-')[0], 10);

        if (selectedSeasonYear < firstSeason) {
            setError(`The ${selectedTeamData.teamName} did not exist in the ${season} season.`);
            setStats(null);
            return;
        }

        setLoading(true);
        setError(null);
        setStats(null);

        // Fetch the stats for the selected team and season from your Flask API
        fetch(`http://127.0.0.1:5000/team-standings?teamId=${selectedTeam}&season=${season}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch');
                }
                return response.json();
            })
            .then(data => {
                setStats(mapStandingData(data));
            })
            .catch(error => {
                console.error('There was an error fetching the stats!', error);
                setError('There was an error fetching the stats!');
            })
            .finally(() => {
                setLoading(false);
            });
    };

    const mapStandingData = (data) => {
        if (data.length === 0) return null;

        const [
            ,
            seasonId,
            teamId,
            city,
            teamName,
            conference,
            conferenceRecord,
            conferenceRank,
            ,
            division,
            divisionRecord,
            divisionRank,
            wins,
            losses,
            winPercentage,
            ,
            ,
            homeRecord,
            roadRecord,
            streak,
            pointsPerGame,
            opponentPointsPerGame,
            pointDifferential
        ] = data[0];

        return {
            seasonId,
            teamId,
            city,
            teamName,
            conference,
            conferenceRecord,
            conferenceRank,
            division,
            divisionRecord,
            divisionRank,
            wins,
            losses,
            winPercentage,
            homeRecord,
            roadRecord,
            streak,
            pointsPerGame,
            opponentPointsPerGame,
            pointDifferential
        };
    };


    return (
        <div>
            <h2>Team Stats</h2>
            <div>
                <label>
                    Select Team:
                    <select value={selectedTeam} onChange={handleTeamChange}>
                        <option value="">-- Select a Team --</option>
                        {teams.map(team => (
                            <option key={team.id} value={team.nbaTeamId}>
                                {team.teamName}
                            </option>
                        ))}
                    </select>
                </label>
            </div>
            <div>
                <label>
                    Select Season:
                    <select value={season} onChange={handleSeasonChange}>
                        <option value="">-- Select a Season --</option>
                        {Array.from({ length: 75 }, (_, index) => {
                            const year = 2023 - index;
                            return (
                                <option key={year} value={`${year}-${(year + 1).toString().slice(2)}`}>
                                    {`${year}-${(year + 1).toString().slice(2)}`}
                                </option>
                            );
                        })}
                    </select>
                </label>
            </div>
            <button onClick={fetchStats} disabled={loading}>
                {loading ? 'Fetching Stats...' : 'Fetch Stats'}
            </button>
            {error && <p>{error}</p>}
            <div>
                {stats ? (
                    <div>
                        <h3>{stats.teamName} ({stats.city})</h3>
                        <p>Conference: {stats.conference} ({stats.conferenceRank})</p>
                        <p>Division: {stats.division} ({stats.divisionRank})</p>
                        <p>Record: {stats.wins}-{stats.losses} ({stats.winPercentage}%)</p>
                        <p>Home Record: {stats.homeRecord}</p>
                        <p>Road Record: {stats.roadRecord}</p>
                        <p>Streak: {stats.streak}</p>
                        <p>Points Per Game: {stats.pointsPerGame}</p>
                        <p>Opponent Points Per Game: {stats.opponentPointsPerGame}</p>
                        <p>Point Differential: {stats.pointDifferential}</p>
                    </div>
                ) : (
                    !loading && <p>Please select a team and season to view stats.</p>
                )}
            </div>
        </div>
    );
};

export default TeamStats;