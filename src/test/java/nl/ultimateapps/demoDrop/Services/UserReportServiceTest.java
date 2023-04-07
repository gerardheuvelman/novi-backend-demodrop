package nl.ultimateapps.demoDrop.Services;


import nl.ultimateapps.demoDrop.Dtos.output.UserReportDto;
import nl.ultimateapps.demoDrop.Helpers.mappers.UserReportMapper;
import nl.ultimateapps.demoDrop.Models.*;
import nl.ultimateapps.demoDrop.Repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {UserReportServiceImpl.class})
public class UserReportServiceTest extends ServiceTest {


    @Mock
    private UserReportRepository userReportRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserReportServiceImpl userReportServiceImpl;

    @BeforeEach
    public void setUp() {
        // Re-initialize the entire in-memory database using the base class setup method.
        super.reInitializeInMemoryDatabase();
    }

    @Test
    public void GetUserReportsReturnAListOfUserReportDtos() {
        //ARRANGE
        List<UserReportDto> expectedUserReportDtoList = allUserReportDtos;

        //GIVEN
        Mockito.when(userReportRepository.findAll()).thenReturn(allUserReports);

        //ACT
        //WHEN
        List<UserReportDto> actualUserReportDtoList = userReportServiceImpl.getUserReports(0);

        //ASSERT
        //THEN
        assertEquals(expectedUserReportDtoList, actualUserReportDtoList);
    }

    @Test
    public void GetUserReportReturnsAUserReportDto() {
        //ARRANGE
        UserReport userReport = userReport1;
        userReport.setReporter(user);
        userReport.setReportedDemo(primeAudio);
        long userReportId = userReport.getUserReportId();
        UserReportDto expectedUserReportDto = userReport1Dto;

        //GIVEN
        Mockito.when(userReportRepository.findById(userReport.getUserReportId())).thenReturn(Optional.of(userReport));

        //ACT
        //WHEN
        UserReportDto actualUserReportDto = userReportServiceImpl.getUserReport(userReportId);


        //ASSERT
        //THEN
        assertEquals(expectedUserReportDto.getSubject(), actualUserReportDto.getSubject());
    }

    @Test
    public void CreateUserReportReturnsAUserReportDto() throws UserPrincipalNotFoundException {
        //ARRANGE
        UserReportDto newUserReportDto = new UserReportDto(null, null, "user" ,"About user 'gerard' ", "body3",  null, null, null, null);
        newUserReportDto.setReportedUser(user.toPublicDto());

        UserReport newUserReport = UserReportMapper.mapToModel(newUserReportDto);
        newUserReport.setUserReportId(4003L);

        UserReportDto expectedUserReportDto = UserReportMapper.mapToDto(newUserReport);

        //GIVEN
        Mockito.when(userRepository.findById(newUserReportDto.getReportedUser().getUsername())).thenReturn(Optional.of(user));
        Mockito.when(userReportRepository.save(any(UserReport.class))).thenReturn(newUserReport);
        // Fake a user with admin authority
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn(user.getUsername());

        String expectedSubject = newUserReportDto.getSubject();

        //ACT
        //WHEN
        UserReportDto returnedUserReportDto = userReportServiceImpl.createUserReport(newUserReportDto);

        //ASSERT
        //THEN
        assertEquals(expectedSubject, returnedUserReportDto.getSubject());
    }


    @Test
    public void deleteUserReportsReturnsTheNumberOfDeletedUserReports() {
        //ARRANGE
        long expectedNumDeletedUserReports = 2L;

        //GIVEN
        Mockito.when((userReportRepository.findAll())).thenReturn(allUserReports);

        //ACT
        long actuaNumDeletedUserReports = userReportServiceImpl.deleteUserReports();


        //ASSERT
        assertEquals(expectedNumDeletedUserReports, actuaNumDeletedUserReports);
    }

    @Test
    public void deleteUserReportReturnsTheIdOfTheDeletedUserReport() {
        //ARRANGE
        UserReport userReport = userReport1;
        userReport.setReporter(user);
        userReport.setReportedDemo(primeAudio);

        long expectedUserReportId = userReport.getUserReportId();

        //GIVEN
        Mockito.when(userReportRepository.findById(userReport.getUserReportId())).thenReturn(Optional.of(userReport));

        //ACT
        long actualUserReportId = userReportServiceImpl.deleteUserReport(userReport.getUserReportId());

        //ASSERT
        assertEquals(expectedUserReportId, actualUserReportId);
    }
}
