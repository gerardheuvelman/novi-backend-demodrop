package nl.ultimateapps.demoDrop.Controllers;

import nl.ultimateapps.demoDrop.Dtos.input.UserInputDto;
import nl.ultimateapps.demoDrop.Dtos.output.ConversationDto;
import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserDto;
import nl.ultimateapps.demoDrop.Exceptions.BadRequestException;
import nl.ultimateapps.demoDrop.Services.ConversationService;
import nl.ultimateapps.demoDrop.Services.DemoService;
import nl.ultimateapps.demoDrop.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.Map;
import lombok.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/users")
@AllArgsConstructor
public class UserController {

    @Getter
    @Setter
    private UserService userService;

    @Getter
    @Setter
    private DemoService demoservice;

    @Getter
    @Setter
    private ConversationService conversationService;

    @GetMapping(value = "")
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam int limit) {
        List<UserDto> userDtos = userService.getUsers(limit);
        return ResponseEntity.ok().body(userDtos);
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<Object> getUser(@PathVariable("username") String username) {
        UserDto user = userService.getUser(username);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        else return ResponseEntity.ok().body(user);
    }

    @GetMapping("{username}/getstatus")
    public ResponseEntity<Boolean> checkIsEnabled(@PathVariable String username) {
        boolean accountStatus  = userService.checkAccountStatus(username);
        return ResponseEntity.ok(accountStatus);
    }

    @PostMapping(value = "")
    public ResponseEntity<Object> createStandardUser(@RequestBody UserDto dto) {;
        if (!userService.userExists(dto.getUsername())) {
            String newUsername = userService.createUser(dto);
            userService.addAuthority(newUsername, "ROLE_USER");
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}").buildAndExpand(newUsername).toUri();
            return ResponseEntity.created(location).build();
        } else return ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping(value = "/admin")
    public ResponseEntity<Object> createAdminUser(@RequestBody UserDto dto) {;
        if (!userService.userExists(dto.getUsername())) {
            String newUsername = userService.createUser(dto);
            userService.addAuthority(newUsername, "ROLE_ADMIN");
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}").buildAndExpand(newUsername).toUri();
            return ResponseEntity.created(location).build();
        } else return ResponseEntity.unprocessableEntity().build();
    }


    @PutMapping(value = "/{username}")
    public ResponseEntity<String> updateUser(@PathVariable("username") String username, @RequestBody UserDto dto) {
        String userFromDb= userService.updateUser(username, dto);
        return ResponseEntity.ok("user \"" + userFromDb + "\" was updated successfully" );
    }

    @PatchMapping(value = "/{username}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable("username") String username, @RequestBody UserInputDto userInputDto) {
        String hashFromDb = userService.changePassword(username, userInputDto);
        return ResponseEntity.ok("Password was updated successfully. Hash: " + hashFromDb);
    }

    @PatchMapping(value = "/{username}/setstatus")
    public ResponseEntity<Boolean> setAccountStatus(@PathVariable("username") String username, @RequestParam boolean status) {
         boolean newAccountStatus = userService.setAccountStatus(username, status);
        return ResponseEntity.ok(newAccountStatus);
    }


    @PatchMapping(value = "/{username}/change-email")
    public ResponseEntity<String> changeEmail(@PathVariable("username") String username, @RequestBody UserInputDto userInputDto) {
        String emailFromDb = userService.changeEmail(username, userInputDto);
        return ResponseEntity.ok("Email was successfully updated to " + emailFromDb);
    }



    @DeleteMapping(value = "/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable("username") String username) {
        String userFromService = userService.deleteUser(username);
        return ResponseEntity.ok("User \""+ userFromService + "\" was deleted successfully");
    }

    @GetMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> getUserAuthorities(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.getAuthorities(username));
    }

    @PostMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> addUserAuthority(@PathVariable("username") String username, @RequestBody Map<String, Object> fields) {
        try {
            String authorityName = (String) fields.get("authority");
            userService.addAuthority(username, authorityName);
            return ResponseEntity.noContent().build();
        }
        catch (Exception ex) {
            throw new BadRequestException();
        }
    }

    @DeleteMapping(value = "/{username}/authorities/{authority}")
    public ResponseEntity<Object> deleteUserAuthority(@PathVariable("username") String username, @PathVariable("authority") String authority) {
        userService.removeAuthority(username, authority);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{username}/demos")
    public ResponseEntity<List<DemoDto>> getDemosForUser(@PathVariable("username") String username) {
        List<DemoDto> demoDtos = demoservice.getPersonalDemos(username);
        return ResponseEntity.ok().body(demoDtos);
    }

    @GetMapping(value = "/{username}/favdemos")
    public ResponseEntity<List<DemoDto>> getFavoriteDemosForUser(@PathVariable("username") String username) {
        List<DemoDto> demoDtos = demoservice.getFavoriteDemos(username);
        return ResponseEntity.ok().body(demoDtos);
    }

    @GetMapping(value = "/{username}/conversations")
    public ResponseEntity<List<ConversationDto>> getConversationsForUser(@PathVariable("username") String username) {
        List<ConversationDto> conversationDtos = conversationService.getPersonalConversations(username);
        return ResponseEntity.ok().body(conversationDtos);
    }
}
