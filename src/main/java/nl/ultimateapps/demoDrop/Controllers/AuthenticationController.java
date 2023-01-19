package nl.ultimateapps.demoDrop.Controllers;

import nl.ultimateapps.demoDrop.Dtos.input.AuthenticationRequest;
import nl.ultimateapps.demoDrop.Dtos.output.AuthenticationResponse;
import nl.ultimateapps.demoDrop.Utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import lombok.*;

@CrossOrigin
@RestController
@AllArgsConstructor
public class AuthenticationController {

    @Getter
    @Setter
    private AuthenticationManager authenticationManager;

    @Getter
    @Setter
    private UserDetailsService userDetailsService;

    @Getter
    @Setter
    private  JwtUtil jwtUtil;

    @GetMapping(value = "/authenticated")
    public ResponseEntity<Object> authenticated(Authentication authentication, Principal principal) {
        return ResponseEntity.ok().body(principal);
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        }
        catch (BadCredentialsException ex) {
            throw new Exception("Incorrect username or password", ex);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}