package learn.hoopAlert.Security;

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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtConverter converter;


    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
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
                .antMatchers(HttpMethod.POST, "/api/users/create").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/user/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/sms/send").permitAll()
                .antMatchers(HttpMethod.GET, "api/teams/*").permitAll()
                .antMatchers(HttpMethod.POST, "api/users/*").permitAll()
                .antMatchers(HttpMethod.DELETE, "api/users/*").permitAll()
                .antMatchers(HttpMethod.PUT, "api/users/*").permitAll()
                .antMatchers(HttpMethod.POST, "api/reminders").permitAll()
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/refresh_token").authenticated()
                .antMatchers("/create_account").permitAll()

                // if we get to this point, let's deny all requests
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
