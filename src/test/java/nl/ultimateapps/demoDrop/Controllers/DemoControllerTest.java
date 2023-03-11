package nl.ultimateapps.demoDrop.Controllers;

import nl.ultimateapps.demoDrop.Config.SpringSecurityConfig;
import nl.ultimateapps.demoDrop.DemoDropApplication;
import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Filters.JwtRequestFilter;
import nl.ultimateapps.demoDrop.Services.CustomUserDetailsService;
import nl.ultimateapps.demoDrop.Services.CustomUserDetailsServiceImpl;
import nl.ultimateapps.demoDrop.Services.DemoService;
import nl.ultimateapps.demoDrop.Utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DemoController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
//@ContextConfiguration(classes = {DemoController.class})

public class DemoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    CustomUserDetailsServiceImpl customUserDetailsService;

    @MockBean
    JwtUtil jwtUtil;

    @Autowired
    JwtRequestFilter jwtRequestFilter;




    @MockBean
    private DemoService demoService;

    @Autowired
    private DemoController demoController;

    @Test
    public void testEndpointGetDemos() throws Exception {

        //ARRANGE
        DemoDto demoDto = new DemoDto(1001L, null, "Prime Audio", null, null, null, null, null, null, null);

        List<DemoDto> allDemoDtos = Arrays.asList(demoDto);

        // ASSERT
        given(demoService.getDemos(0)).willReturn(allDemoDtos);

        //ACT
        mvc.perform(get("/demos?limit=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Prime Audio")));
    }
}