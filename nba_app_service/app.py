from flask import Flask, jsonify, request
from flask_cors import CORS
from nba_api.stats.library.http import NBAStatsHTTP
from nba_api.stats.endpoints._base import Endpoint
import logging
import time

app = Flask(__name__)
CORS(app)

# Set up logging to display INFO level logs
logging.basicConfig(level=logging.INFO)

# Define a custom class to handle the LeagueStandings endpoint
class CustomLeagueStandings(Endpoint):
    endpoint = "leaguestandings"
    expected_data = {
        "Standings": [
            "LeagueID", "SeasonID", "TeamID", "TeamCity", "TeamName", "Conference",
            "ConferenceRecord", "PlayoffRank", "ClinchIndicator", "Division", "DivisionRecord",
            "DivisionRank", "WINS", "LOSSES", "WinPCT", "LeagueRank", "Record", "HOME", "ROAD",
            "L10", "Last10Home", "Last10Road", "OT", "ThreePTSOrLess", "TenPTSOrMore",
            "LongHomeStreak", "strLongHomeStreak", "LongRoadStreak", "strLongRoadStreak",
            "LongWinStreak", "LongLossStreak", "CurrentHomeStreak", "strCurrentHomeStreak",
            "CurrentRoadStreak", "strCurrentRoadStreak", "CurrentStreak", "strCurrentStreak",
            "ConferenceGamesBack", "DivisionGamesBack", "ClinchedConferenceTitle",
            "ClinchedDivisionTitle", "ClinchedPlayoffBirth", "EliminatedConference",
            "EliminatedDivision", "AheadAtHalf", "BehindAtHalf", "TiedAtHalf", "AheadAtThird",
            "BehindAtThird", "TiedAtThird", "Score100PTS", "OppScore100PTS", "OppOver500",
            "LeadInFGPCT", "LeadInReb", "FewerTurnovers", "PointsPG", "OppPointsPG", "DiffPointsPG",
            "vsEast", "vsAtlantic", "vsCentral", "vsSoutheast", "vsWest", "vsNorthwest",
            "vsPacific", "vsSouthwest", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec", "PreAS", "PostAS"
        ]
    }

    def __init__(self, league_id="00", season="2021-22", season_type="Regular Season", proxy=None, headers=None, timeout=30):
        self.proxy = proxy
        self.headers = headers
        self.timeout = timeout
        self.parameters = {
            "LeagueID": league_id,
            "Season": season,
            "SeasonType": season_type,
        }
        self.get_request()

    def get_request(self):
        self.nba_response = NBAStatsHTTP().send_api_request(
            endpoint=self.endpoint,
            parameters=self.parameters,
            proxy=self.proxy,
            headers=self.headers,
            timeout=self.timeout,
        )
        self.load_response()

    def load_response(self):
        data_sets = self.nba_response.get_data_sets()
        self.data_sets = [
            Endpoint.DataSet(data=data_set)
            for data_set_name, data_set in data_sets.items()
        ]
        self.standings = Endpoint.DataSet(data=data_sets["Standings"])

    def get_dict(self):
        return self.nba_response.get_dict()

# Define the Flask route to use the CustomLeagueStandings class
@app.route('/team-standings', methods=['GET'])
def get_team_standings():
    season = request.args.get('season')
    team_id = request.args.get('teamId')

    try:
        start_time = time.time()  # Start timer
        standings = CustomLeagueStandings(season=season)  # API request
        api_time = time.time() - start_time  # Time for API request
        logging.info(f"API request time: {api_time:.2f} seconds")

        standings_data = standings.get_dict()
        process_time = time.time() - start_time  # Total time including processing
        logging.info(f"Total processing time: {process_time:.2f} seconds")

        if team_id:
            filtered_data = [
                team for team in standings_data['resultSets'][0]['rowSet'] 
                if team[2] == int(team_id)
            ]
        else:
            filtered_data = standings_data['resultSets'][0]['rowSet']

        return jsonify(filtered_data)
    except Exception as e:
        logging.error(f"Error fetching standings: {str(e)}")
        return jsonify({"error": "Failed to fetch data from nba_api"}), 500


if __name__ == '__main__':
    app.run(debug=True)