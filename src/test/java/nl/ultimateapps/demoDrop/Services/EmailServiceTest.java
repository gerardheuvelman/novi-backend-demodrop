package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Models.EmailDetails;
import nl.ultimateapps.demoDrop.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {EmailServiceImpl.class})

public class EmailServiceTest extends ServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmailServiceImpl emailServiceImpl;

    @BeforeEach
    public void setUp() {
        // Re-initialize the entire in-memory database using the base class setup method.
        super.reInitializeInMemoryDatabase();
    }

    @Test
    @Disabled
    public void GetAudioFilesReturnAListOfAudioFileDtos() {
        //ARRANGE
        EmailDetails emailDetails = demoCreatedEmail;
        emailDetails.setRecipientUsername(user.getUsername());
        String expectedString = "Mail Sent Successfully...";


        //GIVEN
        Mockito.when((userRepository.findById(emailDetails.getRecipientUsername()))).thenReturn(Optional.of(user));

        //ACT
        //WHEN
        String actualString = emailServiceImpl.sendSimpleMail(emailDetails);

        //ASSERT
        //THEN
        assertEquals(expectedString, actualString);

    }
}
