package learn.hoopAlert.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtConverter converter;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtConverter converter, JwtRequestFilter jwtRequestFilter) {
        this.converter = converter;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authConfig) throws Exception {
        // we're not using HTML forms in our app
        //so disable CSRF (Cross Site Request Forgery)
        http.csrf().disable();

        // this configures Spring Security to allow
        //CORS related requests (such as preflight checks)
        http.cors();

        // the order of the antMatchers() method calls is important
        // as they're evaluated in the order that they're added
        http.authorizeRequests()
                // new...
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/refresh_token").authenticated()
                .antMatchers("/create_account").permitAll()
                .antMatchers(HttpMethod.POST, "/api/sms/send").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/order").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/sighting", "/sighting/*").permitAll()
                .antMatchers(HttpMethod.POST,
                        "/sighting").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT,
                        "/sighting/*").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE,
                        "/sighting/*").hasAnyAuthority("ADMIN")

                // if we get to this point, let's deny all requests
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
