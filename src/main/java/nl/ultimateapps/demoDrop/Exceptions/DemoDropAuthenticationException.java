package nl.ultimateapps.demoDrop.Exceptions;

import org.springframework.security.core.AuthenticationException;

public class DemoDropAuthenticationException extends AuthenticationException {

    public DemoDropAuthenticationException(String msg) {
        super(msg);
    }
}
