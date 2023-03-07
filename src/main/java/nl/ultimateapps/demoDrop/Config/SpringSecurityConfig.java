package nl.ultimateapps.demoDrop.Config;

import nl.ultimateapps.demoDrop.Services.CustomUserDetailsServiceImpl;
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
    private CustomUserDetailsServiceImpl customUserDetailsService;

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

                .antMatchers(HttpMethod.GET,"/users/public").permitAll() // LIST USERS (PUBLIC INFO)
                .antMatchers(HttpMethod.GET,"/users/public/**").permitAll()  // GET USER (PUBLIC INFO)

                .antMatchers(HttpMethod.GET,"/users").hasRole("ADMIN") // LIST USERS
                .antMatchers(HttpMethod.GET, "/users/**/demos").permitAll() // PERSONAL DEMO LIST
                .antMatchers(HttpMethod.GET, "/users/**/favdemos").authenticated() // PERSONAL FAVORITE DEMO LIST
                .antMatchers(HttpMethod.GET, "/users/**/conversations").authenticated() // PERSONAL INBOX
                .antMatchers(HttpMethod.GET, "/users/**/authorities").hasRole("ADMIN")  // LIST USER AUTHORITIES
                .antMatchers(HttpMethod.GET,"/users/**/getstatus").hasRole("ADMIN")  // FETCH ACCOUNT STATUS
                .antMatchers(HttpMethod.GET,"/users/**/").permitAll() // GET ESSENTIAL USER INFO
                .antMatchers(HttpMethod.POST, "/users").permitAll() // REGISTER A NEW USER
                .antMatchers(HttpMethod.POST, "/users/admin").hasRole("ADMIN") // CREATE ADMIN ACCOUNT
                .antMatchers(HttpMethod.POST, "/users/**/authorities").hasRole("ADMIN")  // ASSIGN AUTHORITY TO USER
                .antMatchers(HttpMethod.PUT, "/users/**").authenticated() // EDIT PROFILE
                .antMatchers(HttpMethod.PATCH, "/users/**/change-password").authenticated() // CHANGE PASSWORD
                .antMatchers(HttpMethod.PATCH, "/users/**/change-email").authenticated() // CHANGE EMAIL
                .antMatchers(HttpMethod.PATCH, "/users/**/setstatus").hasRole("ADMIN") // SET ACCOUNT STATUS
                .antMatchers(HttpMethod.DELETE, "/users").hasRole("ADMIN") // DELETE ALL USERS
                .antMatchers(HttpMethod.DELETE, "/users/**/authorities/**").hasRole("ADMIN")  // REMOVE AUTHORITY FROM USER
                .antMatchers(HttpMethod.DELETE,  "/users/**").authenticated() // DELETE USER ACCOUNT

                .antMatchers(HttpMethod.GET,"/demos").permitAll() // LIST DEMOS (PUBLICLY AVAILABLE DATA)
                .antMatchers(HttpMethod.GET,"/demos/**").permitAll() // GET DEMO (PUBLICLY AVAILABLE DATA)
                .antMatchers(HttpMethod.GET, "/demos/**/isfav").authenticated() // CHECK DEMO AGAINST FAVLIST
                .antMatchers(HttpMethod.POST,"/demos").authenticated() // CREATE/UPLOAD NEW DEMO
                .antMatchers(HttpMethod.PUT, "/demos/**").authenticated() // EDIT DEMO DATA
                .antMatchers(HttpMethod.PATCH, "/demos/**/setgenre/**").authenticated() // ASSIGN A GENRE TO A DEMO
                .antMatchers(HttpMethod.PATCH, "/demos/**/setfav").authenticated() // ADD / REMOVE A USER FROM FAVLIST
                .antMatchers(HttpMethod.DELETE,"/demos").hasRole("ADMIN")// DELETE ALL DEMOS
                .antMatchers(HttpMethod.DELETE, "/demos/**").authenticated()// DELETE SINGLE DEMO

                .antMatchers(HttpMethod.POST,"/demos/**/upload").authenticated() // UPLOAD MP3 FILE & ASSIGN TO EXISTING DEMO (>1MB ENABLED)
                .antMatchers(HttpMethod.POST,"/demos/**/download").authenticated() // DOWNLOAD MP3 FILE

                // Direct CRUD for audiofiles (admin only)
                .antMatchers(HttpMethod.GET, "/audiofiles").hasRole("ADMIN") // LIST AUDIOFILES
                .antMatchers(HttpMethod.GET, "/audiofiles/**").hasRole("ADMIN") // GET SINGLE AUDIOFILE INFO
                .antMatchers(HttpMethod.DELETE,"/audiofiles").hasRole("ADMIN") // DELETE ALL AUDIOFILES (ALSO ON DISK)
                .antMatchers(HttpMethod.DELETE,"/audiofiles/**").hasRole("ADMIN") // DELETE SINGLE AUDIOFILE (ALSO ON DISK)
                .antMatchers(HttpMethod.DELETE,"/audiofiles/purge").hasRole("ADMIN") // DELETE ORPHAN MP3 FILES ON DISK

                .antMatchers(HttpMethod.GET,"/conversations").hasRole("ADMIN") // LIST CONVERSATIONS
                .antMatchers(HttpMethod.GET,"/conversations/**").authenticated() // CONVERSATION DETAILS
                .antMatchers(HttpMethod.POST,"/conversations").authenticated() // CREATE NEW CONVERSATION
                .antMatchers(HttpMethod.PUT, "/conversations/**").authenticated() // REPLY TO EXISTING CONVERSATION
                .antMatchers(HttpMethod.PATCH, "/conversations/**/markread").authenticated()//  MARK CONVERSATION AS READ
                .antMatchers(HttpMethod.DELETE,"/conversations").hasRole("ADMIN") // DELETE ALL CONVERSATIONS
                .antMatchers(HttpMethod.DELETE, "/conversations/**").hasRole("ADMIN") //  DELETE SINGLE CONVERSATION

                .antMatchers(HttpMethod.GET,"/genres", "/genres/**").permitAll() // LIST MUSIC GENRES
                .antMatchers(HttpMethod.POST,"/genres", "/genres/**").hasRole("ADMIN") // CREATE MUSIC GENRE
                .antMatchers(HttpMethod.DELETE,"/genres", "/genres/**").hasRole("ADMIN") // DELETE MUSIC GENRE

                .antMatchers(HttpMethod.POST, "/authenticate").permitAll() //USER LOGIN
                .antMatchers(HttpMethod.GET, "/authenticated").authenticated() // AUTHENTICATION TEST ROUTE

                .antMatchers(HttpMethod.POST, "email/send").authenticated() //SEND EMAIL
                .antMatchers(HttpMethod.POST, "email/sendwithattachment").authenticated() //SEND EMAIL WITH AN ATTACHMENT

                .anyRequest().permitAll()  // DEVELOPMENT PURPOSES! CHANGE TO DENYALL() BEFORE RELEASE!!

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}