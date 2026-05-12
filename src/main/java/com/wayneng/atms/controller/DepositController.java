package com.wayneng.atms.controller;

import com.wayneng.atms.dto.AmountRequest;
import com.wayneng.atms.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deposits")
@RequiredArgsConstructor
public class DepositController {

    private final DepositService depositService;

    @PostMapping
    public ResponseEntity<String> deposit(
            @RequestBody AmountRequest request
    ) {
        try {
            depositService.deposit(request.getSessionId(), request.getAmount());
            return ResponseEntity.ok("Deposit successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Deposit failed: " + e.getMessage());
        }
    }
}