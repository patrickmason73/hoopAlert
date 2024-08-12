package learn.hoopAlert.domain;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Service
public class DataFetchService {

    public void fetchLeagueStandings() throws IOException {
        String apiUrl = "https://api.example.com/league-standings"; // Replace with actual API URL
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (Scanner scanner = new Scanner(url.openStream())) {
            while (scanner.hasNext()) {
                System.out.println(scanner.nextLine());
            }
        }
    }
}