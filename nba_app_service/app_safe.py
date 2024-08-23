from flask import Flask, jsonify, request
from flask_cors import CORS
from nba_api.stats.endpoints import leaguestandings
import logging


app = Flask(__name__)
CORS(app)



@app.route('/team-standings', methods=['GET'])
def get_team_standings():
    season = request.args.get('season')
    team_id = request.args.get('teamId')
    
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
    

if __name__ == '__main__':
    app.run(debug=True)