package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.input.UserInputDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserPublicDto;
import nl.ultimateapps.demoDrop.Models.Authority;
import nl.ultimateapps.demoDrop.Models.User;

import java.util.ArrayList;
import java.util.Set;

public interface UserService {
    ArrayList<UserPublicDto> getUserPublicDtos(int limit);

    UserPublicDto getUserPublicDto(String username);

    ArrayList<UserDto> getUserDtos(int limit);

    UserDto getUserDto(String username);

    boolean userExists(String username);

    String createUser(UserDto userDto);

    String updateUser(String username, UserDto userDto);

    String changePassword(String username, UserInputDto userInputDto);

    String changeEmail(String username, UserInputDto userInputDto);

    boolean deleteUser(String username);

    Set<Authority> getAuthorities(String username);

    void addAuthority(String username, String authority);

    void removeAuthority(String username, String authority);

    User updateExistingUser(User user, UserDto userDto);

    void addAuthorityToUser(Authority authority, User user);

    void removeAuthorityFromUser(Authority authority, User user);

    boolean checkAccountStatus(String username);

    boolean setAccountStatus(String username, boolean desiredStatus);
}
