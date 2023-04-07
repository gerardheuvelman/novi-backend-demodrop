package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.input.UserInputDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserPrivateDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserPublicDto;
import nl.ultimateapps.demoDrop.Models.Authority;
import nl.ultimateapps.demoDrop.Models.User;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface UserService {
    ArrayList<UserPublicDto> getUserPublicDtos(int limit);

    UserPublicDto getUserPublicDto(String username);

    ArrayList<UserPrivateDto> getUserPrivateDtos(int limit);

    UserPrivateDto getUserPrivateDto(String username) ;

    boolean userExists(String username);

    String createUser(UserPrivateDto userPrivateDto, String role);

    String updateUser(String username, UserPrivateDto userPrivateDto);

    String changePassword(String username, UserInputDto userInputDto);

    String changeEmail(String username, UserInputDto userInputDto);

    boolean deleteUser(String username) throws UserPrincipalNotFoundException;

    int deleteAllUsers() throws UserPrincipalNotFoundException;

    Set<Authority> getAuthorities(String username);

    void addAuthority(String username, String authority);

    void removeAuthority(String username, String authority);

    User updateExistingUser(User user, UserPrivateDto userPrivateDto);

    void addAuthorityToUser(Authority authority, User user);

    void removeAuthorityFromUser(Authority authority, User user);

    boolean checkAccountStatus(String username);

    boolean setAccountStatus(String username, boolean desiredStatus);

    int deleteSelectedUsers(List<String> usernames) throws UserPrincipalNotFoundException;
}
