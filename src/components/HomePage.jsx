import React, { useEffect, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../context/UserContext';
import '../css/HomePage.css';
import logo from '../assets/logo2.png';
import gifLogo from '../assets/oie_rounded_corners.gif';

const HomePage = () => {
    const [scrolled, setScrolled] = useState(false);
    const [todayGames, setTodayGames] = useState([]);
    const [news, setNews] = useState([]);
    const { token } = useContext(UserContext);
    const navigate = useNavigate();
    
    const handleBoxClick = (route) => {
        if (route === '/profile' && !token) {
            navigate('/login');
        } else {
            navigate(route);
        }
    };

    const handleScroll = () => {
        const scrollTop = window.scrollY;
        const windowHeight = window.innerHeight;
        const docHeight = document.body.scrollHeight;

        if (scrollTop + windowHeight >= docHeight - 50) { 
            setScrolled(true);
        } else {
            setScrolled(false);
        }
    };

    useEffect(() => {
        window.addEventListener('scroll', handleScroll);
        return () => {
            window.removeEventListener('scroll', handleScroll);
        };
    }, []);

    useEffect(() => {
        const fetchTodayGames = async () => {
            try {
                const today = new Date().toISOString().split('T')[0];
                console.log("Fetching games for date:", today);  
    
                const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/schedule/date/${today}`);
    
                if (!response.ok) {
                    throw new Error('Failed to fetch today\'s games');
                }
    
                const data = await response.json();
    
                setTodayGames(data);
            } catch (err) {
                console.error(err.message);
            }
        };
        fetchTodayGames();
    }, []);

    useEffect(() => {
        const fetchNews = async () => {
            const mockNews = [
                { title: 'NBA Finals 2024: A Recap', url: 'https://www.nba.com/news/finals-2024-recap' },
                { title: 'Top 10 Moments of the 2024 NBA Season', url: 'https://www.nba.com/news/top-10-moments-2024' },
                { title: 'Draft 2024: Who’s In, Who’s Out', url: 'https://www.nba.com/news/draft-2024' },
            ];
            setNews(mockNews);
        };
        fetchNews();
    }, []);

    return (
        <div className="home-container">
            <div className={`home-background-image ${scrolled ? 'scrolled' : 'start'}`}></div>
            <div className={`content ${scrolled ? 'scrolled' : ''}`}>
                <h1 className="home-title"></h1>
                <div className="home-description-container">
                    <p  className="home-description" 
                    onClick={() => handleBoxClick('/profile')} >
                    <i className="fas fa-bell"></i>
                        Never miss a game with HoopAlert. Sign up and receive text reminders for your favorite team's games! 
                    </p>
                    <img src={gifLogo} alt='hoopAlertGif' className='small-logo'></img>
                    <p  className="home-description" 
                    onClick={() => handleBoxClick('/team-stats')} 
                    style={{ cursor: 'pointer', marginLeft: '20px' }}>
                    <i className="fas fa-basketball-ball"></i>
                        Check team stats and the 2024-25 NBA schedule.
                    </p>
                </div>

                <div className={`games-today ${scrolled ? 'scrolled' : ''}`}>
                    <h2>Today's Games</h2>
                    {!todayGames.length ? (
                        <p>No games today</p>
                    ) : (
                        todayGames.map((game, index) => {
                            const homeTeam = game.homeTeam;
                            const awayTeam = game.awayTeam;

                            if (!homeTeam || !awayTeam) {
                                console.error(`Missing team data for game:`, game);
                                return null; // Skip rendering this game
                            }

                            return (
                                <div key={index} className="game-item">
                                    <div className="game-date">
                                        <span style={{color: "black"}}>{new Date(game.gameDate).toLocaleDateString('en-US', { month: 'short', day: 'numeric' })}</span>
                                        <span style={{color: "black"}}>{new Date(game.gameDate).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
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
                                        <span style={{color: "black"}}>{game.location}</span>
                                    </div>
                                </div>
                            );
                        })
                    )}
                </div>
            </div>
            {scrolled && (
                <div className="image-caption-container">
                    <h2 className="image-caption">Anthony Edwards dunk on Robert Covington</h2>
                </div>
            )}
        </div>
    );
};

export default HomePage;
