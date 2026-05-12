package com.wayneng.atms.service;

import com.wayneng.atms.model.Session;
import java.math.BigDecimal;

public interface WithdrawalService {

    void withdraw(String sessionId, BigDecimal amount);
}
