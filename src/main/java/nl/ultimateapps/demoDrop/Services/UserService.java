package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.UserDto;
import nl.ultimateapps.demoDrop.Exceptions.BadRequestException;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Exceptions.UsernameNotFoundException;
import nl.ultimateapps.demoDrop.Models.Authority;
import nl.ultimateapps.demoDrop.Models.User;
import nl.ultimateapps.demoDrop.Repositories.UserRepository;
import nl.ultimateapps.demoDrop.Utils.RandomStringGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.*;

@Service
@AllArgsConstructor
//@NoArgsConstructor - dit leidt tot problemen?
public class UserService {

    @Lazy
    @Getter
    @Setter
    private PasswordEncoder passwordEncoder;

    @Getter
    @Setter
    private UserRepository userRepository;
    public List<UserDto> getUsers() {
        List<UserDto> collection = new ArrayList<>();
        List<User> list = userRepository.findAll();
        for (User user : list) {
            collection.add(fromUser(user));
        }
        return collection;
    }

    public UserDto getUser(String username) {
        UserDto dto = new UserDto();
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()){
            dto = fromUser(user.get());
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
        userDto.setApikey(randomString);
        User newUser = userRepository.save(toUser(userDto));
        return newUser.getUsername();
    }

    public String updateUser(String username, UserDto userDto) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userRepository.findById(username).get();
        userRepository.save(updateExistingUser(user, userDto));
        return user.getUsername();

    }

    public String deleteUser(String username) {
        userRepository.deleteById(username);
        return username;
    }

    public Set<Authority> getAuthorities(String username) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        UserDto userDto = fromUser(user);
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

    public static UserDto fromUser(User user){

        var dto = new UserDto();
        dto.username = user.getUsername();
        dto.password = user.getPassword();
        dto.enabled = user.isEnabled();
        dto.apikey = user.getApikey();
        dto.email = user.getEmail();
        dto.authorities = user.getAuthorities();
        return dto;
    }

    public User toUser(UserDto userDto) { // niet alle velden zijn meegenomen . Allenede velden die je meerstuurt bij het creeren vn ene gebruiker.

        var user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public User updateExistingUser(User user, UserDto userDto) {
        if (userDto.getUsername() != null) {
        user.setUsername(userDto.getUsername());
        }
        if (userDto.getPassword() != null) {
            user.setPassword(userDto.getPassword());
        }
        if (userDto.getEnabled() != null) {
            user.setEnabled(userDto.getEnabled());
        }
        if (userDto.getApikey() != null) {
            user.setApikey(userDto.getApikey());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getAuthorities() != null) {
            user.setAuthorities(userDto.getAuthorities());
        }
        return user;
    }
}