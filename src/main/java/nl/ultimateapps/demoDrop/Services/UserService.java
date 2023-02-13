package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.input.UserInputDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserDto;
import nl.ultimateapps.demoDrop.Exceptions.UsernameNotFoundException;
import nl.ultimateapps.demoDrop.Helpers.mappers.UserMapper;
import nl.ultimateapps.demoDrop.Models.Authority;
import nl.ultimateapps.demoDrop.Models.User;
import nl.ultimateapps.demoDrop.Repositories.UserRepository;
import nl.ultimateapps.demoDrop.Utils.RandomStringGenerator;
import org.springframework.context.annotation.Lazy;
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

    public ArrayList<UserDto> getUsers(int limit) {
        ArrayList<UserDto> userDtoList = new ArrayList<>();
        Iterable<User> userIterable = userRepository.findAllByOrderByCreatedDateDesc();
        ArrayList<User> userArrayList = new ArrayList<>();
        userIterable.forEach(userArrayList::add);
        int numResults = userArrayList.size();
        if (limit == 0) {
            // return full list
            for (User user : userArrayList) {
                UserDto userDto = UserMapper.mapToDto(user);
                userDtoList.add(userDto);
            }
        } else {
            // return limited list
            for (int i = 0; i < (Math.min(numResults, limit)); i++) {
                UserDto userDto = UserMapper.mapToDto(userArrayList.get(i));
                userDtoList.add(userDto);
            }
        }
        return userDtoList;
    }

    public UserDto getUser(String username) {
        UserDto dto = new UserDto();
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()){
            dto = UserMapper.mapToDto(user.get());
        }else {
            throw new UsernameNotFoundException(username);
        }
        return dto;
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
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userRepository.findById(username).get();
        userRepository.save(updateExistingUser(user, userDto));
        return user.getUsername();

    }

    public String changePassword(String username, UserInputDto userInputDto) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        userInputDto.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        User user = userRepository.findById(username).get();
        user.setPassword(userInputDto.getPassword());
        userRepository.save(user);
        return user.getPassword();
    }

    public String changeEmail(String username, UserInputDto userInputDto) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        user.setEmail(userInputDto.getEmail());
        userRepository.save(user);
        return user.getEmail();
    }

    public String deleteUser(String username) {
        userRepository.deleteById(username);
        return username;
    }

    public Set<Authority> getAuthorities(String username) {
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

//    public User toUser(UserDto userDto) { // niet alle velden zijn meegenomen . Allenede velden die je meerstuurt bij het creeren vn ene gebruiker.
//
//        var user = new User();
//        user.setUsername(userDto.getUsername());
//        user.setPassword(userDto.getPassword());
//        user.setEmail(userDto.getEmail());
//        user.setCreatedDate(userDto.getCreatedDate());
//        return user;
//    }

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
}