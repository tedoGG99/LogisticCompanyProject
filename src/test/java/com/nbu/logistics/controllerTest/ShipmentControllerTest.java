package com.nbu.logistics.controllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ShipmentControllerTestConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ShipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ------------------------
    // GET /shipments/create
    // ------------------------
    @Test
    @WithMockUser(username = "employee", authorities = {"ROLE_OFFICE EMPLOYEE"})
    void showCreateForm_shouldReturnCreateView() throws Exception {
        mockMvc.perform(get("/shipments/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("shipment-create"))
                .andExpect(model().attributeExists("shipmentDto"))
                .andExpect(model().attributeExists("offices"))
                .andExpect(model().attributeExists("deliveryTypes"))
                .andExpect(model().attributeExists("clients"))
                .andExpect(model().attributeExists("basePrice"));
    }

    // ------------------------
    // POST /shipments/create
    // ------------------------
    @Test
    @WithMockUser(username = "employee", authorities = {"ROLE_OFFICE EMPLOYEE"})
    void createShipment_shouldRedirect() throws Exception {
        mockMvc.perform(post("/shipments/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/shipments"));
    }

    // ------------------------
    // POST /shipments/{id}/status
    // ------------------------
    @Test
    @WithMockUser(username = "courier", authorities = {"ROLE_COURIER"})
    void updateStatus_shouldRedirect() throws Exception {
        mockMvc.perform(post("/shipments/1/status")
                        .param("status", "DELIVERED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/shipments"));
    }

    // ------------------------
    // POST /shipments/{id}/delete
    // ------------------------
    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void deleteShipment_shouldRedirect() throws Exception {
        mockMvc.perform(post("/shipments/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/shipments"));
    }


    // ------------------------
    // GET /shipments/whoami
    // ------------------------
    @Test
    @WithMockUser(username = "testUser", authorities = {"ROLE_USER"})
    void whoAmI_shouldReturnUsername() throws Exception {
        mockMvc.perform(get("/shipments/whoami"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("testUser")));
    }

    // ------------------------
    // POST /shipments/{id}/assign-courier
    // ------------------------
    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void assignCourier_shouldRedirect() throws Exception {
        mockMvc.perform(post("/shipments/1/assign-courier")
                        .param("courierId", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/shipments"));
    }
}
