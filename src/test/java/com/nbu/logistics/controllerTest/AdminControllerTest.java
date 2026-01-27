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
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ----------------------------
    // AUTH TESTS
    // ----------------------------


    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void adminUsers_whenAdmin_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-users"))
                .andExpect(model().attributeExists("users"));
    }

    // ----------------------------
    // LIST OFFICES
    // ----------------------------
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void listOffices_shouldReturnAdminOfficesView() throws Exception {
        mockMvc.perform(get("/admin/offices"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-offices"))
                .andExpect(model().attributeExists("offices"));
    }

    // ----------------------------
    // SHOW EDIT FORM
    // ----------------------------
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void showEditUserForm_whenUserNotFound_shouldRedirectToUsers() throws Exception {
        mockMvc.perform(get("/admin/users/edit/999999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));
    }

    // ----------------------------
    // UPDATE USER
    // ----------------------------
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void updateUser_shouldRedirectToUsers() throws Exception {
        mockMvc.perform(post("/admin/users/update")
                        .param("id", "1")
                        .param("username", "testUser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));
    }
}
