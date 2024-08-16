import React, { createContext, useState, useContext } from 'react';

// Create a Context for the user
export const UserContext = createContext();

// Create a custom hook to use the UserContext
export const useUser = () => useContext(UserContext);

// Create a Provider component
export const UserProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  const login = (userData) => {
    setUser(userData);
    localStorage.setItem('jwt', userData.token); // Store token in localStorage
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('jwt');
  };

  return (
    <UserContext.Provider value={{ user, login, logout }}>
      {children}
    </UserContext.Provider>
  );
};