package learn.hoopAlert.domain;

import com.twilio.rest.chat.v1.service.User;
import learn.hoopAlert.data.GameRepository;
import learn.hoopAlert.data.AppUserRepository;
import learn.hoopAlert.models.Game;
import learn.hoopAlert.models.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReminderService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private TwilioService twilioService;

//    public void sendDailyReminders() {
//        LocalDate today = LocalDate.now();
//        List<Game> todaysGames = gameRepository.findByGameDate(today);
//
//        for (Game game : todaysGames) {
//            List<AppUser> users = appUserRepository.findByTeam(game.getTeam());
//
//            for (AppUser user : users) {
//                String message = "Reminder: Your team " + game.getTeam() + " is playing today!";
//                twilioService.sendSms(user.getPhoneNumber(), message);
//            }
//        }
//    }
}
