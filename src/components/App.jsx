import React, { useContext, useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { UserContext } from "../context/UserContext";
import Login from "./Login";
import SignUp from "./Signup";
import HomePage from "./HomePage";
import Navbar from './Navbar';
import Profile from "./Profile";
import TeamSelection from "./TeamSelection";

const App = () => {
    const { user, logout } = useContext(UserContext);
  
    return (
      <Router>
        <Navbar isAuthenticated={!!user} onLogout={logout} />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/profile" element={user ? <Profile /> : <Login />} />
          <Route path="/login" element={<Login />} />
        </Routes>
      </Router>
    );
  };

export default App;

