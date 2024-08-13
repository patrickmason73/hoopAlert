package learn.hoopAlert.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import learn.hoopAlert.data.AppUserRepository;
import learn.hoopAlert.domain.AppUserService;
import learn.hoopAlert.models.AppUser;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtConverter {

    // 1. Signing key
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // 2. "Configurable" constants
    private final String ISSUER = "hoop-alert";
    private final int EXPIRATION_MINUTES = 15;
    private final int EXPIRATION_MILLIS = EXPIRATION_MINUTES * 60 * 1000;
    private final AppUserService appUserService;

    public JwtConverter(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    public String getTokenFromUser(AppUser user) {

        String authorities = user.getAuthorities().stream()
                .map(i -> i.getAuthority())
                .collect(Collectors.joining(","));

        // 3. Use JJWT classes to build a token.
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(user.getUsername())
                .claim("authorities", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(key)
                .compact();
    }

    public AppUser getUserFromToken(String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        try {
            // 4. Use JJWT classes to read a token.
            Jws<Claims> jws = Jwts.parserBuilder()
                    .requireIssuer(ISSUER)
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.substring(7));

            String username = jws.getBody().getSubject();
//            int appUserId = (int)jws.getBody().get("app_user_id");
            String authStr = (String) jws.getBody().get("authorities");


            AppUser user = appUserService.findByUsername(username);

            if (user != null) {
                // Ensure user has authorities set correctly
                return new AppUser(user.getAppUserId(), username, user.getPassword(), true,
                        Arrays.asList(authStr.split(",")));
            }
            
        }
        catch(JwtException e){
            System.out.println(e);
        }

        return null;
    }
}
