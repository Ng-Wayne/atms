package com.wayneng.atms.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wayneng.atms.controller.SessionController;
import com.wayneng.atms.model.Session;
import com.wayneng.atms.service.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SessionControllerTest {

    private MockMvc mockMvc;
    private SessionService sessionService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        sessionService = mock(SessionService.class);
        SessionController controller = new SessionController(sessionService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void startSession_success() throws Exception {

        Session session = new Session();
        session.setSessionId("abc123");

        when(sessionService.startSession("12345678", "ATM001"))
                .thenReturn(session);

        mockMvc.perform(post("/api/sessions/start")
                        .param("cardNumber", "12345678")
                        .param("atmCode", "ATM001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value("abc123"));

        verify(sessionService).startSession("12345678", "ATM001");
    }

    @Test
    void getSession_success() throws Exception {

        Session session = new Session();
        session.setSessionId("abc123");

        when(sessionService.getSession("abc123")).thenReturn(session);

        mockMvc.perform(get("/api/sessions/{sessionId}", "abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value("abc123"));

        verify(sessionService).getSession("abc123");
    }

    @Test
    void getActiveSessionByATM_success() throws Exception {

        Session session = new Session();
        session.setSessionId("abc123");

        when(sessionService.getActiveSessionByATM("ATM001"))
                .thenReturn(session);

        mockMvc.perform(get("/api/sessions/atm/{atmCode}", "ATM001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value("abc123"));

        verify(sessionService).getActiveSessionByATM("ATM001");
    }

    @Test
    void recordFailedPin_success() throws Exception {

        doNothing().when(sessionService).recordFailedPin("abc123");

        mockMvc.perform(post("/api/sessions/{sessionId}/failed-pin", "abc123"))
                .andExpect(status().isOk());

        verify(sessionService).recordFailedPin("abc123");
    }

    @Test
    void authenticateSession_success() throws Exception {

        doNothing().when(sessionService).authenticateSession("abc123");

        mockMvc.perform(post("/api/sessions/{sessionId}/authenticate", "abc123"))
                .andExpect(status().isOk());

        verify(sessionService).authenticateSession("abc123");
    }

    @Test
    void isAuthenticated_success() throws Exception {

        when(sessionService.isAuthenticated("abc123")).thenReturn(true);

        mockMvc.perform(get("/api/sessions/{sessionId}/authenticated", "abc123"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(sessionService).isAuthenticated("abc123");
    }

    @Test
    void endSession_success() throws Exception {

        doNothing().when(sessionService)
                .endSession("abc123", "COMPLETED");

        mockMvc.perform(post("/api/sessions/{sessionId}/end", "abc123")
                        .param("reason", "COMPLETED"))
                .andExpect(status().isOk());

        verify(sessionService).endSession("abc123", "COMPLETED");
    }
}