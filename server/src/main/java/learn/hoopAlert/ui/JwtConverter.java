//package learn.hoopAlert.ui;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import learn.hoopAlert.domain.AppUserService;
//import learn.hoopAlert.models.AppUser;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Component
//public class JwtConverter {
//
//    // 1. Signing key
//    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//    // 2. "Configurable" constants
//    private final String ISSUER = "hoop-alert";
//    private final int EXPIRATION_MINUTES = 15;
//    private final int EXPIRATION_MILLIS = EXPIRATION_MINUTES * 60 * 1000;
//    private final AppUserService appUserService;
//
//    public JwtConverter(AppUserService appUserService) {
//        this.appUserService = appUserService;
//    }
//
//    public String getTokenFromUser(AppUser user) {
//
//        String authorities = user.getAuthorities().stream()
//                .map(i -> i.getAuthority())
//                .collect(Collectors.joining(","));
//
//        // 3. Use JJWT classes to build a token.
//        return Jwts.builder()
//                .setIssuer(ISSUER)
//                .setSubject(user.getUsername())
//                .claim("authorities", authorities)
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
//                .signWith(key)
//                .compact();
//    }
//
//    public AppUser getUserFromToken(String token) {
//        System.out.println("Received Token: " + token);
//
//
//        if (token == null) {
//            System.out.println("Invalid token format.");
//            return null;
//        }
//
//
//        try {
//            Jws<Claims> jws = Jwts.parserBuilder()
//                    .setSigningKey(key)  // Ensure 'key' is set properly
//                    .requireIssuer(ISSUER)  // Ensure 'ISSUER' is set properly
//                    .build()
//                    .parseClaimsJws(token);
//
//            String username = jws.getBody().getSubject();
//            String authStr = (String) jws.getBody().get("authorities");
//
//            if (username == null || authStr == null) {
//                System.out.println("Missing username or authorities in token.");
//                return null;
//            }
//
//            Optional<AppUser> user = appUserService.findByUsername(username);
//
//            if (user.isPresent()) {
//                AppUser appUser = user.get();
//                return new AppUser(appUser.getAppUserId(), username, appUser.getPassword(), true,
//                        Arrays.asList(authStr.split(",")));
//            } else {
//                System.out.println("User not found: " + username);
//            }
//        } catch (JwtException e) {
//            System.out.println("JWT Exception: " + e.getMessage());
//        }
//        return null;
//    }
//}
