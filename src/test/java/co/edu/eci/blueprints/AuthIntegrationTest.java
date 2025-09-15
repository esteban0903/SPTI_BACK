package co.edu.eci.blueprints;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginSuccessReturnsToken() throws Exception {
    String body = "{\"username\":\"student\",\"password\":\"student123\"}";
    mockMvc.perform(post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.access_token").exists());
    }

    @Test
    void loginFailReturns401() throws Exception {
    String body = "{\"username\":\"student\",\"password\":\"wrong\"}";
    mockMvc.perform(post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body))
        .andExpect(status().isUnauthorized());
    }

    @Test
    void accessProtectedEndpointWithToken() throws Exception {
    String body = "{\"username\":\"student\",\"password\":\"student123\"}";
    MvcResult result = mockMvc.perform(post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body))
        .andExpect(status().isOk())
        .andReturn();
    String token = com.jayway.jsonpath.JsonPath.read(result.getResponse().getContentAsString(), "$.access_token");
    mockMvc.perform(get("/api/blueprints")
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
    }

    @Test
    void accessProtectedEndpointWithoutToken() throws Exception {
    mockMvc.perform(get("/api/blueprints"))
        .andExpect(status().isUnauthorized());
    }
}
