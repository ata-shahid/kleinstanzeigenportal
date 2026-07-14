package de.hsrm.mi.web.projekt.configuration;

import static org.springframework.boot.security.autoconfigure.web.servlet.PathRequest.toH2Console;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(toH2Console()).permitAll()
                .requestMatchers("/admin/benutzer/**").hasRole("ADMINISTRATOR")
                .requestMatchers("/admin/**").authenticated()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/stompbroker/**").permitAll()
                .requestMatchers("/styles.css", "/static/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .defaultSuccessUrl("/admin/anzeige", true)
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(toH2Console())
                .ignoringRequestMatchers("/api/**")
                .ignoringRequestMatchers("/admin/benutzer/*/hx/feld/*")
            )
            .headers(hdrs -> hdrs.frameOptions(fo -> fo.sameOrigin()));

        return http.build();
    }
}
