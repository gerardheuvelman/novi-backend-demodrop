package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.input.UserInputDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserPrivateDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserPublicDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Exceptions.UsernameNotFoundException;
import nl.ultimateapps.demoDrop.Helpers.mappers.AuthorityMapper;
import nl.ultimateapps.demoDrop.Helpers.mappers.UserMapper;
import nl.ultimateapps.demoDrop.Models.Authority;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.User;
import nl.ultimateapps.demoDrop.Repositories.DemoRepository;
import nl.ultimateapps.demoDrop.Repositories.UserRepository;
import nl.ultimateapps.demoDrop.Utils.AuthHelper;
import nl.ultimateapps.demoDrop.Utils.RandomStringGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.Instant;
import java.util.*;

import lombok.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Lazy
    @Getter
    @Setter
    private PasswordEncoder passwordEncoder;

    @Getter
    @Setter
    private UserRepository userRepository;

    @Getter
    @Setter
    private DemoRepository demoRepository;

    @Getter
    @Setter
    private DemoService demoService;

    @Override
    public ArrayList<UserPublicDto> getUserPublicDtos(int limit) {
        ArrayList<User> userList = GetUsersFromRepo(limit);
        ArrayList<UserPublicDto> userPublicDtoList = new ArrayList<>();
        for (User user : userList) {
            UserPublicDto userPublicDto = UserMapper.mapToPublicDto(user);
            userPublicDtoList.add(userPublicDto);
        }
        return userPublicDtoList;
    }

    @Override
    public UserPublicDto getUserPublicDto(String username) {
        User user = getUserFromRepo(username);
        return UserMapper.mapToPublicDto(user);
    }

    @Override
    public ArrayList<UserPrivateDto> getUserPrivateDtos(int limit) {
        ArrayList<User> userList = GetUsersFromRepo(limit);
        ArrayList<UserPrivateDto> userPrivateDtoList = new ArrayList<>();
        for (User user : userList) {
            UserPrivateDto userPrivateDto = UserMapper.mapToPrivateDto(user);
            userPrivateDtoList.add(userPrivateDto);
        }
        return userPrivateDtoList;
    }

    @Override
    public UserPrivateDto getUserPrivateDto(String username) {
        User user = getUserFromRepo(username);
        return UserMapper.mapToPrivateDto(user);
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    @Override
    public String createUser(UserPrivateDto userPrivateDto, String authorityName) {
        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        userPrivateDto.setPassword(passwordEncoder.encode(userPrivateDto.getPassword()));
        userPrivateDto.setEnabled(true);
//        userPrivateDto.setApikey(randomString); // use of an API key is deprecated, but I am leaving this line of code as a comment to be able to quickly reintroduce it should the need arise.
        userPrivateDto.setCreatedDate(Date.from(Instant.now()));
        userPrivateDto.setAuthorities(new HashSet<>());
        User user = UserMapper.mapToModel(userPrivateDto);
        addAuthority(user, authorityName);
        userRepository.save(user);
        return user.getUsername();
    }


    @Override
    public String updateUser(String username, UserPrivateDto userPrivateDto) {
        if (!AuthHelper.checkAuthorization(username)) { // check associative authorization
            throw new AccessDeniedException("User has insufficient rights to alter user details for user " + username);
        }
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        userPrivateDto.setPassword(passwordEncoder.encode(userPrivateDto.getPassword()));
        User user = userRepository.findById(username).get();
        userRepository.save(updateExistingUser(user, userPrivateDto));
        return user.getUsername();

    }

    @Override
    public String changePassword(String username, UserInputDto userInputDto) {
        if (!AuthHelper.checkAuthorization(username)) { // check associative authorization
            throw new AccessDeniedException("User " + AuthHelper.getPrincipalUsername() + " has insufficient rights to change the password for user " + username);
        }
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        String encodedPassword = passwordEncoder.encode(userInputDto.getPassword());
        userInputDto.setPassword(encodedPassword);
        User user = userRepository.findById(username).get();
        user.setPassword(userInputDto.getPassword());
        User savedUser = userRepository.save(user);
        return savedUser.getPassword();
    }

    @Override
    public String changeEmail(String username, UserInputDto userInputDto) {
        if (!AuthHelper.checkAuthorization(username)) { // check associative authorization
            throw new AccessDeniedException("User " + AuthHelper.getPrincipalUsername() + " has insufficient rights to change the email address for user " + username);
        }
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        user.setEmail(userInputDto.getEmail());
        userRepository.save(user);
        return user.getEmail();
    }

    @Override
    public boolean deleteUser(String username) throws UserPrincipalNotFoundException {
        if (!AuthHelper.checkAuthorization(username)) { // check associative authorization
            throw new AccessDeniedException("User " + AuthHelper.getPrincipalUsername() + " has insufficient rights to delete user " + username + " from the system");
        }
        if (userRepository.findById(username).isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        // Before we delete the user, we must first delete his/her favorites (otherwise we orphan entries in the helper table, or , using casading remove, we will end up deleting other user's demos )
        // Find favorite demos
        User user = userRepository.findById(username).get();
        Iterable<Demo> demoIterable = demoRepository.findByFavoriteOfUsersOrderByTitleAsc(user);
        for (Demo demo : demoIterable) {
            demoService.setFavStatus(demo.getDemoId(), false);
        }
//        // now, delete all demos this user has in the system
//        List<DemoDto> demoDtoList = demoService.getPersonalDemos(username);
//        List<Demo> demoList =DemoMapper.mapToModel(demoDtoList);
//        for (Demo demo : demoList) {
//            demoRepository.deleteById(demo.getDemoId());
//            demoRepository.save(demo);
//        }

        // Now we can safely delete the user
        userRepository.deleteById(username);
        return true;
    }


    @Override
    public int deleteSelectedUsers(List<String> usernames) throws UserPrincipalNotFoundException {
        // We will delete all users by calling deleteUser() on each of them
        //Side note: The account being used to make the request will NOT be deleted.
        int numDeletedUsers = 0;
        String principalUserName = AuthHelper.getPrincipalUsername();
        for (String username : usernames) {
            if (!username.equals(principalUserName)) {
                deleteUser(username);
                numDeletedUsers++;
            }
        }
        return numDeletedUsers;
    }

    @Override
    public int deleteAllUsers() throws UserPrincipalNotFoundException {
        // We will delete all users by retrieving a list of all users, and then calling deletUser() on each of them
        //Side note: The account being used to make the request will NOT be deleted.
        ArrayList<User> allUsers = GetUsersFromRepo(0);
        int numDeletedUsers = 0;
        String principalUserName = AuthHelper.getPrincipalUsername();
        for (User user : allUsers) {
            String username = user.getUsername();
            if (!username.equals(principalUserName)) {
                deleteUser(username);
                numDeletedUsers++;
            }
        }
        return numDeletedUsers;
    }


    @Override
    public Set<Authority> getAuthorities(String username) {
        if (!AuthHelper.checkAuthorization(username)) { // check associative authorization
            throw new AccessDeniedException("User " + AuthHelper.getPrincipalUsername() + " has insufficient rights to retrieve authorities for user " + username);
        }
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        UserPrivateDto userPrivateDto = UserMapper.mapToPrivateDto(user);
        return AuthorityMapper.mapToModel(userPrivateDto.getAuthorities());
    }

    @Override
    public void addAuthority(String username, String authorityName) {
        if (!userRepository.existsById(username))
        {
            throw new UsernameNotFoundException(username);
        }
        User user = userRepository.findById(username).get();
        addAuthority(user, authorityName);
    }

    @Override
    public void removeAuthority(String username, String authorityName) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        removeAuthority(user, authorityName);
    }

    private void addAuthority(User user, String authorityName) {
        Authority authority = new Authority(user.getUsername(), authorityName);
        user.addAuthority(authority);
        userRepository.save(user);
    }

    private void removeAuthority(User user, String authorityName) {
        Authority authorityToRemove = user.getAuthorities().stream().filter((a) -> a.getAuthority().equalsIgnoreCase(authorityName)).findAny().get();
        user.removeAuthority(authorityToRemove);
        userRepository.save(user);
    }


    @Override
    public User updateExistingUser(User user, UserPrivateDto userPrivateDto) {
        if (userPrivateDto.getUsername() != null) {
            user.setUsername(userPrivateDto.getUsername());
        }
        if (userPrivateDto.getPassword() != null) {
            user.setPassword(userPrivateDto.getPassword());
        }

        user.setEnabled(userPrivateDto.isEnabled());

        if (userPrivateDto.getEmail() != null) {
            user.setEmail(userPrivateDto.getEmail());
        }
        if (userPrivateDto.getCreatedDate() != null) {
            user.setCreatedDate(userPrivateDto.getCreatedDate());
        }
        if (userPrivateDto.getAuthorities() != null) {
            user.setAuthorities( AuthorityMapper.mapToModel(userPrivateDto.getAuthorities()));
        }
        return user;
    }

    @Override
    public void addAuthorityToUser(Authority authority, User user) {
        user.addAuthority(authority);
    }

    @Override
    public void removeAuthorityFromUser(Authority authority, User user) {
        user.removeAuthority(authority);
    }

    @Override
    public boolean checkAccountStatus(String username) {
        User user = new User();
        if (userRepository.findById(username).isPresent()) {
            user = userRepository.findById(username).get();
        }
        return user.isEnabled();
    }

    @Override
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