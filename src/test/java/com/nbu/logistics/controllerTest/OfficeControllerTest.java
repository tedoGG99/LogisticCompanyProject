

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
public class OfficeControllerTest {
    @Autowired
    private MockMvc mockMvc;


    //  GET /offices/create - ROLE_USER -> forbidden
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void showCreateForm_whenNotAdmin_shouldBeForbidden() throws Exception {
        mockMvc.perform(get("/offices/create"))
                .andExpect(status().isForbidden());
    }

    //  GET /offices/create - ROLE_ADMIN -> ok
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void showCreateForm_whenAdmin_shouldReturnCreateView() throws Exception {
        mockMvc.perform(get("/offices/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("office-create"))
                .andExpect(model().attributeExists("office"));
    }

    //  POST /offices/create - ROLE_USER -> forbidden
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createOffice_whenNotAdmin_shouldBeForbidden() throws Exception {
        mockMvc.perform(post("/offices/create")
                        .param("name", "Hacker Office"))
                .andExpect(status().isForbidden());
    }
}
