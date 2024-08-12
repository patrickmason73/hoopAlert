import pandas as pd
import mysql.connector

def insert_data(df, db_config):
    # Establish a database connection
    conn = mysql.connector.connect(**db_config)
    cursor = conn.cursor()

    # Define your table schema here
    table_name = "team_stats"
    columns = ", ".join(df.columns)
    placeholders = ", ".join(["%s"] * len(df.columns))
    sql = f"INSERT INTO {table_name} ({columns}) VALUES ({placeholders})"

    # Insert data into the table
    for row in df.itertuples(index=False):
        cursor.execute(sql, row)

    # Commit and close the connection
    conn.commit()
    cursor.close()
    conn.close()

def main():
    # Example database configuration
    db_config = {
        'host': 'localhost',
        'user': 'root',
        'password': 'Ericpat1!',
        'database': 'hoop_alert'
    }
    
    from fetch_data import fetch_nba_data  # Import the function from your fetch_data.py
    season_year = "2023-24"  # Replace with the desired season year
    df = fetch_nba_data(season_year)

    # Insert the DataFrame into the database
    insert_data(df, db_config)

if __name__ == "__main__":
    main()