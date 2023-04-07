package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.input.UserInputDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserPrivateDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserPublicDto;
import nl.ultimateapps.demoDrop.Helpers.mappers.UserMapper;
import nl.ultimateapps.demoDrop.Models.Authority;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.User;
import nl.ultimateapps.demoDrop.Repositories.DemoRepository;
import nl.ultimateapps.demoDrop.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {UserServiceImpl.class})
public class UserServiceTest extends ServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private DemoRepository demoRepository;

    @Mock
    private DemoService demoService;

    @Mock
    private Demo demo;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    public void setUp() {
        // Re-initialize the entire in-memory database using the base class setup method.
        super.reInitializeInMemoryDatabase();
    }

    @Test
    public void getUserPublicDtosReturnsAListOfUserPublicDtos() {
        //ARRANGE
        List<UserPublicDto> expectedUserPublicDtoList = allUserPublicDtos;

        //GIVEN
        Mockito.when(userRepository.findAllByOrderByCreatedDateDesc()).thenReturn(allUsers);

        //ACT
        //WHEN
        List<UserPublicDto> actualUserPublicDtoList = userServiceImpl.getUserPublicDtos(0);

        //ASSERT
        //THEN
        UserPublicDto firstEntry = actualUserPublicDtoList.get(0);
        UserPublicDto secondEntry = actualUserPublicDtoList.get(1);

        assertEquals(user.getUsername(), firstEntry.getUsername());
        assertEquals(admin.getUsername(), secondEntry.getUsername());

    }

    @Test
    void getUserPublicDtoReturnsTheCorrectUserPublicDto() {
        //ARRANGE
        String username = user.getUsername();
        UserPublicDto expectedUserPublicDto = userUserPublicDto;
        //GIVEN

        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));

        //ACT
        //WHEN
        UserPublicDto actualUserPublicDto = userServiceImpl.getUserPublicDto(username);

        //ASSERT
        //THEN
        assertEquals(expectedUserPublicDto.getUsername(), actualUserPublicDto.getUsername());
    }

    @Test
    public void getUserPublicDtosReturnsAListOfUserDtos() {
        //ARRANGE
        List<UserPrivateDto> expectedUserPrivateDtoList = allUserPrivateDtos;

        //GIVEN
        Mockito.when(userRepository.findAllByOrderByCreatedDateDesc()).thenReturn(allUsers);

        //ACT
        //WHEN
        List<UserPublicDto> actualUserPublicDtoList = userServiceImpl.getUserPublicDtos(0);

        //ASSERT
        //THEN
        UserPublicDto firstEntry = actualUserPublicDtoList.get(0);
        UserPublicDto secondEntry = actualUserPublicDtoList.get(1);

        assertEquals(user.getUsername(), firstEntry.getUsername());
        assertEquals(admin.getUsername(), secondEntry.getUsername());
    }

    @Test
    void getUserDtoReturnsTheCorrectUserDto() {
        //ARRANGE
        String username = user.getUsername();
        UserPrivateDto expectedUserPrivateDto = userUserPrivateDto;
        //GIVEN

        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));

        //ACT
        //WHEN
        UserPrivateDto actualUserPrivateDto = userServiceImpl.getUserPrivateDto(username);

        //ASSERT
        //THEN
        assertEquals(expectedUserPrivateDto.getUsername(), actualUserPrivateDto.getUsername());
    }

    @Test
    void userExistsReturnsTrue() {
        //ARRANGE
        String username = user.getUsername();
        boolean expectedResult = true;

        //GIVEN
        Mockito.when(userRepository.existsById(username)).thenReturn(true);

        //ACT
        //WHEN
        boolean actualResult = userServiceImpl.userExists(username);

        //ASSERT
        //THEN
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void createUserReturnsTheUsersUsername() throws UserPrincipalNotFoundException {
        //ARRANGE
        User newUser = new User("NewUser", "12345", true, "newuser@gmail.com", laterDate, userAuthoritySet, null, null, null, null, null, null);


        UserPrivateDto postRequestBodyUserPrivateDto = new UserPrivateDto("NewUser", "12345", true, "newuser@gmail.com", null, null, null, null, null, null, null, null);

        String expectedString = "NewUser";

        //GIVEN
        Mockito.when(userRepository.save(any(User.class))).thenReturn(newUser);

        //ACT
        //WHEN
        String actualString = userServiceImpl.createUser(postRequestBodyUserPrivateDto, "ROLE_USER");

        //ASSERT
        //THEN
        assertEquals(expectedString, actualString);
    }

    @Test
    @Disabled
    public void updateUserReturnsTheUsersUsername() throws UserPrincipalNotFoundException {
        //ARRANGE
        User userToBeUpdated = user;
        userToBeUpdated.setEmail("newemail@gmail.com");
        UserPrivateDto putRequestBodyUserPrivateDto = UserMapper.mapToPrivateDto(userToBeUpdated);

        String expectedString = "newemail@gmail.com";

        //GIVEN
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        Mockito.when(userRepository.findById(authentication.getName())).thenReturn(Optional.of(user));
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);


        //ACT
        //WHEN
        String actualString = userServiceImpl.updateUser(user.getUsername() , putRequestBodyUserPrivateDto);

        //ASSERT
        //THEN
        assertEquals(expectedString, actualString);
    }

    @Test
    @Disabled
    public void changePasswordReturnsAnEncryptedPassword() {
        //ARRANGE
        UserInputDto userInputDto = new UserInputDto("admin","admin","admin@demodrop.com");
        String expectedEncryptedPassword = "$2y$10$Te.lNeGaDiRAUAXXpBbR7.3s9vi/K8tKGNe2EvfuEjMOquXAbR/Uy";

        //GIVEN
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(admin);
        Mockito.when(authentication.getName()).thenReturn(admin.getUsername());

        //ACT
        String actualEncryptedPassword = userServiceImpl.changePassword("admin", userInputDto);

        //ASSERT
        assertEquals(expectedEncryptedPassword, actualEncryptedPassword);

    }

    @Test
    public void changeEmailReturnsUpdateEmail() {
        //ARRANGE
        String username = user.getUsername();
        String expectedString = "newemail@gmail.com";
        UserInputDto userInputDto = new UserInputDto(null, null, expectedString);

        //GIVEN
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn(user.getUsername());
        Mockito.when(userRepository.existsById(username)).thenReturn(true);

        //ACT
        String actualString = userServiceImpl.changeEmail(username, userInputDto);

        //ASSERT
        assertEquals(expectedString, actualString);
    }

    @Test
    public void deleteUserReturnsTrue() throws UserPrincipalNotFoundException {
        //ARRANGE
        String username = user.getUsername();
        boolean expectedResult = true;
        //GIVEN
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn(user.getUsername());
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(user));
        Mockito.when(demoRepository.findByFavoriteOfUsersOrderByTitleAsc(user)).thenReturn(usersFavoriteDemoList);
        Mockito.when(demoService.setFavStatus(audioSecundo.getDemoId(), false)).thenReturn(false);

        //ACT
        //WHEN
        boolean actualResult = userServiceImpl.deleteUser(username);

        //ASSERT
        //THEN
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getAuthoritiesReturnsASetOfAuthorities() {
        //ARRANGE
        String username = user.getUsername();
        Set<Authority> expectedSetOfAuthorities = userAuthoritySet;
        //GIVEN
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn(user.getUsername());
        Mockito.when(userRepository.existsById(username)).thenReturn(true);
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(user));

        //ACT
        //WHEN
        Set<Authority> actualSetOfAuthorities = userServiceImpl.getAuthorities(username);

        //ASSERT
        //THEN
        assertEquals(expectedSetOfAuthorities, actualSetOfAuthorities);
    }

    @Test
    public void addAuthorityAddsTheCorrectAuthority() {
        //ARRANGE
        String username = user.getUsername();
        String newAuthorityForUser = "ROLE_ADMIN";

        //GIVEN
        Mockito.when(userRepository.existsById(username)).thenReturn(true);
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(user));

        //ACT
        userServiceImpl.addAuthority(username, newAuthorityForUser);

        //ASSERT
        Set<Authority> usersNewAuthoritySet = user.getAuthorities();
        List<Authority> authorityList = new ArrayList<>(usersNewAuthoritySet);
        boolean authorityFound = false;
        for (Authority authority : authorityList) {
            if (authority.getAuthority() == newAuthorityForUser) {
                authorityFound = true;
            }
        }
        assertTrue(authorityFound);
    }

    @Test
    public void removeAuthorityRemovesTheCorrectAuthority() {
        //ARRANGE
        String username = admin.getUsername();
        String authorityNameToBeRemoved = "ROLE_ADMIN";

        //GIVEN
        Mockito.when(userRepository.existsById(username)).thenReturn(true);
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(admin));

        //ACT
        userServiceImpl.removeAuthority(username, authorityNameToBeRemoved);

        //ASSERT
        Set<Authority> adminsNewAuthoritySet = user.getAuthorities();
        List<Authority> authorityList = new ArrayList<>(adminsNewAuthoritySet);
        boolean authorityFound = false;
        for (Authority authority : authorityList) {
            if (authority.getAuthority() == authorityNameToBeRemoved) {
                authorityFound = true;
            }
        }
        assertFalse(authorityFound);
    }

    @Test
    public void updateExistingUserReturnsTheCorrectUser() {
        //ARRANGE
        UserPrivateDto userPrivateDto = userUserPrivateDto;
        String newEmail = "newemail@gmail.com";
        userPrivateDto.setEmail(newEmail);
        String expectedString = newEmail;

        //ACT
        String actualString = userServiceImpl.updateExistingUser(user, userPrivateDto).getEmail();

        //ASSERT
        assertEquals(expectedString, actualString);

    }

    @Test
    public void checkAccountStatusReturnsCorrectly() {
        // ARRANGE
        String username = user.getUsername();
        user.setEnabled(false);
        boolean expectedResult = false;

        //GIVEN
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(user));

        //ACT
        boolean actualResult = userServiceImpl.checkAccountStatus(username);

        //ASSERT
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void setAccountStatusReturnsCorrectly() {
        // ARRANGE
        String username = user.getUsername();
        boolean expectedResult = false;

        //GIVEN
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(user));

        //ACT
        boolean actualResult = userServiceImpl.setAccountStatus(username, false);

        //ASSERT
        assertEquals(expectedResult, actualResult);
    }
}
