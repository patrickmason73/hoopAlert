package learn.hoopAlert.domain;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import learn.hoopAlert.TwilioConfig;
import learn.hoopAlert.models.SmsRequest;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    private final TwilioConfig twilioConfig;

    public TwilioService(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
        // Initialize Twilio with the configuration
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
    }

    public void sendSms(SmsRequest smsRequest) {
        Message.creator(
                new com.twilio.type.PhoneNumber(smsRequest.getPhoneNumber()), // To number
                new com.twilio.type.PhoneNumber(twilioConfig.getPhoneNumber()), // From number
                smsRequest.getMessage() // Message body
        ).create();
    }
}