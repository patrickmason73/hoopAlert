package learn.hoopAlert.controllers;

import learn.hoopAlert.domain.DataFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private DataFetchService dataFetchService;

    @GetMapping("/fetch-league-standings")
    public ResponseEntity<String> fetchLeagueStandings() {
        try {
            dataFetchService.fetchLeagueStandings();
            return ResponseEntity.ok("League standings data fetched successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch data.");
        }
    }

    // Other endpoints for fetching data
}