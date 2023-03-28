package nl.ultimateapps.demoDrop.IntegrationTests;

import nl.ultimateapps.demoDrop.Models.Genre;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Filters.JwtRequestFilter;
import nl.ultimateapps.demoDrop.Services.CustomUserDetailsServiceImpl;
import nl.ultimateapps.demoDrop.Utils.JwtUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class DemoIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    CustomUserDetailsServiceImpl customUserDetailsService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Test
    public void testEndpointGetDemos() throws Exception {

        //ARRANGE
        String url = "/demos?limit=0";

        //ACT
        ResultActions resultActions = mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));
        // ASSERT
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(16)));
    }

    @Test
    @Disabled
    public void testEndpointGetDemosByGenre() throws Exception {

        //ARRANGE
        String url = "/demos/bygenre?genre=Alternative%20dance&limit=0";

        //ACT
        ResultActions resultActions = mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));
        // ASSERT
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testEndpointGetDemo() throws Exception {

        //ARRANGE
        String url = "/demos/1001";

        //ACT
        ResultActions resultActions = mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));
        // ASSERT
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Prime Audio"));
    }

    @Test
    public void testEndpointDownloadMp3File() throws Exception {

        //ARRANGE
        String url = "/demos/1001/download";

        //ACT
        ResultActions resultActions = mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));
        // ASSERT
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @Disabled
    public void testEndpointCreateDemo() throws Exception {

        // ARRANGE
        String url = "/demos";

        Genre newGenre = new Genre();
        newGenre.setName("New Genre");

        DemoDto demoDTO = new DemoDto();
        demoDTO.setTitle("New Demo");
        demoDTO.setLength(120.0);
        demoDTO.setBpm(140.0);
        demoDTO.setAudioFile(null);
        demoDTO.setGenre(newGenre);

        ObjectMapper objectMapper = new ObjectMapper();
        String demoJson = objectMapper.writeValueAsString(demoDTO);

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJnZXJhcmQiLCJleHAiOjE2ODExMzU1ODMsImlhdCI6MTY3OTMwODE5Nn0.HYGP5b5awGAj1rzjaUOVRAIfbRKvcShFXI8YkZjQxqA";

        // ACT
        ResultActions resultActions = mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .with(user("gerard").password("acme").roles("USER"))
                .content(demoJson));

        // ASSERT
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Demo"))
                .andExpect(jsonPath("$.length").value(120.0));
    }
}