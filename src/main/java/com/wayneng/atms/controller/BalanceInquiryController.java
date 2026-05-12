package com.wayneng.atms.controller;

import com.wayneng.atms.service.BalanceInquiryService;
import com.wayneng.atms.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
public class BalanceInquiryController {

    private final BalanceInquiryService balanceInquiryService;
    private final SessionService sessionService;

    @GetMapping("/{sessionId}")
    public BigDecimal inquireBalance(@PathVariable String sessionId) {

        return balanceInquiryService.inquire(
                sessionService.getSession(sessionId)
        );
    }
}