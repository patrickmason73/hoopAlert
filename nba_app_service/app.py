from flask import Flask, jsonify, request
from flask_cors import CORS
from nba_api.stats.endpoints import leaguestandings, leaguegamefinder
import logging
from datetime import datetime
import re  # Add this import for regex
import requests


logging.basicConfig(level=logging.DEBUG)

app = Flask(__name__)
CORS(app)

# def parse_team_data(raw_game_data, nba_team_ids):
#     parsed_data = []
#     for game in raw_game_data:
#         team_id = game[1]  # Numeric team ID
#         opponent_team_id = game[3]  # Numeric opponent ID
        
#         # Filter out games involving non-NBA teams
#         if team_id not in nba_team_ids or opponent_team_id not in nba_team_ids:
#             continue
        
#         game_time = game[8]  # Correct reference to the game time
        
#         parsed_data.append({
#             'game_id': game[4],  # Assuming this is the correct game ID
#             'team_id': team_id,  # Correct team ID
#             'opponent_team_id': opponent_team_id,  # Correct opponent ID
#             'game_date': game[5],  # Correct game date
#             'matchup': game[6],  # Correct matchup info
#             'game_time': game_time,  # Correct game time
#             'home_away': "home" if game[9] == "H" else "away"  # Correct home/away
#         })
    
#     return parsed_data



@app.route('/team-standings', methods=['GET'])
def get_team_standings():
    season = request.args.get('season')
    team_id = request.args.get('teamId')

    logging.debug(f"Season: {season}, Team ID: {team_id}")

    try:
        standings = leaguestandings.LeagueStandings(season=season)
        standings_data = standings.get_dict()

        if team_id:
            filtered_data = [team for team in standings_data['resultSets'][0]['rowSet'] if team[2] == int(team_id)]
        else:
            filtered_data = standings_data['resultSets'][0]['rowSet']

        return jsonify(filtered_data)
    except Exception as e:
        logging.error(f"Error fetching standings: {str(e)}")
        return jsonify({"error": "Failed to fetch data from nba_api"}), 500
    
# @app.route('/nba/schedule', methods=['GET'])
# def get_nba_schedule():
#     season = '2024-25'
#     gamefinder = leaguegamefinder.LeagueGameFinder(season_nullable=season)
#     games = gamefinder.get_data_frames()[0]
    
#     # Print unique SEASON_ID values
#     print(games['SEASON_ID'].unique())

#     # Return the entire dataset for now
#     schedule_data = games.to_dict(orient='records')
    
#     return jsonify(schedule_data)

if __name__ == '__main__':
    app.run(debug=True)