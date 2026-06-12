package com.wayneng.atms.controller;

import com.wayneng.atms.service.BalanceInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BalanceInquiryController {

    private final BalanceInquiryService balanceInquiryService;

    @GetMapping("/{sessionId}")
    public BigDecimal inquireBalance(@PathVariable String sessionId) {

        return balanceInquiryService.inquire(sessionId);
    }
}