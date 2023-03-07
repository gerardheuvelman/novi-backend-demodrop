package nl.ultimateapps.demoDrop.Services;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService {
    UserDetails loadUserByUsername(String username);
}
