package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.input.UserInputDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserPublicDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Exceptions.UsernameNotFoundException;
import nl.ultimateapps.demoDrop.Helpers.mappers.UserMapper;
import nl.ultimateapps.demoDrop.Models.Authority;
import nl.ultimateapps.demoDrop.Models.User;
import nl.ultimateapps.demoDrop.Repositories.UserRepository;
import nl.ultimateapps.demoDrop.Utils.AuthHelper;
import nl.ultimateapps.demoDrop.Utils.RandomStringGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;

import lombok.*;

@Service
@AllArgsConstructor
public class UserService {

    @Lazy
    @Getter
    @Setter
    private PasswordEncoder passwordEncoder;

    @Getter
    @Setter
    private UserRepository userRepository;

    public ArrayList<UserPublicDto> getUserPublicDtos (int limit) {
        ArrayList<User> userList = GetUsersFromRepo(limit);
        ArrayList<UserPublicDto> userPublicDtoList = new ArrayList<>();
        for (User user : userList) {
            UserPublicDto userPublicDto = UserMapper.mapToPublicDto(user);
            userPublicDtoList.add(userPublicDto);
        }
        return userPublicDtoList;
    }

    public UserPublicDto getUserPublicDto(String username) {
        User user = getUserFromRepo(username);
        return UserMapper.mapToPublicDto(user);
    }

    public ArrayList<UserDto> getUserDtos(int limit) {
        ArrayList<User> userList = GetUsersFromRepo(limit);
        ArrayList<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            UserDto userDto = UserMapper.mapToDto(user);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    public UserDto getUserDto(String username) {
        User user = getUserFromRepo(username);
        return UserMapper.mapToDto(user);
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public String createUser(UserDto userDto) {
        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setEnabled(true);
        userDto.setApikey(randomString);
        userDto.setCreatedDate(Date.from(Instant.now()));
        userDto.setAuthorities(new HashSet<>());
        User newUser = userRepository.save(UserMapper.mapToModel(userDto));
        return newUser.getUsername();
    }


    public String updateUser(String username, UserDto userDto) {
        if (!AuthHelper.checkAuthorization(username)) { // check associative authorization
            throw new AccessDeniedException("User has insufficient rights to alter user details for user " + username);
        }
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userRepository.findById(username).get();
        userRepository.save(updateExistingUser(user, userDto));
        return user.getUsername();

    }

    public String changePassword(String username, UserInputDto userInputDto) {
        if (!AuthHelper.checkAuthorization(username)) { // check associative authorization
            throw new AccessDeniedException("User" + AuthHelper.getPrincipalUsername() + " has insufficient rights to change the password for user " + username);
        }
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        userInputDto.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        User user = userRepository.findById(username).get();
        user.setPassword(userInputDto.getPassword());
        userRepository.save(user);
        return user.getPassword();
    }

    public String changeEmail(String username, UserInputDto userInputDto) {
        if (!AuthHelper.checkAuthorization(username)) { // check associative authorization
            throw new AccessDeniedException("User "+ AuthHelper.getPrincipalUsername() +" has insufficient rights to change the email address for user " + username);
        }
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        user.setEmail(userInputDto.getEmail());
        userRepository.save(user);
        return user.getEmail();
    }

    public boolean deleteUser(String username) {
        if (!AuthHelper.checkAuthorization(username)) { // check associative authorization
            throw new AccessDeniedException("User "+ AuthHelper.getPrincipalUsername() +" has insufficient rights to delete user " + username + " from the system");
        }

        if (AuthHelper.checkAuthorization(username)) {
            userRepository.deleteById(username);
            return true;
        } else return false;
    }

    public Set<Authority> getAuthorities(String username) {
        if (!AuthHelper.checkAuthorization(username)) { // check associative authorization
            throw new AccessDeniedException("User "+ AuthHelper.getPrincipalUsername() +" has insufficient rights to retrieve authorities for user " + username);
        }
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        UserDto userDto = UserMapper.mapToDto(user);
        return userDto.getAuthorities();
    }

    public void addAuthority(String username, String authority) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        user.addAuthority(new Authority(username, authority));
        userRepository.save(user);
    }

    public void removeAuthority(String username, String authority) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        Authority authorityToRemove = user.getAuthorities().stream().filter((a) -> a.getAuthority().equalsIgnoreCase(authority)).findAny().get();
        user.removeAuthority(authorityToRemove);
        userRepository.save(user);
    }

    public User updateExistingUser(User user, UserDto userDto) {
        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }
        if (userDto.getPassword() != null) {
            user.setPassword(userDto.getPassword());
        }

        user.setEnabled(userDto.isEnabled());

        if (userDto.getApikey() != null) {
            user.setApikey(userDto.getApikey());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getCreatedDate() != null) {
            user.setCreatedDate(userDto.getCreatedDate());
        }
        if (userDto.getAuthorities() != null) {
            user.setAuthorities(userDto.getAuthorities());
        }
        return user;
    }

    public void addAuthorityToUser(Authority authority, User user) {
        user.addAuthority(authority);
    }

    public void removeAuthorityFromUser(Authority authority, User user) {
        user.removeAuthority(authority);
    }

    public boolean checkAccountStatus(String username) {
        User user = new User();
        if (userRepository.findById(username).isPresent()) {
            user = userRepository.findById(username).get();
        }
        return user.isEnabled();
    }

    public boolean setAccountStatus(String username, boolean desiredStatus) {
        User user;
        if (userRepository.findById(username).isPresent()) {
            user = userRepository.findById(username).get();
        } else {
            throw new RecordNotFoundException();
        }
        user.setEnabled(desiredStatus);
        userRepository.save(user);
        return checkAccountStatus(username);
    }

    private ArrayList<User> GetUsersFromRepo(int limit) {
        Iterable<User> userIterable = userRepository.findAllByOrderByCreatedDateDesc();
        ArrayList<User> fullUserList = new ArrayList<>();
        userIterable.forEach(fullUserList::add);
        int numResults = fullUserList.size();
        if (limit == 0) {
            // return full list
            return fullUserList;
        } else {
            // return limited list
            ArrayList<User> limitedUserList = new ArrayList<>();
            for (int i = 0; i < (Math.min(numResults, limit)); i++) {
                User user = fullUserList.get(i);
                limitedUserList.add(user);
            }
            return limitedUserList;
        }
    }

    // Private functions:
    private User getUserFromRepo(String username) {
        if (userRepository.findById(username).isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return userRepository.findById(username).get();
    }

}