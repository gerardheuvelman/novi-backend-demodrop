package nl.ultimateapps.demoDrop.Config;

import nl.ultimateapps.demoDrop.Services.CustomUserDetailsService;
import nl.ultimateapps.demoDrop.Filters.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.*;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SpringSecurityConfig {

    @Getter
    @Setter
    private PasswordEncoder passwordEncoder;

    @Getter
    @Setter
    private  CustomUserDetailsService customUserDetailsService;

    @Getter
    @Setter
    private  JwtRequestFilter jwtRequestFilter;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {
        //JWT token authentication
        http
                .csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()

                .antMatchers(HttpMethod.GET,"/users", "/users/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/users", "/users/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/users", "/users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/users", "/users/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET,"/demos", "/demos/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST,"/demos", "/demos/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.PUT,"/demos", "/demos/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE,"/demos", "/demos/**").hasAnyRole("ADMIN", "USER")

                .antMatchers(HttpMethod.GET,"/conversations", "/conversations/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST,"/conversations", "/conversations/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.PUT,"/conversations", "/conversations/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE,"/conversations", "/conversations/**").hasAnyRole("ADMIN", "USER")

                .antMatchers("/authenticated").authenticated()
                .antMatchers("/authenticate").permitAll()/*alleen dit punt mag toegankelijk zijn voor niet ingelogde gebruikers*/
                .anyRequest().permitAll()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}