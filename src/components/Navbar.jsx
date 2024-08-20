import React from 'react';
import { Link } from 'react-router-dom';
import '../css/Navbar.css'; 

const Navbar = ({ isAuthenticated, onLogout }) => {
  return (
    <nav className="sidebar">
      <div className="navbar-logo">
        <Link to="/" className="navbar-brand">HoopAlert</Link>
        <div className="navbar-logo-animation"></div>
      </div>
      <ul className="navbar-links">
        <li><Link to="/">Home</Link></li>
        <li><Link to="/team-stats">Team Stats</Link></li>

        {isAuthenticated ? (
          <>
            <li><Link to="/profile">Profile</Link></li>
            <li><button onClick={onLogout} className="navbar-logout">Logout</button></li>
          </>
        ) : (
          <>
            <li><Link to="/login">Login</Link></li>
            <li><Link to="/signup">Sign Up</Link></li>
          </>
        )}
      </ul>
    </nav>
  );
};

export default Navbar;