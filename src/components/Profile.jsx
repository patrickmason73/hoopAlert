import React, { useEffect, useState, useContext } from 'react';
import { UserContext } from '../context/UserContext';
import TeamSelection from './TeamSelection';

const Profile = () => {
  const { user } = useContext(UserContext);
  const [userTeams, setUserTeams] = useState([]);

  useEffect(() => {
    if (!user) {
      // If no user is logged in, do not fetch teams and display a message instead
      return;
    }

    // Fetch the teams associated with the logged-in user
    const fetchUserTeams = async () => {
      try {
        const response = await fetch(`http//:localhost:8080/api/users/${user.id}/teams`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            // Authorization: `Bearer ${user.token}`, // Assuming token is used for authorization
          },
        });
        if (!response.ok) {
          throw new Error('Failed to fetch user teams');
        }
        const data = await response.json();
        setUserTeams(data);
      } catch (error) {
        console.error('Error fetching user teams:', error);
      }
    };

    fetchUserTeams();
  }, [user]);

  if (!user) {
    return <p>Please log in to view your profile.</p>;
  }

  return (
    <div className="profile">
      <h1>Welcome, {user.username}!</h1>
      <p>Email: {user.email}</p>
      <p>Phone Number: {user.phoneNumber}</p>

      {/* Team selection component */}
      <TeamSelection userTeams={userTeams} setUserTeams={setUserTeams}/>
    </div>
  );
};

export default Profile;