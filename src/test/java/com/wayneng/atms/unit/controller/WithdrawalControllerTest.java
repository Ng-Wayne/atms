package com.wayneng.atms.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wayneng.atms.controller.WithdrawalController;
import com.wayneng.atms.dto.AmountRequest;
import com.wayneng.atms.service.WithdrawalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.math.BigDecimal;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class WithdrawalControllerTest {

    private MockMvc mockMvc;
    private WithdrawalService withdrawalService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        withdrawalService = mock(WithdrawalService.class);
        WithdrawalController controller = new WithdrawalController(withdrawalService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void withdraw_success() throws Exception {

        AmountRequest request = new AmountRequest();
        request.setSessionId("abc123");
        request.setAmount(new BigDecimal("200"));

        doNothing().when(withdrawalService)
                .withdraw("abc123", new BigDecimal("200"));

        mockMvc.perform(post("/api/withdrawal")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Withdrawal successful"));

        verify(withdrawalService)
                .withdraw("abc123", new BigDecimal("200"));
    }

    @Test
    void withdraw_failure() throws Exception {

        AmountRequest request = new AmountRequest();
        request.setSessionId("abc123");
        request.setAmount(new BigDecimal("200"));

        doThrow(new RuntimeException("Insufficient balance"))
                .when(withdrawalService)
                .withdraw("abc123", new BigDecimal("200"));

        mockMvc.perform(post("/api/withdrawal")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Withdrawal failed: Insufficient balance"));

        verify(withdrawalService)
                .withdraw("abc123", new BigDecimal("200"));
    }
}