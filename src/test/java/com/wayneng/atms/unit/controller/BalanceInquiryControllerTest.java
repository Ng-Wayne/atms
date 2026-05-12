package com.wayneng.atms.unit.controller;

import com.wayneng.atms.controller.BalanceInquiryController;
import com.wayneng.atms.service.BalanceInquiryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

class BalanceInquiryControllerTest {

    private MockMvc mockMvc;

    private BalanceInquiryService balanceInquiryService;

    @BeforeEach
    void setUp() {

        balanceInquiryService = mock(BalanceInquiryService.class);

        BalanceInquiryController controller =
                new BalanceInquiryController(balanceInquiryService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void inquireBalance_success() throws Exception {

        String sessionId = "abc123";
        BigDecimal expectedBalance = new BigDecimal("1000.50");

        when(balanceInquiryService.inquire(sessionId)).thenReturn(expectedBalance);

        mockMvc.perform(get("/api/balance/{sessionId}", sessionId))
                .andExpect(status().isOk())
                .andExpect(content().string("1000.50"));

        verify(balanceInquiryService).inquire(sessionId);
    }
}