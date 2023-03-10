package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.UserDto;
import nl.ultimateapps.demoDrop.Models.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.*;

@Service
@AllArgsConstructor
//@NoArgsConstructor kan niet hier
public class CustomUserDetailsServiceImpl implements UserDetailsService, CustomUserDetailsService {

    @Getter
    @Setter
    private UserServiceImpl userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDto userDto = userService.getUserDto(username);
        String password = userDto.getPassword();
        Set<Authority> authorities = userDto.getAuthorities();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Authority authority: authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return new org.springframework.security.core.userdetails.User(username, password, grantedAuthorities);
    }
}