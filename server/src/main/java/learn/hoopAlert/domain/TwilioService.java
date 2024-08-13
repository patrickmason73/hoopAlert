package learn.hoopAlert.domain;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber; // Correct import for SMS sending
import learn.hoopAlert.TwilioConfig;
import learn.hoopAlert.models.SmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class TwilioService {

    private final TwilioConfig twilioConfig;

    @Autowired
    public TwilioService(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
    }

    public void sendSms(SmsRequest smsRequest) {
        Message.creator(
                        new PhoneNumber(smsRequest.getPhoneNumber()),  // To number
                        new PhoneNumber("18887024741"),  // From number
                        smsRequest.getMessage())                      // Message
                .create();
    }

}