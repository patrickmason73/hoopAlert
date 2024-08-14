import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import Login from "./Login";
import SignUp from "./Signup";
import HomePage from "./HomePage";
import Navbar from './Navbar';


const App = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem("jwt"));

  const handleLogin = () => setIsAuthenticated(true);
  const handleLogout = () => {
      localStorage.removeItem("jwt");
      setIsAuthenticated(false);
  };

    return (
        <Router>
            <Navbar isAuthenticated={isAuthenticated} onLogout={handleLogout} />
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/login" element={<Login onLogin={handleLogin} />} />
                <Route path="/signup" element={<SignUp />} />
            </Routes>
        </Router>
    );
};

export default App;
