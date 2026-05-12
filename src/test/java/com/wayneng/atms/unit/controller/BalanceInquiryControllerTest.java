package com.wayneng.atms.unit.controller;

import com.wayneng.atms.controller.BalanceInquiryController;
import com.wayneng.atms.service.BalanceInquiryService;
import com.wayneng.atms.service.SessionService;
import com.wayneng.atms.model.Session;
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
    private SessionService sessionService;

    @BeforeEach
    void setUp() {

        balanceInquiryService = mock(BalanceInquiryService.class);
        sessionService = mock(SessionService.class);

        BalanceInquiryController controller =
                new BalanceInquiryController(balanceInquiryService, sessionService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void inquireBalance_success() throws Exception {

        String sessionId = "abc123";
        Session mockSession = new Session();
        BigDecimal expectedBalance = new BigDecimal("1000.50");

        when(sessionService.getSession(sessionId)).thenReturn(mockSession);
        when(balanceInquiryService.inquire(mockSession)).thenReturn(expectedBalance);

        mockMvc.perform(get("/api/balance/{sessionId}", sessionId))
                .andExpect(status().isOk())
                .andExpect(content().string("1000.50"));

        verify(sessionService).getSession(sessionId);
        verify(balanceInquiryService).inquire(mockSession);
    }
}