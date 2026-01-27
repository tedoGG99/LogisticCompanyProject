package com.nbu.logistics.controllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ReportControllerTestConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;



    // ------------------------
    // REST: /reports/employee/{id}
    // ------------------------
    @Test
    @WithMockUser
    void getShipmentsByEmployee_shouldReturnJsonList() throws Exception {
        mockMvc.perform(get("/reports/employee/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].price").value(100.00))
                .andExpect(jsonPath("$[0].receiver").value("Receiver Test"));
    }

    // ------------------------
    // REST: /reports/client/{id}
    // ------------------------
    @Test
    @WithMockUser
    void getShipmentsByClient_shouldReturnJsonList() throws Exception {
        mockMvc.perform(get("/reports/client/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    // ------------------------
    // REST: /reports/unreceived
    // ------------------------
    @Test
    @WithMockUser
    void getUnreceivedShipments_shouldReturnJsonList() throws Exception {
        mockMvc.perform(get("/reports/unreceived"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }
}
