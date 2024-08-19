package learn.hoopAlert.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import learn.hoopAlert.domain.AppUserService;
import learn.hoopAlert.models.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import learn.hoopAlert.models.Role;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtService {

    private final SecretKey key;
    private static final String ISSUER = "hoop-alert";
    private static final int EXPIRATION_MILLIS = 1000 * 60 * 60 * 10;
    private final AppUserService appUserService;

    @Autowired
    public JwtService(AppUserService appUserService, @Value("${jwt.secret}") String secret) {
        this.appUserService = appUserService;
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Token generation
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(key)
                .compact();
    }

    public String getTokenFromUser(AppUser user) {
        String authorities = user.getAuthorities().stream()
                .map(i -> i.getAuthority())
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(user.getUsername())
                .claim("authorities", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(key)
                .compact();
    }

    // Token validation
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // Strip the "Bearer " prefix
        }
        return null;
    }

    // Token extraction
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        System.out.println("Validating Token with Key: " + Base64.getEncoder().encodeToString(key.getEncoded()));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(ISSUER)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Token parsing and user retrieval
    public AppUser getUserFromToken(String token) {

        if (token == null) {
            System.out.println("Invalid token format.");
            return null;
        }

        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .requireIssuer(ISSUER)
                    .build()
                    .parseClaimsJws(token);

            String username = jws.getBody().getSubject();
            String authStr = (String) jws.getBody().get("authorities");

//            if (username == null || authStr == null) {
//                System.out.println("Missing username or authorities in token.");
//                return null;
//            }

            Optional<AppUser> user = appUserService.findByUsername(username);

            if (user.isPresent()) {
                AppUser appUser = user.get();
                appUser.setRoles(Arrays.stream(authStr.split(","))
                        .map(role -> new Role(role))
                        .collect(Collectors.toList()));
                return appUser;
            } else {
                System.out.println("User not found: " + username);
            }
        } catch (JwtException e) {
            System.out.println("JWT Exception: " + e.getMessage());
        }
        return null;
    }
}