import React, { useContext } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { UserContext } from '../context/UserContext';
import Login from './Login';
import SignUp from './Signup';
import HomePage from './HomePage';
import Navbar from './Navbar';
import Profile from './Profile';
import TeamSelection from './TeamSelection';

const App = () => {
  const { token, logout } = useContext(UserContext);
  console.log('User token in App:', token); // Debugging line
  
  return (
    <Router>
      <Navbar isAuthenticated={!!token} onLogout={logout} />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/profile" element={token ? <Profile /> : <Login />} />
        <Route path="/team-selection" element={token ? <TeamSelection /> : <Login />} />
        <Route path="/login" element={<Login />} />
      </Routes>
    </Router>
    );
  };

export default App;

