import React, { useState, useEffect } from 'react';
import '../css/TeamStats.css';

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
            leagueId,
            seasonId,
            teamId,
            teamCity,
            teamName,
            conference,
            conferenceRecord,
            playoffRank,
            clinchIndicator,
            division,
            divisionRecord,
            divisionRank,
            wins,
            losses,
            winPercentage,
            leagueRank,
            record,
            homeRecord,
            roadRecord,
            l10,
            last10Home,
            last10Road,
            ot,
            threePtsOrLess,
            tenPtsOrMore,
            longHomeStreak,
            strLongHomeStreak,
            longRoadStreak,
            strLongRoadStreak,
            longWinStreak,
            longLossStreak,
            currentHomeStreak,
            strCurrentHomeStreak,
            currentRoadStreak,
            strCurrentRoadStreak,
            currentStreak,
            strCurrentStreak,
            conferenceGamesBack,
            divisionGamesBack,
            clinchedConferenceTitle,
            clinchedDivisionTitle,
            clinchedPlayoffBirth,
            eliminatedConference,
            eliminatedDivision,
            aheadAtHalf,
            behindAtHalf,
            tiedAtHalf,
            aheadAtThird,
            behindAtThird,
            tiedAtThird,
            score100Pts,
            oppScore100Pts,
            oppOver500,
            leadInFgPct,
            leadInReb,
            fewerTurnovers,
            pointsPg,
            oppPointsPg,
            diffPointsPg,
            vsEast,
            vsAtlantic,
            vsCentral,
            vsSoutheast,
            vsWest,
            vsNorthwest,
            vsPacific,
            vsSouthwest,
            jan,
            feb,
            mar,
            apr,
            may,
            jun,
            jul,
            aug,
            sep,
            oct,
            nov,
            dec,
            preAs,
            postAs
        ] = data[0];

        return {
            seasonId,
            teamId,
            teamCity,
            teamName,
            conference,
            conferenceRank: playoffRank,
            division,
            divisionRank,
            wins,
            losses,
            winPercentage,
            homeRecord,
            roadRecord,
            streak: currentStreak,
            pointsPerGame: pointsPg,
            opponentPointsPerGame: oppPointsPg,
            pointDifferential: diffPointsPg,
        };
    };


    return (
        <div className="team-stats-container">
            <h2 className="team-stats-title">Team Stats</h2>
            <div className="team-stats-selection">
                <label>
                    Select Team:
                    <select value={selectedTeam} onChange={handleTeamChange} className="team-stats-select">
                        <option value="">-- Select a Team --</option>
                        {teams.map(team => (
                            <option key={team.id} value={team.nbaTeamId}>
                                {team.teamName}
                            </option>
                        ))}
                    </select>
                </label>
            </div>
            <div className="team-stats-selection">
                <label>
                    Select Season:
                    <select value={season} onChange={handleSeasonChange} className="team-stats-select">
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
            <button onClick={fetchStats} disabled={loading} className="fetch-stats-button">
                {loading ? 'Fetching Stats...' : 'Fetch Stats'}
            </button>
            {error && <p className="error-message">{error}</p>}
            <div className="stats-display">
                {stats ? (
                    <div className="stats-data">
                        <h3>{stats.teamName} ({stats.teamCity})</h3>
                        <p>Conference: {stats.conference} ({stats.conferenceRank})</p>
                        <p>Division: {stats.division} ({stats.divisionRank})</p>
                        <p>Record: {stats.wins}-{stats.losses} ({(stats.winPercentage * 100).toFixed(2)}%)</p>
                        <p>Home Record: {stats.homeRecord}</p>
                        <p>Road Record: {stats.roadRecord}</p>
                        <p>Streak: {stats.streak}</p>
                        <p>Points Per Game: {stats.pointsPerGame}</p>
                        <p>Opponent Points Per Game: {stats.opponentPointsPerGame}</p>
                        <p>Point Differential: {stats.pointDifferential}</p>
                    </div>
                ) : (
                    !loading && <p className="no-stats-message">Please select a team and season to view stats.</p>
                )}
            </div>
        </div>
    );
};

export default TeamStats;
