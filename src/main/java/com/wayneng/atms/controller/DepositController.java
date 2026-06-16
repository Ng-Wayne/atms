package com.wayneng.atms.controller;

import com.wayneng.atms.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/deposit")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DepositController {

    private final DepositService depositService;

    @PostMapping
    public ResponseEntity<String> deposit(
            @RequestParam String sessionId,
            @RequestParam BigDecimal amount
    ) {
        try {
            depositService.deposit(sessionId, amount);
            return ResponseEntity.ok("Deposit successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Deposit failed: " + e.getMessage());
        }
    }
}