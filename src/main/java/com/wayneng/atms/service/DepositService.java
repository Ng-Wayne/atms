package com.wayneng.atms.service;

import java.math.BigDecimal;

public interface DepositService {

    void deposit(String sessionId, BigDecimal amount);
}
