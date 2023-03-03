package nl.ultimateapps.demoDrop.Utils;

import org.springframework.security.core.AuthenticationException;

public class DemoDropAuthenticationException extends AuthenticationException {

    public DemoDropAuthenticationException(String msg) {
        super(msg);
    }
}
