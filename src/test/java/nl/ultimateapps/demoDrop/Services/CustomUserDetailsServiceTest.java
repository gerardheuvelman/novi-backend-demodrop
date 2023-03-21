package nl.ultimateapps.demoDrop.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {CustomUserDetailsServiceImpl.class})
public class CustomUserDetailsServiceTest extends ServiceTest {

    @InjectMocks
    private ConversationServiceImpl conversationServiceImpl;

    @BeforeEach
    public void setUp() {
        // Re-initialize the entire in-memory database using the base class setup method.
        super.reInitializeInMemoryDatabase();
    }

    @Test
    public void loadUserByUserNameReturnsTheCorrectUserDetails() {
        //ARRANGE
        //ACT
        //ASSERT
    }
}
