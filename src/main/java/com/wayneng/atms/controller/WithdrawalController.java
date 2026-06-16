package com.wayneng.atms.controller;

import com.wayneng.atms.service.WithdrawalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/withdraw")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    @PostMapping
    public ResponseEntity<?> withdraw(
            @RequestParam String sessionId,
            @RequestParam BigDecimal amount
    ) {
        try {
            withdrawalService.withdraw(sessionId, amount);
            return ResponseEntity.ok("Withdrawal successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Withdrawal failed: " + e.getMessage());
        }
    }
}