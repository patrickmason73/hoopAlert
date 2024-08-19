package learn.hoopAlert.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/proxy")
public class ProxyController {

    @GetMapping("/team-stats")
    public ResponseEntity<?> getTeamStats(@RequestParam String teamId, @RequestParam String season) {
        String url = "https://www.balldontlie.io/api/v1/stats?team_ids[]=" + teamId + "&seasons[]=" + season;
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            System.out.println("API Response: " + response.getBody()); // Logging the response for debugging
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
            return ResponseEntity.status(500).body("Failed to fetch team stats: " + e.getMessage());
        }
    }
}
