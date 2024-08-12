from nba_api.stats.endpoints import leaguestandings
import pandas as pd

def fetch_nba_data(season_year):
    # Fetch league standings for the given season
    standings = leaguestandings.LeagueStandings(season=season_year)
    
    # Convert the fetched data to a DataFrame
    df = standings.get_data_frames()[0]
    
    # Print out the DataFrame to inspect
    print(df.head())
    return df

def main():
    season_year = "2023-24"  # Replace with the desired season year
    df = fetch_nba_data(season_year)
    
    # Additional code to process the DataFrame or insert it into the database
    # ...

if __name__ == "__main__":
    main()