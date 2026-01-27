package com.nbu.logistics.controllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;


    //  GET /login
    @Test
    void loginPage_shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    //  GET /register
    @Test
    void showRegistrationForm_shouldReturnRegisterViewWithModelAttributes() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("offices"))
                .andExpect(model().attributeExists("roles"));
    }


    //  POST /register (fail - runtime exception -> show register)
    @Test
    void registerUser_whenServiceThrows_shouldReturnRegisterWithError() throws Exception {
        // това целим да предизвика exception, password mismatch
        mockMvc.perform(post("/register")
                        .param("username", "testuser2")
                        .param("password", "12345")
                        .param("confirmPassword", "wrong")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("offices"));
    }
}
