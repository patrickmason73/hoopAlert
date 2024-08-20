import React, { useContext } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { UserContext } from '../context/UserContext';
import Login from './Login';
import SignUp from './SignUp';
import HomePage from './HomePage';
import Navbar from './Navbar';
import Profile from './Profile';
import TeamSelection from './TeamSelection';
import TeamStats from './TeamStats';
import '../css/App.css';

const App = () => {
  const { token, logout } = useContext(UserContext);
  
  return (
    <Router>
         <div className="app-layout">
        <Navbar isAuthenticated={!!token} onLogout={logout} />
        <div className="main-content">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/profile" element={token ? <Profile /> : <Login />} />
            <Route path="/team-selection" element={token ? <TeamSelection /> : <Login />} />
            <Route path="/login" element={<Login />} />
            <Route path="/team-stats" element={<TeamStats />} />
          </Routes>
        </div>
      </div>
    </Router>
    );
  };

export default App;

