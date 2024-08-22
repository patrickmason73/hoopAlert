package learn.hoopAlert.Security;

import learn.hoopAlert.Security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService converter;

    public SecurityConfig(JwtService converter) {
        this.converter = converter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authConfig) throws Exception {
        http.csrf().disable();

        http.cors();

        http.authorizeRequests()
                .antMatchers("/api/users/create",
                        "/api/users/user/**",
                        "/api/teams",
                        "/api/teams/*",
                        "/api/sms/send",
                        "/authenticate",
                        "/create_account",
                        "/api/proxy/**",
                        "/api/schedule/date/**",
                        "/api/schedule/all").permitAll()


                // Public or test-specific endpoints
                .antMatchers(HttpMethod.POST, "/api/reminders/trigger/{date}").permitAll()

                .antMatchers("/api/reminders/trigger/today").authenticated()
                .antMatchers("/api/users/profile").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/users/{userId}").authenticated()  // Allow PUT for user profile updates
                .antMatchers("/api/users/{userId}/teams/**",
                        "/api/reminders",
                        "/api/schedules/**",
                        "/refresh_token").authenticated()
                .antMatchers("/**").denyAll()
                .and()
                .addFilter(new JwtRequestFilter(authenticationManager(authConfig), converter))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    @Profile("test")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
}