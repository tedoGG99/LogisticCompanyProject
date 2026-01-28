
package com.nbu.logistics.controllerTest;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nbu.logistics.controllers.ReportController;
import com.nbu.logistics.data.Shipment;
import com.nbu.logistics.services.ShipmentService;
import com.nbu.logistics.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; 
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;
import java.util.List;

@WebMvcTest(ReportController.class) // <--- THIS IS THE ONLY ONE YOU NEED
@ActiveProfiles("test")
public class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;



    // ------------------------
    // REST: /reports/employee/{id}
    // ------------------------
    @MockitoBean
    private ShipmentService shipmentService;

    @MockitoBean
    private UserService userService;



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
