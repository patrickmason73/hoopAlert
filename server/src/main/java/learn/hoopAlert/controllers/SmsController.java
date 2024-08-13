package learn.hoopAlert.controllers;

import learn.hoopAlert.models.SmsRequest;
import learn.hoopAlert.domain.TwilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final TwilioService twilioService;

    @Autowired
    public SmsController(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendSms(@RequestBody SmsRequest smsRequest) {
        try {
            twilioService.sendSms(smsRequest);
            return new ResponseEntity<>("SMS sent successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to send SMS", HttpStatus.FORBIDDEN);
        }
    }
}
