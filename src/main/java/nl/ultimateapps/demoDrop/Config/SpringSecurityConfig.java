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

                .antMatchers(HttpMethod.GET,"/users").hasRole("ADMIN") // ADMIN CONTROL PANEL
                .antMatchers(HttpMethod.GET,"/users/**").authenticated()  // FETCH USER DATA
                .antMatchers(HttpMethod.POST, "/users").permitAll() // REGISTER A NEW USER
                .antMatchers(HttpMethod.POST, "/users/admin").hasRole("ADMIN") // CREATE ADMIN ACCOUNT
                .antMatchers(HttpMethod.PUT, "/users/**").authenticated() // EDIT PROFILE
                .antMatchers(HttpMethod.PATCH, "/users/**/change-password").authenticated() // CHANGE PASSWORD
                .antMatchers(HttpMethod.PATCH, "/users/**/change-email").authenticated() // CHANGE EMAIL
                .antMatchers(HttpMethod.DELETE, "/users").hasRole("ADMIN") // ADMIN CONTROL PANEL
                .antMatchers(HttpMethod.DELETE,  "/users/**").authenticated() // DELETE YOUR ACCOUNT

                .antMatchers(HttpMethod.GET, "/users/**/demos").authenticated() // PERSONAL DEMO LIST
                .antMatchers(HttpMethod.GET, "/users/**/favdemos").authenticated() // PERSONAL FAVORITE DEMO LIST
                .antMatchers(HttpMethod.GET, "/users/**/conversations").authenticated() // PERSONAL INBOX
                .antMatchers(HttpMethod.GET, "/users/**/authorities").hasRole("ADMIN")  // ADMIN CONTROL PANEL
                .antMatchers(HttpMethod.POST, "/users/**/authorities").hasRole("ADMIN")  // ADMIN CONTROL PANEL
                .antMatchers(HttpMethod.DELETE, "/users/**/authorities/**").hasRole("ADMIN")  // ADMIN CONTROL PANEL

                .antMatchers(HttpMethod.GET,"/demos").permitAll() // LIST DEMOS (PUBLICLY AVAILABLE DATA)
                .antMatchers(HttpMethod.GET,"/demos/**").permitAll() // GET DEMO (PUBLICLY AVAILABLE DATA)
                .antMatchers(HttpMethod.GET, "/demos/**/isfav").authenticated() // ADD A USER TO FAVLIST
                .antMatchers(HttpMethod.POST,"/demos").authenticated() // CREATE/UPLOAD NEW DEMO
                .antMatchers(HttpMethod.PUT, "/demos/**").authenticated() // EDIT DEMO DATA
                .antMatchers(HttpMethod.PATCH, "/demos/**/setgenre/**").authenticated() // ASSIGN A GENRE TO A DEMO
                .antMatchers(HttpMethod.PATCH, "/demos/**/setfav").authenticated() // REMOVE A USER FROM FAVLIST
                .antMatchers(HttpMethod.DELETE,"/demos").hasRole("ADMIN")// ADMIN CONTROL PANEL
                .antMatchers(HttpMethod.DELETE, "/demos/**").authenticated()// !!ONLY DELETE YOUR OWN DEMO (OR ADMIN ROLE)!!

                .antMatchers(HttpMethod.POST,"/demos/**/file").authenticated() // ASSIGN FILE (IN BODY) TO DEMO (MAX SIZE 1 MB!!!)

                .antMatchers(HttpMethod.GET, "/files/**").authenticated() // DOWNLOAD SINGLE FILE
                .antMatchers(HttpMethod.POST,"/files").authenticated() // UPLOAD SINGLE FILE  INTERNAL BACKEND REQUEST FOR ASSIGN FILE ROUTE

                .antMatchers(HttpMethod.GET,"/conversations").hasRole("ADMIN") // ADMIN CONTROL PANEL
                .antMatchers(HttpMethod.GET,"/conversations/**").authenticated() // CONVERSATION DETAILS
                .antMatchers(HttpMethod.POST,"/conversations").authenticated() // CREATE NEW CONVERSATION
                .antMatchers(HttpMethod.PUT, "/conversations/**").authenticated() // REPLY TO EXISTING CONVERSATION
                .antMatchers(HttpMethod.DELETE,"/conversations").hasRole("ADMIN") // ADMIN CONTROL PANEL
                .antMatchers(HttpMethod.DELETE, "/conversations/**").authenticated() // DELETE A CONVERSATION / ALL CONVERSATIONS

                .antMatchers(HttpMethod.GET,"/genres", "/genres/**").permitAll() // PUBLIC DATA
                .antMatchers(HttpMethod.POST,"/genres", "/genres/**").hasRole("ADMIN") // ADMIN CONTROL PANEL
                .antMatchers(HttpMethod.DELETE,"/genres", "/genres/**").hasRole("ADMIN") // ADMIN CONTROL PANEL

                .antMatchers(HttpMethod.POST, "/authenticate").permitAll() //USER LOGIN
                .antMatchers(HttpMethod.GET, "/authenticated").authenticated() // AUTHENTICATION TEST ROUTE

                .antMatchers(HttpMethod.POST, "admin/authenticate").permitAll() //ADMIN LOGIN TODO Implement Admin Control Panel
                .anyRequest().permitAll()  // DEVELOPMENT PURPOSES - change to ADMIN later

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}