package com.wayneng.atms.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wayneng.atms.controller.DepositController;
import com.wayneng.atms.dto.AmountRequest;
import com.wayneng.atms.service.DepositService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.math.BigDecimal;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DepositControllerTest {

    private MockMvc mockMvc;
    private DepositService depositService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        depositService = mock(DepositService.class);
        DepositController controller = new DepositController(depositService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void deposit_success() throws Exception {
        AmountRequest request = new AmountRequest();
        request.setSessionId("abc123");
        request.setAmount(new BigDecimal("500.00"));

        doNothing().when(depositService)
                .deposit("abc123", new BigDecimal("500.00"));

        mockMvc.perform(post("/api/deposits")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Deposit successful"));

        verify(depositService)
                .deposit("abc123", new BigDecimal("500.00"));
    }

    @Test
    void deposit_failure() throws Exception {
        AmountRequest request = new AmountRequest();
        request.setSessionId("abc123");
        request.setAmount(new BigDecimal("500.00"));

        doThrow(new RuntimeException("Insufficient funds"))
                .when(depositService)
                .deposit("abc123", new BigDecimal("500.00"));

        mockMvc.perform(post("/api/deposits")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Deposit failed: Insufficient funds"));

        verify(depositService)
                .deposit("abc123", new BigDecimal("500.00"));
    }
}