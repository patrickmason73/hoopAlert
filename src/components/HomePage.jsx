import React, { useEffect, useState } from 'react';
import '../css/HomePage.css';

const HomePage = () => {
    const [scrolled, setScrolled] = useState(false);

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

    return (
        <div className="home-container">
            <div className={`home-background-image ${scrolled ? 'scrolled' : 'start'}`}></div>
            <div className="content">
                <h1 className="home-title">Welcome to HoopAlert</h1>
                <p className="home-description">
                    Stay ahead of the game with real-time updates on your favorite NBA teams and players.
                </p>
            </div>
            {scrolled && (
                <div className="image-caption-container">
                    <h2 className="image-caption">Anthony Edwards one hand jam</h2>
                </div>
            )}
        </div>
    );
};

export default HomePage;
