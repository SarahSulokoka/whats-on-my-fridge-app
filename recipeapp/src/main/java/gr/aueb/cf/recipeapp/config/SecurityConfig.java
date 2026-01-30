package gr.aueb.cf.recipeapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/fridge", "/css/**", "/signup", "/login").permitAll()

                        .requestMatchers(
                                "/recipes/new",
                                "/recipes/add-ingredient",
                                "/recipes/mine",
                                "/recipes/*/delete",
                                "/recipes/*/edit",
                                "/recipes/*/edit/**"
                        ).authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/fridge", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/fridge")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
