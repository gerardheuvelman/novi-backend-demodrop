package nl.ultimateapps.demoDrop.Services;

public class DemoDropGrantedAuthorityImpl implements DemoDropGrantedAuthority {


    @Override
    public String getAuthority() {
        return "ROLE_ADMIN";
    }
}
