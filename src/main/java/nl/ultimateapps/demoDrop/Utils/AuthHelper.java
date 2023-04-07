package nl.ultimateapps.demoDrop.Utils;

import lombok.AllArgsConstructor;
import nl.ultimateapps.demoDrop.Exceptions.DemoDropAuthenticationException;
import nl.ultimateapps.demoDrop.Models.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

@AllArgsConstructor
public class AuthHelper {

    public static String getPrincipalUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new DemoDropAuthenticationException("User is not authenticated");
        }
        return authentication.getName();
    }

    public static boolean hasAdminRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return hasAdminRole(authentication);
    }

    private static boolean hasAdminRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = false;
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }
        return isAdmin;
    }

    public static boolean checkAuthorization() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new DemoDropAuthenticationException("User must be authenticated to perform this action");
        }
        if (hasAdminRole(authentication)) {
            return true;
        } else return false;
    }

    public static boolean checkAuthorization(String username) {
        // make sure that the username current user is the same as the username being passed in.
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new DemoDropAuthenticationException("User must be authenticated to perform this action");
        }
        if (hasAdminRole(authentication)) {
            return true;
        }
        String currentPrincipalName = authentication.getName();
        if (!username.equals(currentPrincipalName)) {
            return false;
        } else return true;
    }

    public static boolean checkAuthorization(User user) {
        String username = user.getUsername();
        return checkAuthorization(username);
    }

    public static boolean checkAuthorization(Demo demo) {
        String username = demo.getProducer().getUsername();
        return checkAuthorization(username);
    }

    public static boolean checkAuthorization(Conversation conversation) {
        String correspondent = conversation.getCorrespondent().getUsername();
        String initiatorName = conversation.getInitiator().getUsername();
        boolean principalIsCorrespondentOrAdmin = checkAuthorization(correspondent);
        boolean principalIsInitiatorOrAdmin = checkAuthorization(initiatorName);
        return principalIsCorrespondentOrAdmin || principalIsInitiatorOrAdmin;
    }
}