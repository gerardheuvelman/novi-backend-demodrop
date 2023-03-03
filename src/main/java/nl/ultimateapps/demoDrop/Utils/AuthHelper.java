package nl.ultimateapps.demoDrop.Utils;

import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class AuthHelper {

    public static String getPrincipalUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
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

    public static boolean checkAuthorization(String username) {
        // make sure that the username current user is the same as the username being passed in.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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

        public static boolean checkAuthorization (User user){
            String username = user.getUsername();
            if (!checkAuthorization(username)) {
                throw new AccessDeniedException("User has insufficient rights to perform this action. ");
            }

            return checkAuthorization(username);
        }

        public static boolean checkAuthorization (Demo demo){
            String username = demo.getUser().getUsername();
            if (!checkAuthorization(username)) {
                throw new AccessDeniedException("User has insufficient rights to perform this action. ");
            }
            return checkAuthorization(username);
        }

        public static boolean checkAuthorization (Conversation conversation){
            String producerName = conversation.getProducer().getUsername();
            String interestedUserName = conversation.getInterestedUser().getUsername();
            boolean principalIsProducerOrAdmin = checkAuthorization(producerName);
            boolean principalIsInterestedUserOrAdmin = checkAuthorization(interestedUserName);
            if (!(principalIsProducerOrAdmin || principalIsInterestedUserOrAdmin)) {
                throw new AccessDeniedException("User has insufficient rights to perform this action. ");
            }
            return principalIsProducerOrAdmin || principalIsInterestedUserOrAdmin;
        }
    }
